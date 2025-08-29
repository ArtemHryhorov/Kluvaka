package co.kluvaka.cmp.features.equipment.domain.model

data class Equipment(
  val id: Int,
  val title: String,
  val image: String?,
  val price: Double,
)