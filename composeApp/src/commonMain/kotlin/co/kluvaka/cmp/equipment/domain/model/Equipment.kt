package co.kluvaka.cmp.equipment.domain.model

data class Equipment(
  val id: Int,
  val title: String,
  val image: String?,
  val price: Double,
)