package co.kluvaka.cmp.features.trophies.ui.details

import co.kluvaka.cmp.features.trophies.domain.model.Trophy

sealed interface TrophyDetailsOperation {

  sealed interface Action : TrophyDetailsOperation {
    data class GetTrophy(val id: Int) : Action
  }

  sealed interface Event : TrophyDetailsOperation {
    data class TrophyObserved(val trophy: Trophy) : Event
  }
}