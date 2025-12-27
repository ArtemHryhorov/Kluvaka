package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.model.Session

fun interface GetSessionById {
  suspend operator fun invoke(sessionId: Int): Session
}
