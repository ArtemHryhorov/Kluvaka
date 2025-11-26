package co.kluvaka.cmp.features.sessions.data.usecase

import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.GetSessionEventById

class GetSessionEventByIdUseCase(
  private val repository: FishingSessionRepository,
) : GetSessionEventById {
  override suspend fun invoke(eventId: Int): FishingSessionEvent? {
    return repository.getEventById(eventId)
  }
}

