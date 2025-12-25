package co.kluvaka.cmp.features.sessions.ui.start.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.common.domain.DateFormatter
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession
import co.kluvaka.cmp.features.sessions.domain.model.Rod
import co.kluvaka.cmp.features.sessions.domain.usecase.CreateFishingSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class StartSessionViewModel(
  private val createFishingSession: CreateFishingSession,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(StartSessionState())
  val state: StateFlow<StartSessionState> = _mutableState

  init {
    _mutableState.update {
      it.copy(
        date = Clock.System.now().toEpochMilliseconds(),
        rods = it.rods + createNewEmptyRod()
      )
    }
  }

  fun addEmptyRod() {
    _mutableState.update {
      it.copy(
        rods = it.rods + createNewEmptyRod()
      )
    }
  }

  fun removeRod(rodNumber: Int) {
    _mutableState.update {
      it.copy(
        rods = it.rods.toMutableList().filter { rod -> rod.order != rodNumber },
      )
    }
  }

  fun changeSessionLocation(location: String) {
    _mutableState.update { currentState ->
      currentState.copy(
        location = location.ifEmpty { null },
      )
    }
  }

  fun changeSessionDate(date: Long) {
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
    val distanceInt = distance.toIntOrNull() ?: 0
    _mutableState.update { currentState ->
      currentState.copy(
        rods = currentState.rods.map { rod ->
          if (rod.order == rodNumber) {
            rod.copy(distance = distanceInt)
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
      location = _mutableState.value.location ?: DateFormatter.format(_mutableState.value.date),
      date = DateFormatter.format(_mutableState.value.date),
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