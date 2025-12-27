package co.kluvaka.cmp.features.trophies.ui.trophies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.trophies.domain.usecase.DeleteTrophy
import co.kluvaka.cmp.features.trophies.domain.usecase.GetAllTrophies
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesOperation.Actions
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesOperation.Events
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrophiesViewModel(
  private val getAllTrophies: GetAllTrophies,
  private val deleteTrophy: DeleteTrophy,
  private val reducer: TrophiesReducer,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(TrophiesState())
  val state: StateFlow<TrophiesState> = _mutableState

  fun handleAction(action: Actions) {
    when (action) {
      is Actions.DeleteTrophyCancel -> dispatchOperation(action)
      is Actions.DeleteTrophyConfirm -> handleDeleteTrophy(action)
      is Actions.DeleteTrophyRequest -> dispatchOperation(action)
      is Actions.FetchTrophies -> handleFetchTrophies()
    }
  }

  private fun dispatchOperation(operation: TrophiesOperation) {
    _mutableState.update { currentState ->
      reducer.updateState(currentState, operation)
    }
  }

  private fun handleFetchTrophies() {
    viewModelScope.launch {
      dispatchOperation(
        operation = Events.FetchTrophiesObserved(
          payload = getAllTrophies(),
        )
      )
    }
  }

  private fun handleDeleteTrophy(action: Actions.DeleteTrophyConfirm) {
    viewModelScope.launch {
      deleteTrophy(action.id)
      handleAction(Actions.FetchTrophies)
    }
  }
}
