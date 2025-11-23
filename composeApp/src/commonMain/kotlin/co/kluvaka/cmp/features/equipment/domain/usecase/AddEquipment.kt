package co.kluvaka.cmp.features.equipment.domain.usecase

fun interface AddEquipment {
  suspend operator fun invoke(
    title: String,
    images: List<String>,
    price: Double,
  )
}