package co.kluvaka.cmp.equipment.ui.equipments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.equipment.domain.usecase.GetAllEquipments
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EquipmentsViewModel(
  private val getAllEquipments: GetAllEquipments,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(EquipmentsState())
  val state: StateFlow<EquipmentsState> = _mutableState

  init {
    fetchEquipments()
  }

  private fun fetchEquipments() {
    viewModelScope.launch {
      _mutableState.update {
        it.copy(equipments = getAllEquipments())
      }
    }
  }
}