package co.kluvaka.cmp.sessions.ui.history

import co.kluvaka.cmp.sessions.domain.model.FishingSession

data class SessionsHistoryState(
  val sessions: List<FishingSession> = emptyList(),
)
