package co.kluvaka.cmp.features.sessions.ui.start.session

import co.kluvaka.cmp.features.sessions.domain.model.Rod

data class StartSessionState(
  val location: String = "",
  val date: String = "", // Change to Date format
  val rods: List<Rod> = emptyList(),
)
