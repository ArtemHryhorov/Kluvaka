package co.kluvaka.cmp.features.equipment.data.repository

import co.kluvaka.cmp.database.Database
import co.kluvaka.cmp.database.DatabaseDriverFactory
import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.model.Equipment

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