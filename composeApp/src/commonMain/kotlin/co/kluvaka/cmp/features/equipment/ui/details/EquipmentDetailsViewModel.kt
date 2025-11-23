package co.kluvaka.cmp.features.equipment.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.equipment.domain.model.Equipment
import co.kluvaka.cmp.features.equipment.domain.usecase.GetEquipmentById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EquipmentDetailsViewModel(
  private val getEquipmentById: GetEquipmentById,
) : ViewModel() {

  private val _mutableState = MutableStateFlow<Equipment?>(null)
  val equipment: StateFlow<Equipment?> = _mutableState

  fun loadEquipment(equipmentId: Int) {
    viewModelScope.launch {
      _mutableState.update { getEquipmentById(equipmentId) }
    }
  }
}
