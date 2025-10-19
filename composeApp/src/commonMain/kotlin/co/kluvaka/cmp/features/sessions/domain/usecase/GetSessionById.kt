package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.model.FishingSession

fun interface GetSessionById {
  suspend operator fun invoke(sessionId: Int): FishingSession
}
