package co.kluvaka.cmp.features.equipment.ui.add.equipment

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

sealed interface AddEquipmentOperation {

  sealed interface Actions : AddEquipmentOperation {
    data class AddCameraImage(val imageUri: String?): Actions
    data class AddGalleryImages(val imageUris: List<String>): Actions
    data class DeleteImage(val index: Int): Actions
    data class EditEquipment(val equipment: Equipment) : Actions
    data class PriceUpdate(val price: String) : Actions
    object Save : Actions
    data class TitleUpdate(val title: String) : Actions
  }
}