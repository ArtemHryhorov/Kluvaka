package co.kluvaka.cmp.features.sessions.ui.start.session

import co.kluvaka.cmp.features.sessions.domain.model.Rod

data class StartSessionState(
  val location: String = "",
  val date: Long = 0L,
  val rods: List<Rod> = emptyList(),
) {
  val isStartSessionEnabled: Boolean
    get() = location.isNotBlank() && rods.isNotEmpty()
}
