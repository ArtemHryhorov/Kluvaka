package co.kluvaka.cmp.features.trophies.ui.add.trophy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.trophies.domain.usecase.AddTrophy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddTrophyViewModel(
  private val addTrophy: AddTrophy,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(AddTrophyState())
  val state: StateFlow<AddTrophyState> = _mutableState

  fun updateFishType(fishType: String) {
    _mutableState.update { it.copy(fishType = fishType) }
  }

  fun updateWeight(weight: String) {
    _mutableState.update { it.copy(weight = weight) }
  }

  fun updateLength(length: String) {
    _mutableState.update { it.copy(length = length) }
  }

  fun updateLocation(location: String) {
    _mutableState.update { it.copy(location = location) }
  }

  fun updateDate(date: String) {
    _mutableState.update { it.copy(date = date) }
  }

  fun updateImage(image: String?) {
    println("DEBUG: AddTrophyViewModel.updateImage called with: $image")
    _mutableState.update { it.copy(image = image) }
    println("DEBUG: AddTrophyViewModel state updated, current image: ${_mutableState.value.image}")
  }

  fun updateNotes(notes: String) {
    _mutableState.update { it.copy(notes = notes) }
  }

  fun addTrophy() {
    viewModelScope.launch {
      val state = _mutableState.value
      println("DEBUG: Adding trophy with image: ${state.image}")
      addTrophy(
        fishType = state.fishType,
        weight = state.weight.toDoubleOrNull() ?: 0.0,
        length = state.length.toDoubleOrNull(),
        location = state.location,
        date = state.date,
        image = state.image,
        notes = state.notes.takeIf { it.isNotEmpty() },
      )
    }
  }
}
