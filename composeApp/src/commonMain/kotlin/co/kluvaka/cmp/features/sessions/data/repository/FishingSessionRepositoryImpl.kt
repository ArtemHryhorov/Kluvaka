package co.kluvaka.cmp.features.sessions.data.repository

import co.kluvaka.cmp.database.SessionDatabase
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession
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
}