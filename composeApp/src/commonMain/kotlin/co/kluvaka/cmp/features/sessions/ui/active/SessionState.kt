package co.kluvaka.cmp.features.sessions.ui.active

import co.kluvaka.cmp.features.sessions.domain.model.FishingSession
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEventType
import co.kluvaka.cmp.features.sessions.domain.model.SessionMode

data class SessionState(
  val session: FishingSession?,
  val mode: SessionMode = SessionMode.Active,
  val events: List<FishingSessionEvent> = emptyList(),
  val showEventTypeDialog: Boolean = false,
  val showRodSelectionDialog: Boolean = false,
  val showFishEventDialog: Boolean = false,
  val showSpombEventDialog: Boolean = false,
  val showFishLooseDialog: Boolean = false,
  val showFinishSessionDialog: Boolean = false,
  val selectedEventType: FishingSessionEventType? = null,
  val selectedRodId: Int? = null,
  val newEventWeight: String = "",
  val newEventNotes: String = "",
  val newEventPhotos: List<String> = emptyList(),
  val newSpombCount: String = "",
  val sessionNotes: String = "",
  val sessionPhotos: List<String> = emptyList(),
)
