package co.kluvaka.cmp.features.sessions.ui.completed

import co.kluvaka.cmp.features.sessions.domain.model.FishingSession
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent

data class CompletedSessionState(
  val session: FishingSession?,
  val events: List<FishingSessionEvent> = emptyList(),
)

