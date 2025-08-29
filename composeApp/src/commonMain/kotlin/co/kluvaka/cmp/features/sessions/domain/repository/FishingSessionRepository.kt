package co.kluvaka.cmp.features.sessions.domain.repository

import co.kluvaka.cmp.features.sessions.domain.model.FishingSession

interface FishingSessionRepository {

  suspend fun createSession(session: FishingSession)

  suspend fun getAllSessions(): List<FishingSession>
}