package co.kluvaka.cmp.features.trophies.data.usecase

import co.kluvaka.cmp.features.trophies.domain.model.TrophyInput
import co.kluvaka.cmp.features.trophies.domain.repository.TrophyRepository
import co.kluvaka.cmp.features.trophies.domain.usecase.AddTrophy
import kotlinx.datetime.Clock

class AddTrophyUseCase(
  private val repository: TrophyRepository,
) : AddTrophy {

  override suspend fun invoke(input: TrophyInput) = repository.insert(
    fishType = input.fishType,
    weight = input.weight.toDoubleOrNull(),
    length = input.length.toDoubleOrNull(),
    location = input.location.takeIf { it.isNotEmpty() },
    date = input.date ?: Clock.System.now().toEpochMilliseconds(),
    images = input.images,
    notes = input.notes.takeIf { it.isNotEmpty() },
  )
}
