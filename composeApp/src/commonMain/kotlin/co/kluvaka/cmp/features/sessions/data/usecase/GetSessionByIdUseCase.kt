package co.kluvaka.cmp.features.sessions.data.usecase

import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.GetSessionById

class GetSessionByIdUseCase(
  private val repository: FishingSessionRepository,
) : GetSessionById {
  override suspend fun invoke(sessionId: Int): Session =
    repository.getSessionById(sessionId)
}
