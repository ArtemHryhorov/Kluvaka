package co.kluvaka.cmp.features.equipment.ui.equipments

import co.kluvaka.cmp.features.equipment.domain.model.Equipment

sealed interface EquipmentsOperation {

  sealed interface Actions : EquipmentsOperation {
    object DeleteEquipmentCancel : Actions
    data class DeleteEquipmentConfirm(val id: Int) : Actions
    data class DeleteEquipmentRequest(val equipment: Equipment) : Actions
    object FetchEquipments : Actions
  }

  sealed interface Events : EquipmentsOperation {
    data class FetchEquipmentsObserved(val payload: List<Equipment>) : Events
  }
}
