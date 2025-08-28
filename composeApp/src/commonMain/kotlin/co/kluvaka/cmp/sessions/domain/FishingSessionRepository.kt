package co.kluvaka.cmp.sessions.domain

import co.kluvaka.cmp.sessions.domain.model.FishingSession

interface FishingSessionRepository {

  suspend fun createSession(session: FishingSession)

  suspend fun getAllSessions(): List<FishingSession>
}