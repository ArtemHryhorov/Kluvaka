package co.kluvaka.cmp.features.equipment.data.usecase

import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.usecase.DeleteEquipment

class DeleteEquipmentUseCase(
  private val repository: EquipmentRepository,
) : DeleteEquipment {

  override suspend fun invoke(id: Int) = repository.delete(id)
}