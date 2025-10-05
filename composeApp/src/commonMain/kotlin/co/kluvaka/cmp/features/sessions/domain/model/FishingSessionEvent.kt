package co.kluvaka.cmp.features.sessions.domain.model

data class FishingSessionEvent(
  val id: Int,
  val type: FishingSessionEventType,
  val timestamp: String,
  val weight: String? = null, // For fish events
  val photos: List<String> = emptyList(), // For fish events
)