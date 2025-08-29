package co.kluvaka.cmp.features.sessions.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.sessions.domain.usecase.GetAllFishingSessions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionsHistoryViewModel(
  private val getAllFishingSessions: GetAllFishingSessions,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(SessionsHistoryState())
  val state: StateFlow<SessionsHistoryState> = _mutableState

  fun getAllSessions() {
    viewModelScope.launch {
      val sessions = getAllFishingSessions()
      _mutableState.update {
        it.copy(
          sessions = sessions,
        )
      }
    }
  }
}