package co.kluvaka.cmp.features.equipment.ui.equipments

import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.equipment.domain.model.Equipment

data class EquipmentsState(
  val equipments: List<Equipment> = emptyList(),
  val deleteConfirmationDialog: DialogState<Equipment> = DialogState.Hidden,
) {
  val totalPrice: Double = equipments.sumOf { it.price }
}
