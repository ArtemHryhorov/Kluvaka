package co.kluvaka.cmp.sessions.domain.usecase

import co.kluvaka.cmp.sessions.domain.FishingSessionRepository
import co.kluvaka.cmp.sessions.domain.model.FishingSession

class GetAllFishingSessionsImpl(
  private val repository: FishingSessionRepository,
) : GetAllFishingSessions {

  override suspend fun invoke(): List<FishingSession> = repository.getAllSessions()
}

fun interface GetAllFishingSessions {
  suspend operator fun invoke(): List<FishingSession>
}