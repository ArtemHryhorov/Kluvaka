package co.kluvaka.cmp.features.equipment.ui.add.equipment

import co.kluvaka.cmp.features.equipment.domain.model.AddEquipmentInput
import co.kluvaka.cmp.features.equipment.domain.model.Equipment

data class AddEquipmentState(
  val input: AddEquipmentInput,
  val mode: AddEquipmentMode,
) {
  companion object {
    val Initial = AddEquipmentState(
      input = AddEquipmentInput.Initial,
      mode = AddEquipmentMode.New,
    )
  }
}

sealed interface AddEquipmentMode {
  object New : AddEquipmentMode
  data class Edit(val equipment: Equipment) : AddEquipmentMode
}