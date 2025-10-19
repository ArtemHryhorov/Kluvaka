package co.kluvaka.cmp.features.trophies.domain.usecase

import co.kluvaka.cmp.features.trophies.domain.model.Trophy

fun interface GetTrophyById {
  suspend operator fun invoke(id: Int): Trophy?
}
