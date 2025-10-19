package co.kluvaka.cmp.features.trophies.domain.model

data class Trophy(
  val id: Int,
  val fishType: String,
  val weight: Double,
  val length: Double?,
  val location: String,
  val date: String,
  val image: String?,
  val notes: String?,
)
