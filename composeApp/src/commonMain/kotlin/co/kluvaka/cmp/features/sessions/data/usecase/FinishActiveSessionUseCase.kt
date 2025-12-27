package co.kluvaka.cmp.features.sessions.data.usecase

import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.FinishActiveSession

class FinishActiveSessionUseCase(
  private val repository: FishingSessionRepository,
) : FinishActiveSession {
  override suspend fun invoke(session: Session) {
    repository.updateSession(session)
  }
}