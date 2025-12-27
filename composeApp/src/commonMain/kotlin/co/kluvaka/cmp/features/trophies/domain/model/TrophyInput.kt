package co.kluvaka.cmp.features.trophies.domain.model

data class TrophyInput(
  val date: Long?,
  val fishType: String,
  val images: List<String>,
  val length: String,
  val location: String,
  val notes: String,
  val weight: String,
) {
  companion object {
    val Initial = TrophyInput(
      date = null,
      fishType = "",
      images = emptyList(),
      length = "",
      location = "",
      notes = "",
      weight = "",
    )
  }
}