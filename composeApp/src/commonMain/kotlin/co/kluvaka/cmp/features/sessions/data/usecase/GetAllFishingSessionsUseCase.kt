package co.kluvaka.cmp.features.sessions.data.usecase

import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.GetAllFishingSessions

class GetAllFishingSessionsUseCase(
  private val repository: FishingSessionRepository,
) : GetAllFishingSessions {

  override suspend fun invoke(): List<Session> = repository.getAllSessions().reversed()
}
