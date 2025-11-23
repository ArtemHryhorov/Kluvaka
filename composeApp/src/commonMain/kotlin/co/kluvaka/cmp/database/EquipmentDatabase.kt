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
    images: List<String>,
    price: Double,
  ) {
    dbQuery.transaction {
      dbQuery.insertEquipment(
        id = null,
        title = title,
        images = images.joinToString("|"),
        price = price,
      )
    }
  }

  fun updateEquipment(
    id: Int,
    title: String,
    images: List<String>,
    price: Double,
  ) {
    dbQuery.transaction {
      dbQuery.updateEquipment(
        id = id.toLong(),
        title = title,
        images = images.joinToString("|"),
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
    images: String?,
    price: Double,
  ) = Equipment(
    id = id.toInt(),
    title = title,
    images = images?.split("|")?.filter { it.isNotEmpty() } ?: emptyList(),
    price = price,
  )

  private fun mapEquipment(
    equipment: co.kluvaka.cmp.Equipment,
  ) = Equipment(
    id = equipment.id.toInt(),
    title = equipment.title,
    images = equipment.images?.split("|")?.filter { it.isNotEmpty() } ?: emptyList(),
    price = equipment.price,
  )
}