package co.kluvaka.cmp.features.trophies.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.trophies.domain.usecase.GetTrophyById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrophyDetailViewModel(
  private val getTrophyById: GetTrophyById,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(TrophyDetailState())
  val state: StateFlow<TrophyDetailState> = _mutableState

  fun loadTrophy(trophyId: Int) {
    viewModelScope.launch {
      val trophy = getTrophyById(trophyId)
      _mutableState.update { it.copy(trophy = trophy) }
    }
  }
}
