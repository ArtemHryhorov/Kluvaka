package co.kluvaka.cmp.features.equipment.domain

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

interface EquipmentRepository {

  suspend fun insert(equipment: Equipment)

  suspend fun getAll(): List<Equipment>

  suspend fun delete(id: Int)
}