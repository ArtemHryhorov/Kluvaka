package co.kluvaka.cmp.features.trophies.ui.details

import co.kluvaka.cmp.features.trophies.domain.model.Trophy

data class TrophyDetailsState(
  val trophy: Trophy?,
) {
  companion object {
    val Initial = TrophyDetailsState(
      trophy = null,
    )
  }
}
