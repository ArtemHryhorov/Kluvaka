package co.kluvaka.cmp.features.sessions.ui.history

import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession

data class SessionsHistoryState(
  val sessions: List<FishingSession> = emptyList(),
  val deleteDialogState: DialogState<FishingSession> = DialogState.Hidden,
) {
  val anyActiveSession = sessions.any { it.isActive }
}
