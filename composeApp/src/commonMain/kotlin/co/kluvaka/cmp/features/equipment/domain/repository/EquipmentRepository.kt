package co.kluvaka.cmp.features.equipment.domain.repository

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

interface EquipmentRepository {

  suspend fun insert(
    title: String,
    image: String?,
    price: Double,
  )

  suspend fun getAll(): List<Equipment>

  suspend fun delete(id: Int)
}