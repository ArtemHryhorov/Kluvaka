package co.kluvaka.cmp.features.sessions.ui.completed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.sessions.domain.usecase.GetSessionById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CompletedSessionViewModel(
  private val getSessionById: GetSessionById,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(CompletedSessionState(null))
  val state: StateFlow<CompletedSessionState> = _mutableState

  fun loadSession(sessionId: Int) {
    viewModelScope.launch {
      try {
        val session = getSessionById(sessionId)
        _mutableState.update { currentState ->
          currentState.copy(session = session, events = session.events)
        }
      } catch (e: Exception) {
        _mutableState.update { currentState ->
          currentState.copy(session = null)
        }
      }
    }
  }

}
