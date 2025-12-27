package co.kluvaka.cmp.features.trophies.domain.usecase

import co.kluvaka.cmp.features.trophies.domain.model.TrophyInput

fun interface AddTrophy {
  suspend operator fun invoke(input: TrophyInput)
}
