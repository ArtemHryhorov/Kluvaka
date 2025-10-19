package co.kluvaka.cmp.features.trophies.ui.trophies

import co.kluvaka.cmp.features.trophies.domain.model.Trophy

data class TrophiesState(
  val trophies: List<Trophy> = emptyList(),
)
