package co.kluvaka.cmp.features.equipment.data.repository

import co.kluvaka.cmp.database.EquipmentDatabase
import co.kluvaka.cmp.features.equipment.domain.model.Equipment
import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository

class EquipmentRepositoryImpl(
  private val database: EquipmentDatabase,
) : EquipmentRepository {

  override suspend fun insert(
    title: String,
    images: List<String>,
    price: Double,
  ) {
    database.insertEquipment(title, images, price)
  }

  override suspend fun update(
    id: Int,
    title: String,
    images: List<String>,
    price: Double
  ) {
    database.updateEquipment(id, title, images, price)
  }

  override suspend fun getAll(): List<Equipment> {
    return database.getAllEquipment()
  }

  override suspend fun getById(id: Int): Equipment? {
    return database.getEquipment(id)
  }

  override suspend fun delete(id: Int) {
    return database.deleteEquipment(id)
  }
}