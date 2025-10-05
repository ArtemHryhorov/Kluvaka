package co.kluvaka.cmp.features.sessions.ui.active

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEventType
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
) : ViewModel() {

  private val _mutableState = MutableStateFlow(ActiveSessionState(null))
  val state: StateFlow<ActiveSessionState> = _mutableState

  fun getActiveSession() {
    viewModelScope.launch {
      _mutableState.update {
        it.copy(session = getActiveFishingSession())
      }
    }
  }

  fun getActiveSessionById(sessionId: Int) {
    viewModelScope.launch {
      _mutableState.update {
        it.copy(session = getSessionById(sessionId))
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

  fun showAddEventDialog() {
    _mutableState.update { it.copy(showAddEventDialog = true) }
  }

  fun hideAddEventDialog() {
    _mutableState.update { it.copy(showAddEventDialog = false) }
  }

  fun showEventTypeDialog() {
    _mutableState.update { 
      it.copy(
        showAddEventDialog = false,
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
    when (eventType) {
      is FishingSessionEventType.Fish -> showRodSelectionDialog()
      is FishingSessionEventType.Loose -> showRodSelectionDialog()
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
      is FishingSessionEventType.Loose -> addFishLooseEvent()
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

  fun updateEventWeight(weight: String) {
    _mutableState.update { it.copy(newEventWeight = weight) }
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
      photos = state.value.newEventPhotos
    )
    _mutableState.update { 
      it.copy(
        events = it.events + event,
        showFishEventDialog = false,
        newEventWeight = "",
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
      timestamp = getCurrentTimestamp()
    )
    _mutableState.update { 
      it.copy(
        events = it.events + event,
        showSpombEventDialog = false,
        newSpombCount = "",
        selectedEventType = null
      ) 
    }
  }

  fun addFishLooseEvent() {
    val event = FishingSessionEvent(
      id = (state.value.events.size + 1),
      type = FishingSessionEventType.Loose(state.value.selectedRodId ?: 1),
      timestamp = getCurrentTimestamp()
    )
    _mutableState.update { 
      it.copy(
        events = it.events + event,
        showFishLooseDialog = false,
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
        finishActiveSession(activeSession.copy(isActive = false))
      }
      _mutableState.update { it.copy(showFinishSessionDialog = false) }
    }
  }

  private fun getCurrentTimestamp(): String {
    val now = kotlinx.datetime.Clock.System.now()
    val localDateTime = now.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
    return "${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}"
  }
}
