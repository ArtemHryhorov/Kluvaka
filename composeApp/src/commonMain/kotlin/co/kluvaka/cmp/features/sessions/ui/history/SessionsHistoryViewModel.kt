package co.kluvaka.cmp.features.sessions.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession
import co.kluvaka.cmp.features.sessions.domain.usecase.DeleteFishingSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetAllFishingSessions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionsHistoryViewModel(
  private val getAllFishingSessions: GetAllFishingSessions,
  private val deleteFishingSession: DeleteFishingSession,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(SessionsHistoryState())
  val state: StateFlow<SessionsHistoryState> = _mutableState

  fun getAllSessions() {
    viewModelScope.launch {
      val sessions = getAllFishingSessions().reversed()
      _mutableState.update {
        it.copy(
          sessions = sessions,
        )
      }
    }
  }

  fun showDeleteDialog(session: FishingSession) {
    _mutableState.update {
      it.copy(deleteDialogState = DialogState.Shown(session))
    }
  }

  fun hideDeleteDialog() {
    _mutableState.update {
      it.copy(deleteDialogState = DialogState.Hidden)
    }
  }

  fun deleteSession(session: FishingSession) {
    val sessionId = session.id ?: return
    viewModelScope.launch {
      deleteFishingSession(sessionId)
      hideDeleteDialog()
      getAllSessions()
    }
  }
}