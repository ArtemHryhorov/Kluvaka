package co.kluvaka.cmp.features.equipment.domain.usecase

fun interface UpdateEquipment {
  suspend operator fun invoke(
    id: Int,
    title: String,
    images: List<String>,
    price: Double,
  )
}