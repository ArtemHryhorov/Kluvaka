package co.kluvaka.cmp.features.equipment.data.repository

import co.kluvaka.cmp.database.EquipmentDatabase
import co.kluvaka.cmp.features.equipment.domain.model.Equipment
import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository

class EquipmentRepositoryImpl(
  private val database: EquipmentDatabase,
) : EquipmentRepository {

  override suspend fun insert(equipment: Equipment) {
    database.insertEquipment(equipment)
  }

  override suspend fun getAll(): List<Equipment> {
    return database.getAllEquipment()
  }

  override suspend fun delete(id: Int) {
    return database.deleteEquipment(id)
  }
}