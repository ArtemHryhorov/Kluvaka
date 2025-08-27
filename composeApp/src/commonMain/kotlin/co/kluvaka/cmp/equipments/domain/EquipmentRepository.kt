package co.kluvaka.cmp.equipments.domain

interface EquipmentRepository {

  suspend fun insert(equipment: Equipment)

  suspend fun getAll(): List<Equipment>
}