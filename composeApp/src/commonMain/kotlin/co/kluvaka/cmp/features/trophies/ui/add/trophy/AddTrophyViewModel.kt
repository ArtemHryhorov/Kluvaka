package co.kluvaka.cmp.features.trophies.ui.add.trophy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.trophies.domain.usecase.AddTrophy
import co.kluvaka.cmp.features.trophies.domain.usecase.GetTrophyById
import co.kluvaka.cmp.features.trophies.domain.usecase.UpdateTrophy
import co.kluvaka.cmp.features.trophies.ui.add.trophy.AddTrophyOperation.Actions
import co.kluvaka.cmp.features.trophies.ui.add.trophy.AddTrophyOperation.Events
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class AddTrophyViewModel(
  private val addTrophy: AddTrophy,
  private val getTrophyById: GetTrophyById,
  private val reducer: AddTrophyReducer,
  private val updateTrophy: UpdateTrophy,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(AddTrophyState.Initial)
  val state: StateFlow<AddTrophyState> = _mutableState

  fun handleAction(action: Actions) {
    when (action) {
      is Actions.EditTrophy -> handleEditTrophy(action)
      is Actions.Save -> handleSave()
      else -> dispatchOperation(action)
    }
  }

  private fun dispatchOperation(operation: AddTrophyOperation) {
    _mutableState.update { currentState ->
      reducer.updateState(currentState, operation)
    }
  }

  private fun handleEditTrophy(action: Actions.EditTrophy ) {
    viewModelScope.launch {
      val trophy = getTrophyById(action.trophyId) ?: return@launch
      val event = Events.EditTrophyObserved(trophy)
      dispatchOperation(event)
    }
  }

  private fun handleSave() {
    when (val mode = state.value.mode) {
      is AddTrophyMode.Edit -> viewModelScope.launch {
        updateTrophy(
          id = mode.trophy.id,
          input = _mutableState.value.input,
        )
      }

      AddTrophyMode.New -> viewModelScope.launch {
        addTrophy(
          input = _mutableState.value.input,
        )
      }
    }
  }
}
