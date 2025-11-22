package co.kluvaka.cmp.features.equipment.data.usecase

import co.kluvaka.cmp.features.equipment.domain.model.Equipment
import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.usecase.GetEquipmentById

class GetEquipmentByIdUseCase(
  private val repository: EquipmentRepository,
) : GetEquipmentById {

  override suspend fun invoke(id: Int): Equipment ?= repository.getById(id)
}