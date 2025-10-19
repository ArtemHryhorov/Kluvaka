package co.kluvaka.cmp.features.trophies.domain.usecase

fun interface DeleteTrophy {
  suspend operator fun invoke(id: Int)
}
