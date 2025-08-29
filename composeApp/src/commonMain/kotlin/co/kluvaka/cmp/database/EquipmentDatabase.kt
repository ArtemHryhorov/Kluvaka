package co.kluvaka.cmp.database

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

class EquipmentDatabase(databaseDriverFactory: DatabaseDriverFactory) {
  private val database = AppDatabase(databaseDriverFactory.createDriver())
  private val dbQuery = database.equipmentQueries

  fun getAllEquipment(): List<Equipment> = dbQuery
    .selectAllEquipment(::mapEquipment)
    .executeAsList()

  private fun mapEquipment(
    id: Long,
    title: String,
    image: String?,
    price: Double,
  ) = Equipment(
    id = id.toInt(),
    title = title,
    image = image,
    price = price,
  )

  fun insertEquipment(
    title: String,
    image: String?,
    price: Double,
  ) {
    dbQuery.transaction {
      dbQuery.insertEquipment(
        id = null,
        title = title,
        image = image,
        price = price,
      )
    }
  }

  fun deleteEquipment(id: Int) {
    dbQuery.transaction {
      dbQuery.deleteEquipment(id.toLong())
    }
  }
}