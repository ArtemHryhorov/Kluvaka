package co.kluvaka.cmp.features.sessions.ui.sessions

import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Actions
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Events

class SessionsReducer {
  fun updateState(
    currentState: SessionsState,
    operation: SessionsOperation,
  ): SessionsState = when (operation) {
    is Actions.FetchSessions -> currentState
    is Events.FetchSessionsObserved -> handleFetchSessionsObserved(currentState, operation)
    is Actions.DeleteSessionCancel -> handleDeleteSessionCancel(currentState)
    is Actions.DeleteSessionConfirm -> currentState
    is Actions.DeleteSessionRequest -> handleDeleteSessionRequest(currentState, operation)
  }

  private fun handleDeleteSessionCancel(
    currentState: SessionsState,
  ): SessionsState = currentState.copy(
    deleteConfirmationDialog = DialogState.Hidden,
  )

  private fun handleDeleteSessionRequest(
    currentState: SessionsState,
    action: Actions.DeleteSessionRequest,
  ): SessionsState = currentState.copy(
    deleteConfirmationDialog = DialogState.Shown(action.session),
  )

  private fun handleFetchSessionsObserved(
    currentState: SessionsState,
    event: Events.FetchSessionsObserved,
  ): SessionsState = currentState.copy(
    deleteConfirmationDialog = DialogState.Hidden,
    sessions = event.payload,
  )
}
