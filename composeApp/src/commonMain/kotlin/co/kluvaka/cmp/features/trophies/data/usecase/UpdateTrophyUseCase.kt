package co.kluvaka.cmp.features.trophies.data.usecase

import co.kluvaka.cmp.features.trophies.domain.repository.TrophyRepository
import co.kluvaka.cmp.features.trophies.domain.usecase.UpdateTrophy

class UpdateTrophyUseCase(
  private val repository: TrophyRepository,
) : UpdateTrophy {
  override suspend fun invoke(
    id: Long,
    fishType: String,
    weight: Double,
    length: Double?,
    location: String,
    date: Long,
    images: List<String>,
    notes: String?
  ) = repository.update(
    id = id,
    fishType = fishType,
    weight = weight,
    length = length,
    location = location,
    date = date,
    images = images,
    notes = notes,
  )
}