package co.kluvaka.cmp.features.trophies.data.repository

import co.kluvaka.cmp.database.TrophyDatabase
import co.kluvaka.cmp.features.trophies.domain.model.Trophy
import co.kluvaka.cmp.features.trophies.domain.repository.TrophyRepository

class TrophyRepositoryImpl(
  private val database: TrophyDatabase,
) : TrophyRepository {

  override suspend fun insert(
    fishType: String,
    weight: Double,
    length: Double?,
    location: String,
    date: String,
    image: String?,
    notes: String?,
  ) = database.insertTrophy(
    fishType = fishType,
    weight = weight,
    length = length,
    location = location,
    date = date,
    image = image,
    notes = notes
  )

  override suspend fun getAll(): List<Trophy> {
    return database.getAllTrophies()
  }

  override suspend fun getById(id: Int): Trophy? {
    return database.getTrophyById(id)
  }

  override suspend fun delete(id: Int) {
    return database.deleteTrophy(id)
  }
}
