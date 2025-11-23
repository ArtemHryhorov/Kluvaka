package co.kluvaka.cmp.features.trophies.ui.add.trophy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.trophies.domain.usecase.AddTrophy
import co.kluvaka.cmp.features.trophies.domain.usecase.UpdateTrophy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddTrophyViewModel(
  private val addTrophy: AddTrophy,
  private val updateTrophy: UpdateTrophy,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(AddTrophyState())
  val state: StateFlow<AddTrophyState> = _mutableState

  fun setAddTrophyMode(mode: AddTrophyMode) {
    _mutableState.update { state ->
      state.copy(mode = mode)
    }

    if (mode is AddTrophyMode.Edit) {
      _mutableState.update { state ->
        state.copy(
          trophyInput = state.trophyInput.copy(
            fishType = mode.trophy.fishType,
            weight = mode.trophy.weight?.toString() ?: "",
            length = mode.trophy.length?.toString() ?: "",
            location = mode.trophy.location ?: "",
            date = mode.trophy.date,
            images = mode.trophy.images,
            notes = mode.trophy.notes ?: "",
          ),
        )
      }
    }
  }

  fun updateFishType(fishType: String) {
    _mutableState.update { state ->
      state.copy(
        trophyInput = state.trophyInput.copy(
          fishType = fishType,
        ),
      )
    }
  }

  fun updateWeight(weight: String) {
    _mutableState.update { state ->
      state.copy(
        trophyInput = state.trophyInput.copy(
          weight = weight,
        ),
      )
    }
  }

  fun updateLength(length: String) {
    _mutableState.update { state ->
      state.copy(
        trophyInput = state.trophyInput.copy(
          length = length,
        ),
      )
    }
  }

  fun updateLocation(location: String) {
    _mutableState.update { state ->
      state.copy(
        trophyInput = state.trophyInput.copy(
          location = location,
        ),
      )
    }
  }

  fun updateDate(date: String) {
    _mutableState.update { state ->
      state.copy(
        trophyInput = state.trophyInput.copy(
          date = date,
        ),
      )
    }
  }

  fun addImage(image: String?) {
    if (image == null) return
    _mutableState.update { state ->
      state.copy(
        trophyInput = state.trophyInput.copy(
          images = state.trophyInput.images + image,
        ),
      )
    }
  }

  fun removeImage(index: Int) {
    _mutableState.update { state ->
      val newImages = state.trophyInput.images.toMutableList().apply {
        removeAt(index)
      }
      state.copy(
        trophyInput = state.trophyInput.copy(
          images = newImages,
        ),
      )
    }
  }

  fun updateNotes(notes: String) {
    _mutableState.update { state ->
      state.copy(
        trophyInput = state.trophyInput.copy(
          notes = notes,
        ),
      )
    }
  }

  fun save() {
    when (val mode = state.value.mode) {
      is AddTrophyMode.Edit -> editTrophy(mode.trophy.id)
      AddTrophyMode.New -> addTrophy()
    }
  }

  private fun addTrophy() {
    viewModelScope.launch {
      val trophyInput = _mutableState.value.trophyInput
      addTrophy(
        fishType = trophyInput.fishType,
        weight = trophyInput.weight.toDoubleOrNull(),
        length = trophyInput.length.toDoubleOrNull(),
        location = trophyInput.location.takeIf { it.isNotEmpty() },
        date = trophyInput.date,
        images = trophyInput.images,
        notes = trophyInput.notes.takeIf { it.isNotEmpty() },
      )
    }
  }

  private fun editTrophy(id: Int) {
    viewModelScope.launch {
      val trophyInput = _mutableState.value.trophyInput
      updateTrophy(
        id = id.toLong(),
        fishType = trophyInput.fishType,
        weight = trophyInput.weight.toDoubleOrNull() ?: 0.0,
        length = trophyInput.length.toDoubleOrNull(),
        location = trophyInput.location,
        date = trophyInput.date,
        images = trophyInput.images,
        notes = trophyInput.notes.takeIf { it.isNotEmpty() },
      )
    }
  }
}

