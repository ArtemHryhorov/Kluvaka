package co.kluvaka.cmp.features.equipment.domain.usecase

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

fun interface GetEquipmentById {
  suspend operator fun invoke(id: Int): Equipment?
}