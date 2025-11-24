package co.kluvaka.cmp.features.sessions.data.usecase

import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.AddSessionEvent
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent

class AddSessionEventUseCase(
  private val repository: FishingSessionRepository
) : AddSessionEvent {
  override suspend operator fun invoke(sessionId: Int, event: FishingSessionEvent) {
    repository.addEvent(sessionId, event)
  }
}

