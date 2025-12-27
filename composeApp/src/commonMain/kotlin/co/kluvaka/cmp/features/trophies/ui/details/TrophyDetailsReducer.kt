package co.kluvaka.cmp.features.trophies.ui.details

import co.kluvaka.cmp.features.trophies.ui.details.TrophyDetailsOperation.Action
import co.kluvaka.cmp.features.trophies.ui.details.TrophyDetailsOperation.Event

class TrophyDetailsReducer {
  fun updateState(
    currentState: TrophyDetailsState,
    operation: TrophyDetailsOperation,
  ): TrophyDetailsState = when (operation) {
    is Action.GetTrophy -> currentState
    is Event.TrophyObserved -> handleTrophyObserved(currentState, operation)
  }

  private fun handleTrophyObserved(
    currentState: TrophyDetailsState,
    event: Event.TrophyObserved,
  ): TrophyDetailsState = currentState.copy(
    trophy = event.trophy,
  )
}
