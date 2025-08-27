package co.kluvaka.cmp.equipment.ui.add.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.equipment.domain.model.Equipment
import co.kluvaka.cmp.equipment.domain.usecase.AddEquipment
import kotlinx.coroutines.launch
import kotlin.random.Random

class AddEquipmentViewModel(
  private val addEquipment: AddEquipment,
) : ViewModel() {

  fun addEquipment(
    name: String,
    price: Double
  ) {
    val equipment = Equipment(
      id = Random.nextInt(),
      title = name,
      image = null,
      price = price,
    )
    viewModelScope.launch {
      addEquipment(equipment)
    }
  }
}