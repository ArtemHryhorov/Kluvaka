package co.kluvaka.cmp.sessions.domain.usecase

import co.kluvaka.cmp.sessions.domain.FishingSessionRepository
import co.kluvaka.cmp.sessions.domain.model.FishingSession

class CreateFishingSessionImpl(
  private val repository: FishingSessionRepository,
) : CreateFishingSession {

  override suspend fun invoke(session: FishingSession) = repository.createSession(session)
}

fun interface CreateFishingSession {
  suspend operator fun invoke(session: FishingSession)
}