package co.kluvaka.cmp.features.sessions.ui.active

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEventType
import co.kluvaka.cmp.features.sessions.domain.usecase.AddSessionEvent
import co.kluvaka.cmp.features.sessions.domain.usecase.FinishActiveSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetActiveFishingSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetSessionById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.toLocalDateTime

class ActiveSessionViewModel(
  private val getActiveFishingSession: GetActiveFishingSession,
  private val getSessionById: GetSessionById,
  private val finishActiveSession: FinishActiveSession,
  private val addSessionEvent: AddSessionEvent,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(ActiveSessionState(null))
  val state: StateFlow<ActiveSessionState> = _mutableState

  fun getActiveSession() {
    viewModelScope.launch {
      val activeSession = getActiveFishingSession()
      val session = activeSession?.id?.let { getSessionById(it) } ?: activeSession
      _mutableState.update {
        it.copy(
          session = session,
          events = session?.events ?: emptyList()
        )
      }
    }
  }

  fun getActiveSessionById(sessionId: Int) {
    viewModelScope.launch {
      val session = getSessionById(sessionId)
      _mutableState.update {
        it.copy(
          session = session,
          events = session.events
        )
      }
    }
  }

  fun finishSession() {
    viewModelScope.launch {
      state.value.session?.let { activeSession ->
        finishActiveSession(activeSession.copy(isActive = false))
      }
    }
  }
  fun showEventTypeDialog() {
    _mutableState.update { 
      it.copy(
        showEventTypeDialog = true
      ) 
    }
  }

  fun hideEventTypeDialog() {
    _mutableState.update { it.copy(showEventTypeDialog = false) }
  }

  fun selectEventType(eventType: FishingSessionEventType) {
    _mutableState.update { 
      it.copy(
        selectedEventType = eventType,
        showEventTypeDialog = false
      ) 
    }

    val sessionRods = state.value.session?.rods
    val singleRodId = if (sessionRods?.size == 1) sessionRods.first().order else null

    when (eventType) {
      is FishingSessionEventType.Fish -> {
        if (singleRodId != null) {
          selectRod(singleRodId)
        } else {
          showRodSelectionDialog()
        }
      }
      is FishingSessionEventType.Loose -> {
        if (singleRodId != null) {
          selectRod(singleRodId)
        } else {
          showRodSelectionDialog()
        }
      }
      is FishingSessionEventType.Spomb -> showSpombEventDialog()
    }
  }

  fun showRodSelectionDialog() {
    _mutableState.update { it.copy(showRodSelectionDialog = true) }
  }

  fun hideRodSelectionDialog() {
    _mutableState.update { it.copy(showRodSelectionDialog = false) }
  }

  fun selectRod(rodId: Int) {
    _mutableState.update { 
      it.copy(
        selectedRodId = rodId,
        showRodSelectionDialog = false
      ) 
    }
    when (state.value.selectedEventType) {
      is FishingSessionEventType.Fish -> showFishEventDialog()
      is FishingSessionEventType.Loose -> showFishLooseDialog()
      else -> {}
    }
  }

  fun showFishEventDialog() {
    _mutableState.update { it.copy(showFishEventDialog = true) }
  }

  fun hideFishEventDialog() {
    _mutableState.update { it.copy(showFishEventDialog = false) }
  }

  fun showSpombEventDialog() {
    _mutableState.update { it.copy(showSpombEventDialog = true) }
  }

  fun hideSpombEventDialog() {
    _mutableState.update { it.copy(showSpombEventDialog = false) }
  }

  fun showFishLooseDialog() {
    _mutableState.update { it.copy(showFishLooseDialog = true) }
  }

  fun hideFishLooseDialog() {
    _mutableState.update { it.copy(showFishLooseDialog = false) }
  }

  fun updateEventWeight(weight: String) {
    _mutableState.update { it.copy(newEventWeight = weight) }
  }

  fun updateEventNotes(notes: String) {
    _mutableState.update { it.copy(newEventNotes = notes) }
  }

  fun addEventPhoto(photo: String) {
    _mutableState.update { it.copy(newEventPhotos = it.newEventPhotos + photo) }
  }

  fun removeEventPhoto(index: Int) {
    _mutableState.update {
      val photos = it.newEventPhotos.toMutableList()
      if (index in photos.indices) {
        photos.removeAt(index)
      }
      it.copy(newEventPhotos = photos)
    }
  }

  fun updateSpombCount(count: String) {
    _mutableState.update { it.copy(newSpombCount = count) }
  }

  fun addFishEvent() {
    val event = FishingSessionEvent(
      id = (state.value.events.size + 1),
      type = FishingSessionEventType.Fish(state.value.selectedRodId ?: 1),
      timestamp = getCurrentTimestamp(),
      weight = state.value.newEventWeight.takeIf { it.isNotEmpty() },
      photos = state.value.newEventPhotos,
      notes = state.value.newEventNotes.takeIf { it.isNotEmpty() }
    )
    viewModelScope.launch {
      state.value.session?.id?.let { sessionId ->
        addSessionEvent(sessionId, event)
        refreshCurrentSessionFromDb(sessionId)
      }
    }
    _mutableState.update { 
      it.copy(
        showFishEventDialog = false,
        newEventWeight = "",
        newEventNotes = "",
        newEventPhotos = emptyList(),
        selectedEventType = null,
        selectedRodId = null
      ) 
    }
  }

  fun addSpombEvent() {
    val event = FishingSessionEvent(
      id = (state.value.events.size + 1),
      type = FishingSessionEventType.Spomb(state.value.newSpombCount.toIntOrNull() ?: 1),
      timestamp = getCurrentTimestamp(),
      photos = state.value.newEventPhotos,
      notes = state.value.newEventNotes.takeIf { it.isNotEmpty() }
    )
    viewModelScope.launch {
      state.value.session?.id?.let { sessionId ->
        addSessionEvent(sessionId, event)
        refreshCurrentSessionFromDb(sessionId)
      }
    }
    _mutableState.update { 
      it.copy(
        showSpombEventDialog = false,
        newSpombCount = "",
        newEventNotes = "",
        newEventPhotos = emptyList(),
        selectedEventType = null
      ) 
    }
  }

  fun confirmFishLooseEvent() {
    val event = FishingSessionEvent(
      id = (state.value.events.size + 1),
      type = FishingSessionEventType.Loose(state.value.selectedRodId ?: 1),
      timestamp = getCurrentTimestamp(),
      photos = state.value.newEventPhotos,
      notes = state.value.newEventNotes.takeIf { it.isNotEmpty() }
    )
    viewModelScope.launch {
      state.value.session?.id?.let { sessionId ->
        addSessionEvent(sessionId, event)
        refreshCurrentSessionFromDb(sessionId)
      }
    }
    _mutableState.update { 
      it.copy(
        showFishLooseDialog = false,
        newEventNotes = "",
        newEventPhotos = emptyList(),
        selectedEventType = null,
        selectedRodId = null
      ) 
    }
  }

  fun showFinishSessionDialog() {
    _mutableState.update { it.copy(showFinishSessionDialog = true) }
  }

  fun hideFinishSessionDialog() {
    _mutableState.update { it.copy(showFinishSessionDialog = false) }
  }

  fun updateSessionNotes(notes: String) {
    _mutableState.update { it.copy(sessionNotes = notes) }
  }

  fun finishSessionWithNotes() {
    viewModelScope.launch {
      state.value.session?.let { activeSession ->
        val sessionWithEvents = activeSession.copy(
          isActive = false,
          events = state.value.events
        )
        finishActiveSession(sessionWithEvents)
      }
      _mutableState.update { it.copy(showFinishSessionDialog = false) }
    }
  }

  private fun getCurrentTimestamp(): String {
    val now = kotlinx.datetime.Clock.System.now()
    val localDateTime = now.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
    return "${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}"
  }

  private fun refreshCurrentSessionFromDb(sessionId: Int) {
    viewModelScope.launch {
      val session = getSessionById(sessionId)
      _mutableState.update {
        it.copy(
          session = session,
          events = session.events
        )
      }
    }
  }
}
