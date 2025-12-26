package co.kluvaka.cmp.features.equipment.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.equipment.domain.usecase.GetEquipmentById
import co.kluvaka.cmp.features.equipment.ui.details.EquipmentDetailsOperation.Action
import co.kluvaka.cmp.features.equipment.ui.details.EquipmentDetailsOperation.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EquipmentDetailsViewModel(
  private val getEquipmentById: GetEquipmentById,
  private val reducer: EquipmentDetailsReducer,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(EquipmentDetailsState.Initial)
  val state: StateFlow<EquipmentDetailsState> = _mutableState

  fun handleAction(action: Action) {
    when (action) {
      is Action.GetEquipment -> handleGetEquipment(action.id)
    }
  }

  private fun dispatchOperation(operation: EquipmentDetailsOperation) {
    viewModelScope.launch {
      _mutableState.update { currentState ->
        reducer.updateState(currentState, operation)
      }
    }
  }

  private fun handleGetEquipment(equipmentId: Int) {
    viewModelScope.launch {
      val equipment = getEquipmentById(equipmentId) ?: return@launch
      val event = Event.EquipmentObserved(equipment)
      dispatchOperation(event)
    }
  }
}
