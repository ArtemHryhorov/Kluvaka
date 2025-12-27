package co.kluvaka.cmp.features.trophies.data.usecase

import co.kluvaka.cmp.features.trophies.domain.model.TrophyInput
import co.kluvaka.cmp.features.trophies.domain.repository.TrophyRepository
import co.kluvaka.cmp.features.trophies.domain.usecase.UpdateTrophy
import kotlinx.datetime.Clock

class UpdateTrophyUseCase(
  private val repository: TrophyRepository,
) : UpdateTrophy {

  override suspend fun invoke(
    id: Int,
    input: TrophyInput,
  ) = repository.update(
    id = id,
    fishType = input.fishType,
    weight = input.weight.toDoubleOrNull(),
    length = input.length.toDoubleOrNull(),
    location = input.location.takeIf { it.isNotEmpty() },
    date = input.date ?: Clock.System.now().toEpochMilliseconds(),
    images = input.images,
    notes = input.notes.takeIf { it.isNotEmpty() },
  )
}