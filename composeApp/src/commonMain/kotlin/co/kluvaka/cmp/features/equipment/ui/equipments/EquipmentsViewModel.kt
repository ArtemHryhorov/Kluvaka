package co.kluvaka.cmp.features.equipment.ui.equipments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.equipment.domain.model.Equipment
import co.kluvaka.cmp.features.equipment.domain.usecase.DeleteEquipment
import co.kluvaka.cmp.features.equipment.domain.usecase.GetAllEquipments
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EquipmentsViewModel(
  private val getAllEquipments: GetAllEquipments,
  private val deleteEquipment: DeleteEquipment,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(EquipmentsState())
  val state: StateFlow<EquipmentsState> = _mutableState

  fun fetchEquipments() {
    viewModelScope.launch {
      _mutableState.update {
        it.copy(equipments = getAllEquipments())
      }
    }
  }

  fun delete(id: Int) {
    viewModelScope.launch {
      deleteEquipment(id)
      fetchEquipments()
    }
  }

  fun showDeleteConfirmationDialog(equipment: Equipment) {
    _mutableState.update {
      it.copy(deleteConfirmationDialog = DialogState.Shown(equipment))
    }
  }

  fun hideDeleteConfirmationDialog() {
    _mutableState.update {
      it.copy(deleteConfirmationDialog = DialogState.Hidden)
    }
  }
}