package co.kluvaka.cmp.features.sessions.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.usecase.GetSessionById
import co.kluvaka.cmp.features.sessions.domain.usecase.GetSessionEventById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailedSessionEventState(
  val session: FishingSession? = null,
  val event: FishingSessionEvent? = null,
  val isLoading: Boolean = true,
  val errorMessage: String? = null,
)

class DetailedSessionEventViewModel(
  private val getSessionById: GetSessionById,
  private val getSessionEventById: GetSessionEventById,
  private val sessionId: Int,
  private val eventId: Int,
) : ViewModel() {

  private val _mutableState = MutableStateFlow(DetailedSessionEventState())
  val state: StateFlow<DetailedSessionEventState> = _mutableState

  init {
    loadData()
  }

  private fun loadData() {
    viewModelScope.launch {
      try {
        val session = getSessionById(sessionId)
        val event = getSessionEventById(eventId)
        _mutableState.update {
          it.copy(
            session = session,
            event = event,
            isLoading = false,
            errorMessage = if (event == null) "Событие не найдено" else null
          )
        }
      } catch (e: Exception) {
        _mutableState.update {
          it.copy(
            session = null,
            event = null,
            isLoading = false,
            errorMessage = e.message
          )
        }
      }
    }
  }
}

