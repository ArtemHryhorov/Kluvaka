package co.kluvaka.cmp.features.sessions.ui.active

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.sessions.domain.usecase.FinishActiveSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetActiveFishingSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActiveSessionViewModel(
  private val getActiveFishingSession: GetActiveFishingSession,
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

  fun finishSession() {
    viewModelScope.launch {
      state.value.session?.let { activeSession ->
        finishActiveSession(activeSession.copy(isActive = false))
      }
    }
  }
}
