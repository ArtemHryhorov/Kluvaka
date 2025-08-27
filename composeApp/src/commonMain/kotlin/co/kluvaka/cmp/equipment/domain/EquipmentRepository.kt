package co.kluvaka.cmp.equipment.domain

import co.kluvaka.cmp.equipment.domain.model.Equipment

interface EquipmentRepository {

  suspend fun insert(equipment: Equipment)

  suspend fun getAll(): List<Equipment>

  suspend fun delete(id: Int)
}