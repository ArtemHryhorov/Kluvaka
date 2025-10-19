package co.kluvaka.cmp.features.trophies.data.usecase

import co.kluvaka.cmp.features.trophies.domain.repository.TrophyRepository
import co.kluvaka.cmp.features.trophies.domain.usecase.GetTrophyById

class GetTrophyByIdUseCase(
  private val repository: TrophyRepository,
) : GetTrophyById {
  override suspend fun invoke(id: Int): co.kluvaka.cmp.features.trophies.domain.model.Trophy? {
    return repository.getById(id)
  }
}
