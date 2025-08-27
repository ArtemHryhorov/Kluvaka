package co.kluvaka.cmp.equipment.data

import co.kluvaka.cmp.database.Database
import co.kluvaka.cmp.database.DatabaseDriverFactory
import co.kluvaka.cmp.equipment.domain.model.Equipment
import co.kluvaka.cmp.equipment.domain.EquipmentRepository

class EquipmentRepositoryImpl(
  databaseDriverFactory: DatabaseDriverFactory,
) : EquipmentRepository {
  private val database = Database(databaseDriverFactory)

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