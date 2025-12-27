package co.kluvaka.cmp.features.trophies.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.trophies.domain.usecase.GetTrophyById
import co.kluvaka.cmp.features.trophies.ui.details.TrophyDetailsOperation.Action
import co.kluvaka.cmp.features.trophies.ui.details.TrophyDetailsOperation.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrophyDetailsViewModel(
  private val getTrophyById: GetTrophyById,
  private val reducer: TrophyDetailsReducer,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(TrophyDetailsState.Initial)
  val state: StateFlow<TrophyDetailsState> = _mutableState

  fun handleAction(action: Action) {
    when (action) {
      is Action.GetTrophy -> handleGetTrophy(action.id)
    }
  }

  private fun dispatchOperation(operation: TrophyDetailsOperation) {
    viewModelScope.launch {
      _mutableState.update { currentState ->
        reducer.updateState(currentState, operation)
      }
    }
  }

  private fun handleGetTrophy(trophyId: Int) {
    viewModelScope.launch {
      val equipment = getTrophyById(trophyId) ?: return@launch
      val event = Event.TrophyObserved(equipment)
      dispatchOperation(event)
    }
  }
}
