package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.model.FishingSession

fun interface FinishActiveSession {
  suspend operator fun invoke(session: FishingSession)
}