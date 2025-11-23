package co.kluvaka.cmp.features.equipment.data.usecase

import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.usecase.UpdateEquipment

class UpdateEquipmentUseCase(
  private val repository: EquipmentRepository,
) : UpdateEquipment {
  override suspend fun invoke(
    id: Int,
    title: String,
    images: List<String>,
    price: Double
  ) = repository.update(
    id = id,
    title = title,
    images = images,
    price = price,
  )
}