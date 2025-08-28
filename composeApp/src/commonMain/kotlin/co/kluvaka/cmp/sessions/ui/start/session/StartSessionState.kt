package co.kluvaka.cmp.sessions.ui.start.session

import co.kluvaka.cmp.sessions.domain.model.Rod

data class StartSessionState(
  val location: String = "",
  val date: String = "", // Change to Date format
  val rods: List<Rod> = emptyList(),
)
