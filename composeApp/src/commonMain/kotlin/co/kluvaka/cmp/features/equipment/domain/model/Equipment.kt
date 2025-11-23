package co.kluvaka.cmp.features.equipment.domain.model

data class Equipment(
  val id: Int,
  val title: String,
  val images: List<String>,
  val price: Double,
)