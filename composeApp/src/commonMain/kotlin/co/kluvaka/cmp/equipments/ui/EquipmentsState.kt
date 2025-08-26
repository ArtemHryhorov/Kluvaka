package co.kluvaka.cmp.equipments.ui

import co.kluvaka.cmp.equipments.domain.Equipment

data class EquipmentsState(
  val equipments: List<Equipment> = emptyList(),
) {
  val totalPrice: Double = equipments.sumOf { it.price }
}
