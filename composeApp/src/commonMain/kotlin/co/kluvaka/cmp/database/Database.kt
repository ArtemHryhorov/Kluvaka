package co.kluvaka.cmp.database

import co.kluvaka.cmp.equipments.domain.Equipment

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
  private val database = AppDatabase(databaseDriverFactory.createDriver())
  private val dbQuery = database.appDatabaseQueries

  internal fun getAllEquipment(): List<Equipment> {
    return dbQuery.selectAllEquipment(::mapEquipment).executeAsList()
  }

  internal fun insertEquipment(equipment: Equipment) {
    dbQuery.transaction {
      dbQuery.insertEquipment(
        id = equipment.id.toLong(),
        title = equipment.title,
        image = equipment.image,
        price = equipment.price,
      )
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