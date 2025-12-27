package co.kluvaka.cmp.features.sessions.data.usecase

import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.CreateFishingSession

class CreateFishingSessionUseCase(
  private val repository: FishingSessionRepository,
) : CreateFishingSession {

  override suspend fun invoke(session: Session) = repository.createSession(session)
}
