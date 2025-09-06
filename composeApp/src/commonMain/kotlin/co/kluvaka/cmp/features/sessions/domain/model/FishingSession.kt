package co.kluvaka.cmp.features.sessions.domain.model

data class FishingSession(
  val id: Int?,
  val location: String,
  val date: String,
  val rods: List<Rod>,
  val isActive: Boolean,
)
