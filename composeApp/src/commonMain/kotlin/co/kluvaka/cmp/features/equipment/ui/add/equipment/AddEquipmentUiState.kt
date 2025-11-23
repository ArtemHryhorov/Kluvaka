package co.kluvaka.cmp.features.equipment.ui.add.equipment

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

data class AddEquipmentUiState(
  val input: AddEquipmentInput = AddEquipmentInput(),
  val mode: AddEquipmentMode = AddEquipmentMode.New,
)

data class AddEquipmentInput(
  val title: String = "",
  val price: String = "",
  val images: List<String> = emptyList(),
)

sealed interface AddEquipmentMode {
  object New : AddEquipmentMode
  data class Edit(val equipment: Equipment) : AddEquipmentMode
}