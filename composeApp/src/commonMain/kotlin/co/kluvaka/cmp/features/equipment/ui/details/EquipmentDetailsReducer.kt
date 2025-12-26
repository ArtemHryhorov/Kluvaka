package co.kluvaka.cmp.features.equipment.ui.details

import co.kluvaka.cmp.features.equipment.ui.details.EquipmentDetailsOperation.Action
import co.kluvaka.cmp.features.equipment.ui.details.EquipmentDetailsOperation.Event

class EquipmentDetailsReducer {
  fun updateState(
    currentState: EquipmentDetailsState,
    operation: EquipmentDetailsOperation,
  ): EquipmentDetailsState = when (operation) {
    is Action.GetEquipment -> currentState
    is Event.EquipmentObserved -> handleEquipmentObserved(currentState, operation)
  }

  private fun handleEquipmentObserved(
    currentState: EquipmentDetailsState,
    event: Event.EquipmentObserved,
  ): EquipmentDetailsState = currentState.copy(
    equipment = event.equipment,
  )
}