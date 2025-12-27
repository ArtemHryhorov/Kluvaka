package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.model.Session

fun interface FinishActiveSession {
  suspend operator fun invoke(session: Session)
}