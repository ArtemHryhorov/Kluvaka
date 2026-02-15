package co.kluvaka.cmp.features.sessions.ui.session

import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEventType
import co.kluvaka.cmp.features.sessions.domain.model.SessionMode

data class SessionState(
  val session: Session?,
  val mode: SessionMode = SessionMode.Active,
  val events: List<FishingSessionEvent> = emptyList(),
  val selectedTab: SessionTab = SessionTab.Events,
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
  val sessionCoverPhoto: String? = null,
)

enum class SessionTab { Events, Information }
