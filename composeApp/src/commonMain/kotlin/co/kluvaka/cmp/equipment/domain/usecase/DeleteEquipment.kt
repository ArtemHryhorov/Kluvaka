package co.kluvaka.cmp.equipment.domain.usecase

import co.kluvaka.cmp.equipment.domain.EquipmentRepository

class DeleteEquipmentImpl(
  private val repository: EquipmentRepository,
) : DeleteEquipment {

  override suspend fun invoke(id: Int) = repository.delete(id)
}

fun interface DeleteEquipment {
  suspend operator fun invoke(id: Int)
}