package co.kluvaka.cmp.features.sessions.ui.start.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession
import co.kluvaka.cmp.features.sessions.domain.model.Rod
import co.kluvaka.cmp.features.sessions.domain.usecase.CreateFishingSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartSessionViewModel(
  private val createFishingSession: CreateFishingSession,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(StartSessionState())
  val state: StateFlow<StartSessionState> = _mutableState

  fun addEmptyRod() {
    _mutableState.update {
      it.copy(
        rods = it.rods + createNewEmptyRod()
      )
    }
  }

  fun changeSessionLocation(location: String) {
    _mutableState.update { currentState ->
      currentState.copy(
        location = location,
      )
    }
  }

  fun changeSessionDate(date: String) {
    _mutableState.update { currentState ->
      currentState.copy(
        date = date,
      )
    }
  }

  fun changeRodBait(rodNumber: Int, bait: String) {
    _mutableState.update { currentState ->
      currentState.copy(
        rods = currentState.rods.map { rod ->
          if (rod.order == rodNumber) {
            rod.copy(bait = bait)
          } else {
            rod
          }
        }
      )
    }
  }

  fun changeRodDistance(rodNumber: Int, distance: String) {
    if (distance.isBlank()) return
    _mutableState.update { currentState ->
      currentState.copy(
        rods = currentState.rods.map { rod ->
          if (rod.order == rodNumber) {
            rod.copy(distance = distance.toInt())
          } else {
            rod
          }
        }
      )
    }
  }

  fun saveSession() {
    val session = FishingSession(
      id = null,
      location = _mutableState.value.location,
      date = _mutableState.value.date,
      rods = _mutableState.value.rods,
      isActive = true,
    )
    viewModelScope.launch {
      createFishingSession(session)
    }
  }

  private fun createNewEmptyRod(): Rod = Rod(
    order = _mutableState.value.rods.size + 1,
    distance = 0,
    bait = "",
  )
}