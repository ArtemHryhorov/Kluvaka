package co.kluvaka.cmp.features.trophies.domain.repository

import co.kluvaka.cmp.features.trophies.domain.model.Trophy

interface TrophyRepository {

  suspend fun insert(
    fishType: String,
    weight: Double,
    length: Double?,
    location: String,
    date: String,
    image: String?,
    notes: String?,
  )

  suspend fun update(
    id: Long,
    fishType: String,
    weight: Double,
    length: Double?,
    location: String,
    date: String,
    image: String?,
    notes: String?,
  )

  suspend fun getAll(): List<Trophy>

  suspend fun getById(id: Int): Trophy?

  suspend fun delete(id: Int)
}
