package co.kluvaka.cmp.features.trophies.domain.model

data class Trophy(
  val id: Int,
  val fishType: String,
  val weight: Double?,
  val length: Double?,
  val location: String?,
  val date: Long?,
  val images: List<String>,
  val notes: String?,
)
