package co.kluvaka.cmp.features.equipment.ui.add.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.equipment.domain.usecase.AddEquipment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEquipmentViewModel(
  private val addEquipment: AddEquipment,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(AddEquipmentState())
  val state: StateFlow<AddEquipmentState> = _mutableState

  fun updateTitle(title: String) {
    _mutableState.update { it.copy(title = title) }
  }

  fun updatePrice(price: String) {
    _mutableState.update { it.copy(price = price) }
  }

  fun addImage(image: String?) {
    if (image == null) return
    _mutableState.update { state ->
      state.copy(images = state.images + image)
    }
  }

  fun removeImage(index: Int) {
    _mutableState.update { state ->
      val newImages = state.images.toMutableList().apply {
        removeAt(index)
      }
      state.copy(images = newImages)
    }
  }

  fun addEquipment() {
    viewModelScope.launch {
      val state = _mutableState.value
      addEquipment(
        title = state.title,
        images = state.images,
        price = state.price.toDoubleOrNull() ?: 0.0
      )
    }
  }
}