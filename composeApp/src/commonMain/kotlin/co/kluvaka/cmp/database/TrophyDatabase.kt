package co.kluvaka.cmp.database

import co.kluvaka.cmp.features.trophies.domain.model.Trophy

class TrophyDatabase(databaseDriverFactory: DatabaseDriverFactory) {
  private val database = AppDatabase(databaseDriverFactory.createDriver())
  private val dbQuery = database.trophyQueries

  fun getAllTrophies(): List<Trophy> = dbQuery
    .selectAllTrophies(::mapTrophy)
    .executeAsList()

  private fun mapTrophy(
    id: Long,
    fishType: String,
    weight: Double,
    length: Double?,
    location: String,
    date: String,
    image: String?,
    notes: String?,
  ) = Trophy(
    id = id.toInt(),
    fishType = fishType,
    weight = weight,
    length = length,
    location = location,
    date = date,
    image = image,
    notes = notes,
  )

  fun insertTrophy(
    fishType: String,
    weight: Double,
    length: Double?,
    location: String,
    date: String,
    image: String?,
    notes: String?,
  ) {
    dbQuery.transaction {
      dbQuery.insertTrophy(
        fishType = fishType,
        weight = weight,
        length = length,
        location = location,
        date = date,
        image = image,
        notes = notes,
      )
    }
  }

  fun deleteTrophy(id: Int) {
    dbQuery.transaction {
      dbQuery.deleteTrophy(id.toLong())
    }
  }

  fun getTrophyById(id: Int): Trophy? {
    return dbQuery.getTrophyById(id.toLong())
      .executeAsOneOrNull()
      ?.let { trophyRow ->
        mapTrophy(
          id = trophyRow.id,
          fishType = trophyRow.fish_type,
          weight = trophyRow.weight,
          length = trophyRow.length,
          location = trophyRow.location,
          date = trophyRow.date,
          image = trophyRow.image,
          notes = trophyRow.notes,
        )
      }
  }
}
