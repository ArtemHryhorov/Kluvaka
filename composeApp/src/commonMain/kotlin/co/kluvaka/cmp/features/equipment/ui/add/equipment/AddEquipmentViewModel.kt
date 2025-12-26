package co.kluvaka.cmp.features.equipment.ui.add.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.equipment.domain.usecase.AddEquipment
import co.kluvaka.cmp.features.equipment.domain.usecase.UpdateEquipment
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentOperation.Actions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEquipmentViewModel(
  private val addEquipment: AddEquipment,
  private val reducer: AddEquipmentReducer,
  private val updateEquipment: UpdateEquipment,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(AddEquipmentState.Initial)
  val state: StateFlow<AddEquipmentState> = _mutableState

  fun handleAction(action: Actions) {
    when (action) {
      is Actions.Save -> handleSave()
      else -> dispatchOperation(action)
    }
  }

  private fun dispatchOperation(operation: AddEquipmentOperation) {
    _mutableState.update { currentState ->
      reducer.updateState(operation, currentState)
    }
  }

  private fun handleSave() {
    when (val mode = state.value.mode) {
      is AddEquipmentMode.Edit -> viewModelScope.launch {
        updateEquipment(
          id = mode.equipment.id,
          input = _mutableState.value.input,
        )
      }

      AddEquipmentMode.New -> viewModelScope.launch {
        addEquipment(
          input = _mutableState.value.input,
        )
      }
    }
  }
}