package co.kluvaka.cmp.features.equipment.domain.usecase

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

fun interface GetAllEquipments {
  suspend operator fun invoke(): List<Equipment>
}