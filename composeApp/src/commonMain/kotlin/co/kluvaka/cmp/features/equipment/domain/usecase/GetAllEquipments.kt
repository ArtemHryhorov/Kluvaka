package co.kluvaka.cmp.features.equipment.domain.usecase

import co.kluvaka.cmp.features.equipment.domain.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.model.Equipment

class GetAllEquipmentsImpl(
  private val repository: EquipmentRepository,
) : GetAllEquipments {

  override suspend fun invoke(): List<Equipment> = repository.getAll()
}

fun interface GetAllEquipments {
  suspend operator fun invoke(): List<Equipment>
}