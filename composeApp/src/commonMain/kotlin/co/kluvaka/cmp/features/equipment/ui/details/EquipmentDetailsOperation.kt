package co.kluvaka.cmp.features.equipment.ui.details

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

sealed interface EquipmentDetailsOperation {

  sealed interface Action : EquipmentDetailsOperation {
    data class GetEquipment(val id: Int) : Action
  }

  sealed interface Event : EquipmentDetailsOperation {
    data class EquipmentObserved(val equipment: Equipment) : Event
  }
}