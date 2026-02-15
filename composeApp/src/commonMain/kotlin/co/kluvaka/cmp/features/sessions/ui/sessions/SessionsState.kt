package co.kluvaka.cmp.features.sessions.ui.sessions

import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.sessions.domain.model.Session

data class SessionsState(
  val sessions: List<Session>,
  val deleteConfirmationDialog: DialogState<Session>,
  val progressMetric: ProgressMetric = ProgressMetric.SessionsCount,
) {
  companion object {
    val Initial = SessionsState(
      sessions = emptyList(),
      deleteConfirmationDialog = DialogState.Hidden,
    )
  }
}

enum class ProgressMetric {
  SessionsCount,
  FishCount,
  FishWeight,
}
