package co.kluvaka.cmp.features.sessions.ui.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.sessions.domain.usecase.DeleteFishingSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetAllFishingSessions
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Actions
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Events
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionsViewModel(
  private val getAllFishingSessions: GetAllFishingSessions,
  private val deleteFishingSession: DeleteFishingSession,
  private val reducer: SessionsReducer,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(SessionsState.Initial)
  val state: StateFlow<SessionsState> = _mutableState

  fun handleAction(actions: Actions) {
    when (actions) {
      Actions.FetchSessions -> handleFetchSessions()
      Actions.DeleteSessionCancel -> dispatchOperation(actions)
      is Actions.DeleteSessionConfirm -> handleDeleteSession(actions)
      is Actions.DeleteSessionRequest -> dispatchOperation(actions)
    }
  }

  private fun dispatchOperation(operation: SessionsOperation) {
    _mutableState.update { currentState ->
      reducer.updateState(currentState, operation)
    }
  }

  private fun handleFetchSessions() {
    viewModelScope.launch {
      val sessions = getAllFishingSessions()
      val event = Events.FetchSessionsObserved(sessions)
      dispatchOperation(event)
    }
  }

  private fun handleDeleteSession(actions: Actions.DeleteSessionConfirm) {
    viewModelScope.launch {
      deleteFishingSession(actions.id)
      handleAction(Actions.FetchSessions)
    }
  }
}
