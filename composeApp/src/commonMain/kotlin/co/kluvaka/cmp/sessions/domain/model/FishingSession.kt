package co.kluvaka.cmp.sessions.domain.model

data class FishingSession(
  val id: Int?,
  val location: String,
  val date: String,
  val rods: List<Rod>,
)
