package co.kluvaka.cmp.features.trophies.data.usecase

import co.kluvaka.cmp.features.trophies.domain.repository.TrophyRepository
import co.kluvaka.cmp.features.trophies.domain.usecase.AddTrophy

class AddTrophyUseCase(
  private val repository: TrophyRepository,
) : AddTrophy {
  override suspend fun invoke(
    fishType: String,
    weight: Double?,
    length: Double?,
    location: String?,
    date: Long,
    images: List<String>,
    notes: String?,
  ) = repository.insert(
    fishType = fishType,
    weight = weight,
    length = length,
    location = location,
    date = date,
    images = images,
    notes = notes
  )
}
