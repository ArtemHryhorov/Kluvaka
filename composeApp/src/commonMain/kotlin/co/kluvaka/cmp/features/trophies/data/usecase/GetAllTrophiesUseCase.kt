package co.kluvaka.cmp.features.trophies.data.usecase

import co.kluvaka.cmp.features.trophies.domain.repository.TrophyRepository
import co.kluvaka.cmp.features.trophies.domain.usecase.GetAllTrophies

class GetAllTrophiesUseCase(
  private val repository: TrophyRepository,
) : GetAllTrophies {
  override suspend fun invoke(): List<co.kluvaka.cmp.features.trophies.domain.model.Trophy> {
    return repository.getAll()
  }
}
