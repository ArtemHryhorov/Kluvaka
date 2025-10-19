package co.kluvaka.cmp.features.trophies.domain.usecase

import co.kluvaka.cmp.features.trophies.domain.model.Trophy

fun interface GetAllTrophies {
  suspend operator fun invoke(): List<Trophy>
}
