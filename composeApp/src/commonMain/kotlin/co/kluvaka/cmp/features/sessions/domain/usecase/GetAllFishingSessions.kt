package co.kluvaka.cmp.features.sessions.domain.usecase

import co.kluvaka.cmp.features.sessions.domain.model.FishingSession

fun interface GetAllFishingSessions {
  suspend operator fun invoke(): List<FishingSession>
}