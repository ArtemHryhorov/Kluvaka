package co.kluvaka.cmp.features.equipment.data.usecase

import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.model.Equipment
import co.kluvaka.cmp.features.equipment.domain.usecase.AddEquipment

class AddEquipmentUseCase(
  private val repository: EquipmentRepository,
) : AddEquipment {

  override suspend fun invoke(equipment: Equipment) = repository.insert(equipment)
}