package co.kluvaka.cmp.features.trophies.ui.add.trophy

import co.kluvaka.cmp.features.trophies.domain.model.Trophy

sealed interface AddTrophyOperation {

  sealed interface Actions : AddTrophyOperation {
    data class AddCameraImage(val imageUri: String?): Actions
    data class AddGalleryImages(val imageUris: List<String>): Actions
    data class DateUpdate(val date: Long): Actions
    data class DeleteImage(val index: Int): Actions
    data class EditTrophy(val trophyId: Int) : Actions
    data class FishTypeUpdate(val type: String) : Actions
    data class LengthUpdate(val length: String) : Actions
    data class LocationUpdate(val location: String) : Actions
    data class NotesUpdate(val notes: String) : Actions
    object Save : Actions
    data class SetCurrentDate(val date: Long) : Actions
    data class WeightUpdate(val weight: String) : Actions
  }

  sealed interface Events : AddTrophyOperation {
    data class EditTrophyObserved(val payload: Trophy) : Events
  }
}