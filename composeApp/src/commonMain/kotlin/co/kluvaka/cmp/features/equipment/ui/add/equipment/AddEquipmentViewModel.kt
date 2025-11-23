package co.kluvaka.cmp.features.equipment.ui.add.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.equipment.domain.usecase.AddEquipment
import co.kluvaka.cmp.features.equipment.domain.usecase.UpdateEquipment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEquipmentViewModel(
  private val addEquipment: AddEquipment,
  private val updateEquipment: UpdateEquipment,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(AddEquipmentUiState())
  val state: StateFlow<AddEquipmentUiState> = _mutableState

  fun setAddEquipmentMode(mode: AddEquipmentMode) {
    _mutableState.update { state ->
      state.copy(mode = mode)
    }

    if (mode is AddEquipmentMode.Edit) {
      _mutableState.update { state ->
        state.copy(
          input = state.input.copy(
            images = mode.equipment.images,
            title = mode.equipment.title,
            price = mode.equipment.price.toString(),
          ),
        )
      }
    }
  }

  fun updateTitle(title: String) {
    _mutableState.update { state ->
      state.copy(
        input = state.input.copy(
          title = title,
        ),
      )
    }
  }

  fun updatePrice(price: String) {
    _mutableState.update { state ->
      state.copy(
        input = state.input.copy(
          price = price,
        ),
      )
    }
  }

  fun addImage(image: String?) {
    if (image == null) return
    _mutableState.update { state ->
      state.copy(
        input = state.input.copy(
          images = state.input.images + image,
        ),
      )
    }
  }

  fun removeImage(index: Int) {
    val state = _mutableState.value
    val newImages = state.input.images.toMutableList().apply {
      removeAt(index)
    }
    _mutableState.update { state ->
      state.copy(
        input = state.input.copy(
          images = newImages,
        ),
      )
    }
  }

  fun save() {
    when (val mode = state.value.mode) {
      is AddEquipmentMode.Edit -> updateEquipment(mode.equipment.id)
      AddEquipmentMode.New -> addEquipment()
    }
  }

  private fun updateEquipment(id: Int) {
    viewModelScope.launch {
      val state = _mutableState.value
      updateEquipment(
        id = id,
        title = state.input.title,
        images = state.input.images,
        price = state.input.price.toDoubleOrNull() ?: 0.0
      )
    }
  }

  private fun addEquipment() {
    viewModelScope.launch {
      val state = _mutableState.value
      addEquipment(
        title = state.input.title,
        images = state.input.images,
        price = state.input.price.toDoubleOrNull() ?: 0.0
      )
    }
  }
}