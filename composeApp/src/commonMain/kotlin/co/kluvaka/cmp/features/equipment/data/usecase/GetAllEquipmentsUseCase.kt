package co.kluvaka.cmp.features.equipment.data.usecase

import co.kluvaka.cmp.features.equipment.domain.model.Equipment
import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.usecase.GetAllEquipments

class GetAllEquipmentsUseCase(
  private val repository: EquipmentRepository,
) : GetAllEquipments {

  override suspend fun invoke(): List<Equipment> = repository.getAll()
}