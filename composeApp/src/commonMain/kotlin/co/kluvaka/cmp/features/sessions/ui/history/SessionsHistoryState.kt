package co.kluvaka.cmp.features.sessions.ui.history

import co.kluvaka.cmp.features.sessions.domain.model.FishingSession

data class SessionsHistoryState(
  val sessions: List<FishingSession> = emptyList(),
)
