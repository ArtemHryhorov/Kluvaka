package co.kluvaka.cmp.features.equipment.data.usecase

import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.usecase.AddEquipment

class AddEquipmentUseCase(
  private val repository: EquipmentRepository,
) : AddEquipment {

  override suspend fun invoke(
    title: String,
    image: String?,
    price: Double,
  ) = repository.insert(title, image, price)
}