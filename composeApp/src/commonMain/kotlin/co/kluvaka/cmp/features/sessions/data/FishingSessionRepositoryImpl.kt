package co.kluvaka.cmp.features.sessions.data

import co.kluvaka.cmp.database.Database
import co.kluvaka.cmp.database.DatabaseDriverFactory
import co.kluvaka.cmp.features.sessions.domain.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession

class FishingSessionRepositoryImpl(
  databaseDriverFactory: DatabaseDriverFactory,
) : FishingSessionRepository {
  private val database = Database(databaseDriverFactory)

  override suspend fun createSession(session: FishingSession) {
    database.insertSessionWithRods(session)
  }

  override suspend fun getAllSessions(): List<FishingSession> {
    return database.getAllSessionsWithRods()
  }
}