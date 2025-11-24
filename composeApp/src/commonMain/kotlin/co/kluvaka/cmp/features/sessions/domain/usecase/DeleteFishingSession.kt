package co.kluvaka.cmp.features.sessions.domain.usecase

fun interface DeleteFishingSession {
  suspend operator fun invoke(sessionId: Int)
}

