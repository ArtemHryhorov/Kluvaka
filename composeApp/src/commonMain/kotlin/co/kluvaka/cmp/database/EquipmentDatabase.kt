package co.kluvaka.cmp.database

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

class EquipmentDatabase(databaseDriverFactory: DatabaseDriverFactory) {
  private val database = AppDatabase(databaseDriverFactory.createDriver())
  private val dbQuery = database.equipmentQueries

  fun getAllEquipment(): List<Equipment> {
    return dbQuery.selectAllEquipment(::mapEquipment).executeAsList()
  }

  fun insertEquipment(equipment: Equipment) {
    dbQuery.transaction {
      dbQuery.insertEquipment(
        id = equipment.id.toLong(),
        title = equipment.title,
        image = equipment.image,
        price = equipment.price,
      )
    }
  }

  fun deleteEquipment(id: Int) {
    dbQuery.transaction {
      dbQuery.deleteEquipment(id.toLong())
    }
  }

  private fun mapEquipment(
    id: Long,
    title: String,
    image: String?,
    price: Double,
  ): Equipment {
    return Equipment(
      id = id.toInt(),
      title = title,
      image = image,
      price = price,
    )
  }
}