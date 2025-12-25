package co.kluvaka.cmp.features.equipment.data.usecase

import co.kluvaka.cmp.features.equipment.domain.model.AddEquipmentInput
import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.usecase.AddEquipment

class AddEquipmentUseCase(
  private val repository: EquipmentRepository,
) : AddEquipment {

  override suspend fun invoke(input: AddEquipmentInput) =
    repository.insert(
      title = input.title,
      images = input.images,
      price = input.price.toDoubleOrNull() ?: 0.0
    )
}