package co.kluvaka.cmp.features.trophies.domain.usecase

fun interface UpdateTrophy {
  suspend operator fun invoke(
    id: Long,
    fishType: String,
    weight: Double,
    length: Double?,
    location: String,
    date: String,
    images: List<String>,
    notes: String?,
  )
}