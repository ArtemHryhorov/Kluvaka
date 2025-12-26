package co.kluvaka.cmp.features.equipment.domain.model

data class AddEquipmentInput(
  val title: String,
  val price: String,
  val images: List<String>,
) {
  companion object {
    val Initial = AddEquipmentInput(
      title = "",
      price = "",
      images = emptyList(),
    )
  }
}