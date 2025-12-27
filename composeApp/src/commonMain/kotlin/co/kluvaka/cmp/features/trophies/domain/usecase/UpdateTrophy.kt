package co.kluvaka.cmp.features.trophies.domain.usecase

import co.kluvaka.cmp.features.trophies.domain.model.TrophyInput

fun interface UpdateTrophy {
  suspend operator fun invoke(
    id: Int,
    input: TrophyInput,
  )
}