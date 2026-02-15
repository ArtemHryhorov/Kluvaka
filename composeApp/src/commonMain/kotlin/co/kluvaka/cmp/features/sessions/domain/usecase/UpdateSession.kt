package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.model.Session

fun interface UpdateSession {
  suspend operator fun invoke(session: Session)
}

