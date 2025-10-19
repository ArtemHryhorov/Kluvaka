package co.kluvaka.cmp.features.trophies.ui.trophies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.trophies.domain.usecase.DeleteTrophy
import co.kluvaka.cmp.features.trophies.domain.usecase.GetAllTrophies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrophiesViewModel(
  private val getAllTrophies: GetAllTrophies,
  private val deleteTrophy: DeleteTrophy,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(TrophiesState())
  val state: StateFlow<TrophiesState> = _mutableState

  fun fetchTrophies() {
    viewModelScope.launch {
      _mutableState.update {
        it.copy(trophies = getAllTrophies())
      }
    }
  }

  fun delete(id: Int) {
    viewModelScope.launch {
      deleteTrophy(id)
      fetchTrophies()
    }
  }
}
