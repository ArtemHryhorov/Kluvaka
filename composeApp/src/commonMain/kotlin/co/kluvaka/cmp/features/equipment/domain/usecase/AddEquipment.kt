package co.kluvaka.cmp.features.equipment.domain.usecase

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

fun interface AddEquipment {
  suspend operator fun invoke(equipment: Equipment)
}