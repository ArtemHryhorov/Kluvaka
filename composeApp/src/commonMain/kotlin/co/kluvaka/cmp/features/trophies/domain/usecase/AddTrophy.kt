package co.kluvaka.cmp.features.trophies.domain.usecase

fun interface AddTrophy {
  suspend operator fun invoke(
    fishType: String,
    weight: Double?,
    length: Double?,
    location: String?,
    date: Long,
    images: List<String>,
    notes: String?,
  )
}
