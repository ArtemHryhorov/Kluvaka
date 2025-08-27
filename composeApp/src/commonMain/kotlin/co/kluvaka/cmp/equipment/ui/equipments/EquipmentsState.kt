package co.kluvaka.cmp.equipment.ui.equipments

import co.kluvaka.cmp.equipment.domain.model.Equipment

data class EquipmentsState(
  val equipments: List<Equipment> = emptyList(),
) {
  val totalPrice: Double = equipments.sumOf { it.price }
}
