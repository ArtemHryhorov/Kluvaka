package co.kluvaka.cmp.features.equipment.domain.usecase

import co.kluvaka.cmp.features.equipment.domain.model.AddEquipmentInput

fun interface UpdateEquipment {
  suspend operator fun invoke(
    id: Int,
    input: AddEquipmentInput,
  )
}