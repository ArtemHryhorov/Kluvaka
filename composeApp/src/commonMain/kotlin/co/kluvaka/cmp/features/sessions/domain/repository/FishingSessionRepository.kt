package co.kluvaka.cmp.features.sessions.domain.repository

import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent

interface FishingSessionRepository {

  suspend fun createSession(session: Session)

  suspend fun getAllSessions(): List<Session>

  suspend fun getSessionById(sessionId: Int): Session

  suspend fun updateSession(session: Session)

  suspend fun addEvent(sessionId: Int, event: FishingSessionEvent)

  suspend fun deleteSession(sessionId: Int)

  suspend fun getEventById(eventId: Int): FishingSessionEvent?
}