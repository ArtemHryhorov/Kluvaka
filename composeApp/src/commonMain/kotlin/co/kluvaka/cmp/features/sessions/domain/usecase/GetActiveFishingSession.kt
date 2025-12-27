package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.model.Session

fun interface GetActiveFishingSession {
  suspend operator fun invoke(): Session?
}