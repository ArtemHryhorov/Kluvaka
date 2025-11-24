package co.kluvaka.cmp.features.sessions.domain.repository

import co.kluvaka.cmp.features.sessions.domain.model.FishingSession
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent

interface FishingSessionRepository {

  suspend fun createSession(session: FishingSession)

  suspend fun getAllSessions(): List<FishingSession>

  suspend fun getSessionById(sessionId: Int): FishingSession

  suspend fun updateSession(session: FishingSession)

  suspend fun addEvent(sessionId: Int, event: FishingSessionEvent)
}