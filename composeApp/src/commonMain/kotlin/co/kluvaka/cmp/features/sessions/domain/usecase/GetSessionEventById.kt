package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent

fun interface GetSessionEventById {
  suspend operator fun invoke(eventId: Int): FishingSessionEvent?
}

