package co.kluvaka.cmp.features.sessions.domain.model

sealed interface FishingSessionEventType {

  data class Fish(val rodId: Int) : FishingSessionEventType

  data class Loose(val rodId: Int) : FishingSessionEventType

  data class Spomb(val count: Int) : FishingSessionEventType
}