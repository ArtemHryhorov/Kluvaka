package co.kluvaka.cmp.equipments.data

import co.kluvaka.cmp.database.Database
import co.kluvaka.cmp.database.DatabaseDriverFactory
import co.kluvaka.cmp.equipments.domain.Equipment
import co.kluvaka.cmp.equipments.domain.EquipmentRepository

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
}