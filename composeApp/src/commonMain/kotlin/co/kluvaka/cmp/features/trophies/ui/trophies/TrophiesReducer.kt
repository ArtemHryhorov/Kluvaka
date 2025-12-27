package co.kluvaka.cmp.features.trophies.ui.trophies

import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesOperation.Actions
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesOperation.Events

class TrophiesReducer {
  fun updateState(
    currentState: TrophiesState,
    operation: TrophiesOperation,
  ): TrophiesState = when (operation) {
    is Actions.DeleteTrophyCancel -> handleDeleteTrophyCancel(currentState)
    is Actions.DeleteTrophyConfirm -> handleDeleteTrophyConfirm(currentState)
    is Actions.DeleteTrophyRequest -> handleDeleteTrophyRequest(currentState, operation)
    is Actions.FetchTrophies -> currentState
    is Events.FetchTrophiesObserved -> handleFetchTrophiesObserved(currentState, operation)
  }

  private fun handleDeleteTrophyCancel(
    currentState: TrophiesState,
  ): TrophiesState = currentState.copy(
    deleteConfirmationDialog = DialogState.Hidden,
  )

  private fun handleDeleteTrophyConfirm(
    currentState: TrophiesState,
  ): TrophiesState = currentState.copy(
    deleteConfirmationDialog = DialogState.Hidden,
  )

  private fun handleDeleteTrophyRequest(
    currentState: TrophiesState,
    action: Actions.DeleteTrophyRequest,
  ): TrophiesState = currentState.copy(
    deleteConfirmationDialog = DialogState.Shown(action.trophy),
  )

  private fun handleFetchTrophiesObserved(
    currentState: TrophiesState,
    event: Events.FetchTrophiesObserved,
  ): TrophiesState = currentState.copy(
    trophies = event.payload,
  )
}