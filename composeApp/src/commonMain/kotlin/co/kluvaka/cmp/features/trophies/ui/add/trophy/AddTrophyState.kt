package co.kluvaka.cmp.features.trophies.ui.add.trophy

data class AddTrophyState(
  val fishType: String = "",
  val weight: String = "",
  val length: String = "",
  val location: String = "",
  val date: String = "",
  val image: String? = null,
  val notes: String = "",
)
