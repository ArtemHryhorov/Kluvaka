package co.kluvaka.cmp.features.sessions.ui.active

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEventType
import co.kluvaka.cmp.features.sessions.domain.model.SessionMode
import co.kluvaka.cmp.features.sessions.domain.usecase.AddSessionEvent
import co.kluvaka.cmp.features.sessions.domain.usecase.FinishActiveSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetActiveFishingSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetSessionById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.toLocalDateTime

class SessionViewModel(
  private val getActiveFishingSession: GetActiveFishingSession,
  private val getSessionById: GetSessionById,
  private val finishActiveSession: FinishActiveSession,
  private val addSessionEvent: AddSessionEvent,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(SessionState(null))
  val state: StateFlow<SessionState> = _mutableState

  fun loadSession(mode: SessionMode, sessionId: Int?) {
    viewModelScope.launch {
      val session = when (mode) {
        SessionMode.Active -> {
          sessionId?.let { getSessionById(it) } ?: run {
            val activeSession = getActiveFishingSession()
            activeSession?.id?.let { getSessionById(it) } ?: activeSession
          }
        }
        SessionMode.Completed -> sessionId?.let { getSessionById(it) }
      }

      _mutableState.update {
        it.copy(
          mode = mode,
          session = session,
          events = session?.events ?: emptyList()
        )
      }
    }
  }

  fun showEventTypeDialog() {
    if (isReadOnlyMode()) return
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
    if (isReadOnlyMode()) return
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
    if (isReadOnlyMode()) return
    _mutableState.update { it.copy(showRodSelectionDialog = true) }
  }

  fun hideRodSelectionDialog() {
    _mutableState.update { it.copy(showRodSelectionDialog = false) }
  }

  fun selectRod(rodId: Int) {
    if (isReadOnlyMode()) return
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
    if (isReadOnlyMode()) return
    _mutableState.update { it.copy(showFishEventDialog = true) }
  }

  fun hideFishEventDialog() {
    _mutableState.update { it.copy(showFishEventDialog = false) }
  }

  fun showSpombEventDialog() {
    if (isReadOnlyMode()) return
    _mutableState.update { it.copy(showSpombEventDialog = true) }
  }

  fun hideSpombEventDialog() {
    _mutableState.update { it.copy(showSpombEventDialog = false) }
  }

  fun showFishLooseDialog() {
    if (isReadOnlyMode()) return
    _mutableState.update { it.copy(showFishLooseDialog = true) }
  }

  fun hideFishLooseDialog() {
    _mutableState.update { it.copy(showFishLooseDialog = false) }
  }

  fun updateEventWeight(weight: String) {
    if (isReadOnlyMode()) return
    _mutableState.update { it.copy(newEventWeight = weight) }
  }

  fun updateEventNotes(notes: String) {
    if (isReadOnlyMode()) return
    _mutableState.update { it.copy(newEventNotes = notes) }
  }

  fun addEventPhoto(photo: String) {
    if (isReadOnlyMode()) return
    _mutableState.update { it.copy(newEventPhotos = it.newEventPhotos + photo) }
  }

  fun removeEventPhoto(index: Int) {
    if (isReadOnlyMode()) return
    _mutableState.update {
      val photos = it.newEventPhotos.toMutableList()
      if (index in photos.indices) {
        photos.removeAt(index)
      }
      it.copy(newEventPhotos = photos)
    }
  }

  fun updateSpombCount(count: String) {
    if (isReadOnlyMode()) return
    _mutableState.update { it.copy(newSpombCount = count) }
  }

  fun addFishEvent() {
    if (isReadOnlyMode()) return
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
    if (isReadOnlyMode()) return
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
    if (isReadOnlyMode()) return
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
    if (isReadOnlyMode()) return
    _mutableState.update { it.copy(showFinishSessionDialog = true) }
  }

  fun hideFinishSessionDialog() {
    _mutableState.update { it.copy(showFinishSessionDialog = false) }
  }

  fun updateSessionNotes(notes: String) {
    if (isReadOnlyMode()) return
    _mutableState.update { it.copy(sessionNotes = notes) }
  }

  fun finishSessionWithNotes() {
    if (state.value.mode == SessionMode.Completed) return
    viewModelScope.launch {
      state.value.session?.let { activeSession ->
        val sessionWithEvents = activeSession.copy(isActive = false)
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

  private fun isReadOnlyMode(): Boolean = state.value.mode == SessionMode.Completed
}
