package co.kluvaka.cmp.features.sessions.data.usecase

import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.UpdateSession

class UpdateSessionUseCase(
  private val repository: FishingSessionRepository,
) : UpdateSession {
  override suspend fun invoke(session: Session) {
    repository.updateSession(session)
  }
}

