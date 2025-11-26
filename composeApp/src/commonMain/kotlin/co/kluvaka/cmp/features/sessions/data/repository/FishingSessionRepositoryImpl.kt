package co.kluvaka.cmp.features.sessions.data.repository

import co.kluvaka.cmp.database.SessionDatabase
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository

class FishingSessionRepositoryImpl(
  private val database: SessionDatabase,
) : FishingSessionRepository {

  override suspend fun createSession(session: FishingSession) {
    database.insertSessionWithRods(session)
  }

  override suspend fun getAllSessions(): List<FishingSession> {
    return database.getAllSessionsWithRods()
  }

  override suspend fun getSessionById(sessionId: Int): FishingSession {
    return database.getSessionWithRods(sessionId)
  }

  override suspend fun updateSession(session: FishingSession) {
    database.updateSession(session)
  }

  override suspend fun addEvent(sessionId: Int, event: FishingSessionEvent) {
    database.insertEvent(sessionId, event)
  }

  override suspend fun deleteSession(sessionId: Int) {
    database.deleteSession(sessionId)
  }

  override suspend fun getEventById(eventId: Int): FishingSessionEvent? {
    return database.getEventById(eventId)
  }
}