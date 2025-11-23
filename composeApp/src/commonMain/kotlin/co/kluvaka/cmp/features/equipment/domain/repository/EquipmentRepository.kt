package co.kluvaka.cmp.features.equipment.domain.repository

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

interface EquipmentRepository {

  suspend fun insert(
    title: String,
    images: List<String>,
    price: Double,
  )

  suspend fun update(
    id: Int,
    title: String,
    images: List<String>,
    price: Double,
  )

  suspend fun getAll(): List<Equipment>

  suspend fun getById(id: Int): Equipment?

  suspend fun delete(id: Int)
}