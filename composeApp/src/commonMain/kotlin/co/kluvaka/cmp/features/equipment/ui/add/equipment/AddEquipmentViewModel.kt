package co.kluvaka.cmp.features.equipment.ui.add.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.equipment.domain.usecase.AddEquipment
import kotlinx.coroutines.launch

class AddEquipmentViewModel(
  private val addEquipment: AddEquipment,
) : ViewModel() {

  fun addEquipment(
    title: String,
    price: Double,
  ) {
    viewModelScope.launch {
      addEquipment(title, null, price)
    }
  }
}