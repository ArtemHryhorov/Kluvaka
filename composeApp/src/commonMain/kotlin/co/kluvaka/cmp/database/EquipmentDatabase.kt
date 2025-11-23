package co.kluvaka.cmp.database

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

class EquipmentDatabase(databaseDriverFactory: DatabaseDriverFactory) {
  private val database = AppDatabase(databaseDriverFactory.createDriver())
  private val dbQuery = database.equipmentQueries

  fun getEquipment(id: Int): Equipment? = dbQuery
    .getEquipmentById(id.toLong())
    .executeAsOneOrNull()
    ?.let(::mapEquipment)

  fun getAllEquipment(): List<Equipment> = dbQuery
    .selectAllEquipment(::mapEquipment)
    .executeAsList()

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

  private fun mapEquipment(
    equipment: co.kluvaka.cmp.Equipment,
  ) = Equipment(
    id = equipment.id.toInt(),
    title = equipment.title,
    image = equipment.image,
    price = equipment.price,
  )
}