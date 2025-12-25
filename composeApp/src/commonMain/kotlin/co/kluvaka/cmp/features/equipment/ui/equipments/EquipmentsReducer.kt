package co.kluvaka.cmp.features.equipment.ui.equipments

import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsOperation.Actions
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsOperation.Events

class EquipmentsReducer {
  fun updateState(
    currentState: EquipmentsState,
    operation: EquipmentsOperation,
  ): EquipmentsState = when (operation) {
    is Actions.DeleteEquipmentCancel -> handleDeleteEquipmentCancel(currentState)
    is Actions.DeleteEquipmentConfirm -> handleDeleteEquipmentConfirm(currentState)
    is Actions.DeleteEquipmentRequest -> handleDeleteEquipmentRequest(currentState, operation)
    is Actions.FetchEquipments -> currentState
    is Events.FetchEquipmentsObserved -> handleFetchEquipmentsObserved(currentState, operation)
  }

  private fun handleDeleteEquipmentCancel(
    currentState: EquipmentsState,
  ): EquipmentsState = currentState.copy(
    deleteConfirmationDialog = DialogState.Hidden,
  )

  private fun handleDeleteEquipmentConfirm(
    currentState: EquipmentsState,
  ): EquipmentsState = currentState.copy(
    deleteConfirmationDialog = DialogState.Hidden,
  )

  private fun handleDeleteEquipmentRequest(
    currentState: EquipmentsState,
    action: Actions.DeleteEquipmentRequest,
  ): EquipmentsState = currentState.copy(
    deleteConfirmationDialog = DialogState.Shown(action.equipment),
  )

  private fun handleFetchEquipmentsObserved(
    currentState: EquipmentsState,
    event: Events.FetchEquipmentsObserved,
  ): EquipmentsState = currentState.copy(
    equipments = event.payload,
  )
}