package co.kluvaka.cmp.features.trophies.data.usecase

import co.kluvaka.cmp.features.trophies.domain.repository.TrophyRepository
import co.kluvaka.cmp.features.trophies.domain.usecase.DeleteTrophy

class DeleteTrophyUseCase(
  private val repository: TrophyRepository,
) : DeleteTrophy {
  override suspend fun invoke(id: Int) {
    repository.delete(id)
  }
}
