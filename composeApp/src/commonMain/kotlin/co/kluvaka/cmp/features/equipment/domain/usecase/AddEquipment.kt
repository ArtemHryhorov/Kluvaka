package co.kluvaka.cmp.features.equipment.domain.usecase

import co.kluvaka.cmp.features.equipment.domain.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.model.Equipment

class AddEquipmentImpl(
  private val repository: EquipmentRepository,
) : AddEquipment {

  override suspend fun invoke(equipment: Equipment) = repository.insert(equipment)
}

fun interface AddEquipment {
  suspend operator fun invoke(equipment: Equipment)
}