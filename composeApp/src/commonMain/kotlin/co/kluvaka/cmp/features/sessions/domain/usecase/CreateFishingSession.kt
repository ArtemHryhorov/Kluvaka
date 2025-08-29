package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession

class CreateFishingSessionImpl(
  private val repository: FishingSessionRepository,
) : CreateFishingSession {

  override suspend fun invoke(session: FishingSession) = repository.createSession(session)
}

fun interface CreateFishingSession {
  suspend operator fun invoke(session: FishingSession)
}