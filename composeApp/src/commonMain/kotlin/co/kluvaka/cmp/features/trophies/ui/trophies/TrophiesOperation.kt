package co.kluvaka.cmp.features.trophies.ui.trophies

import co.kluvaka.cmp.features.trophies.domain.model.Trophy

sealed interface TrophiesOperation {

  sealed interface Actions : TrophiesOperation {
    object DeleteTrophyCancel : Actions
    data class DeleteTrophyConfirm(val id: Int) : Actions
    data class DeleteTrophyRequest(val trophy: Trophy) : Actions
    object FetchTrophies : Actions
  }

  sealed interface Events : TrophiesOperation {
    data class FetchTrophiesObserved(val payload: List<Trophy>) : Events
  }
}