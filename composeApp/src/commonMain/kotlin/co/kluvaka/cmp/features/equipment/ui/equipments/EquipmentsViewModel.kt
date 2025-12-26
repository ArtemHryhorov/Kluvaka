package co.kluvaka.cmp.features.equipment.ui.equipments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.equipment.domain.usecase.DeleteEquipment
import co.kluvaka.cmp.features.equipment.domain.usecase.GetAllEquipments
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsOperation.Actions
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsOperation.Events.FetchEquipmentsObserved
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EquipmentsViewModel(
  private val getAllEquipments: GetAllEquipments,
  private val deleteEquipment: DeleteEquipment,
  private val reducer: EquipmentsReducer,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(EquipmentsState.Initial)
  val state: StateFlow<EquipmentsState> = _mutableState

  fun handleAction(action: Actions) {
    when (action) {
      is Actions.FetchEquipments -> handleFetchEquipments()
      is Actions.DeleteEquipmentCancel -> dispatchOperation(action)
      is Actions.DeleteEquipmentConfirm -> handleDeleteEquipment(action)
      is Actions.DeleteEquipmentRequest -> dispatchOperation(action)
    }
  }

  private fun dispatchOperation(operation: EquipmentsOperation) {
    _mutableState.update { currentState ->
      reducer.updateState(currentState, operation)
    }
  }

  private fun handleFetchEquipments() {
    viewModelScope.launch {
      dispatchOperation(
        operation = FetchEquipmentsObserved(
          payload = getAllEquipments(),
        )
      )
    }
  }

  private fun handleDeleteEquipment(action: Actions.DeleteEquipmentConfirm) {
    viewModelScope.launch {
      deleteEquipment(action.id)
      handleAction(Actions.FetchEquipments)
    }
  }
}
