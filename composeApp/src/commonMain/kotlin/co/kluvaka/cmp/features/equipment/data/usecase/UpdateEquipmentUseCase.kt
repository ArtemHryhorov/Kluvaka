package co.kluvaka.cmp.features.equipment.data.usecase

import co.kluvaka.cmp.features.equipment.domain.model.AddEquipmentInput
import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.usecase.UpdateEquipment

class UpdateEquipmentUseCase(
  private val repository: EquipmentRepository,
) : UpdateEquipment {

  override suspend fun invoke(
    id: Int,
    input: AddEquipmentInput,
  ) = repository.update(
    id = id,
    title = input.title,
    images = input.images,
    price = input.price.toDoubleOrNull() ?: 0.0
  )
}