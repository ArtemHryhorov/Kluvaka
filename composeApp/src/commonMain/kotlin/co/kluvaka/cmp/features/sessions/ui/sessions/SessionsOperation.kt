package co.kluvaka.cmp.features.sessions.ui.sessions

import co.kluvaka.cmp.features.sessions.domain.model.Session

sealed interface SessionsOperation {

  sealed interface Actions : SessionsOperation {
    data object FetchSessions : Actions
    data object DeleteSessionCancel : Actions
    data class DeleteSessionConfirm(val id: Int) : Actions
    data class DeleteSessionRequest(val session: Session) : Actions
    data object ToggleProgressMetric : Actions
  }

  sealed interface Events : SessionsOperation {
    data class FetchSessionsObserved(val payload: List<Session>) : Events
  }
}