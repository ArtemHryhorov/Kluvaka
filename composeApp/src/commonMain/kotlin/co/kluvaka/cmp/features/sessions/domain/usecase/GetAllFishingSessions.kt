package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.model.Session

fun interface GetAllFishingSessions {
  suspend operator fun invoke(): List<Session>
}