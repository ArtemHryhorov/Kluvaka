package co.kluvaka.cmp.features.sessions.domain.model

fun List<FishingSessionEvent>.totalFishCount(): Int =
  count { it.type is FishingSessionEventType.Fish }

fun List<FishingSessionEvent>.totalFishWeight(): Double =
  filter { it.type is FishingSessionEventType.Fish }
    .sumOf { event ->
      event.weight?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
    }

