package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent

interface AddSessionEvent {
  suspend operator fun invoke(sessionId: Int, event: FishingSessionEvent)
}

