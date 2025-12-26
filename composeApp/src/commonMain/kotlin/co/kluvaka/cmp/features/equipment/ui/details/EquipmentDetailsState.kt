package co.kluvaka.cmp.features.equipment.ui.details

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

data class EquipmentDetailsState(
  val equipment: Equipment?,
) {
  companion object {
    val Initial = EquipmentDetailsState(
      equipment = null,
    )
  }
}