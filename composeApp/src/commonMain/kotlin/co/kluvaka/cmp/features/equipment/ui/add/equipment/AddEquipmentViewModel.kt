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

  fun updateImage(image: String?) {
    _mutableState.update { it.copy(image = image) }
  }

  fun addEquipment(
    title: String,
    price: Double,
  ) {
    viewModelScope.launch {
      addEquipment(title, _mutableState.value.image, price)
    }
  }
}