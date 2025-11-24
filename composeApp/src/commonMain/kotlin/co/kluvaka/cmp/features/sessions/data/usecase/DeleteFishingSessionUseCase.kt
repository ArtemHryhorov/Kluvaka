package co.kluvaka.cmp.features.sessions.data.usecase

import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.DeleteFishingSession

class DeleteFishingSessionUseCase(
  private val repository: FishingSessionRepository,
) : DeleteFishingSession {
  override suspend fun invoke(sessionId: Int) {
    repository.deleteSession(sessionId)
  }
}

