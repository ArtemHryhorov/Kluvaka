package co.kluvaka.cmp.features.sessions.data.usecase

import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.GetActiveFishingSession

class GetActiveFishingSessionUseCase(
  private val repository: FishingSessionRepository,
) : GetActiveFishingSession {
  override suspend fun invoke(): Session? =
    repository
      .getAllSessions()
      .firstOrNull { it.isActive }
}