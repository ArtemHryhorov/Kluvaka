package co.kluvaka.cmp.features.trophies.domain.usecase

fun interface UpdateTrophy {
  suspend operator fun invoke(
    id: Long,
    fishType: String,
    weight: Double,
    length: Double?,
    location: String,
    date: String,
    image: String?,
    notes: String?,
  )
}