package co.kluvaka.cmp.equipment.domain.usecase

import co.kluvaka.cmp.equipment.domain.EquipmentRepository
import co.kluvaka.cmp.equipment.domain.model.Equipment

class GetAllEquipmentsImpl(
  private val repository: EquipmentRepository,
) : GetAllEquipments {

  override suspend fun invoke(): List<Equipment> = repository.getAll()
}

fun interface GetAllEquipments {
  suspend operator fun invoke(): List<Equipment>
}