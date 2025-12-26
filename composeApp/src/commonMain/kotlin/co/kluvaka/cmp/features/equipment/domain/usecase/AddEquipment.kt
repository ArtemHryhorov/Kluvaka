package co.kluvaka.cmp.features.equipment.domain.usecase

import co.kluvaka.cmp.features.equipment.domain.model.AddEquipmentInput

fun interface AddEquipment {
  suspend operator fun invoke(input: AddEquipmentInput)
}