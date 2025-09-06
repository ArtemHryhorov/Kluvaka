package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.model.FishingSession

fun interface GetActiveFishingSession {
  suspend operator fun invoke(): FishingSession?
}