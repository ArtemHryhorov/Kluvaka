package co.kluvaka.cmp.features.equipment.ui.equipments

import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.equipment.domain.model.Equipment

data class EquipmentsState(
  val equipments: List<Equipment>,
  val deleteConfirmationDialog: DialogState<Equipment>,
  val totalPrice: Double,
) {
  companion object {
    val Initial = EquipmentsState(
      equipments = emptyList(),
      deleteConfirmationDialog = DialogState.Hidden,
      totalPrice = 0.0,
    )
  }
}
