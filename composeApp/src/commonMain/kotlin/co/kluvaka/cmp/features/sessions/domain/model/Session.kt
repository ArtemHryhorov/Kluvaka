package co.kluvaka.cmp.features.sessions.domain.model

data class Session(
  val id: Int?,
  val location: String,
  val dateMillis: Long,
  val rods: List<Rod>,
  val isActive: Boolean,
  val events: List<FishingSessionEvent> = emptyList(),
)
