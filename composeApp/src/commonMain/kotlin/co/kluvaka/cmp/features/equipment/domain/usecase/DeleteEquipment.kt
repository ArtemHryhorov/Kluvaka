package co.kluvaka.cmp.features.equipment.domain.usecase

fun interface DeleteEquipment {
  suspend operator fun invoke(id: Int)
}