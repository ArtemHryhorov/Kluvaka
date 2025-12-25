package co.kluvaka.cmp.features.equipment.ui.add.equipment

import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentOperation.Actions

class AddEquipmentReducer {
  fun updateState(
    operation: AddEquipmentOperation,
    currentState: AddEquipmentState,
  ): AddEquipmentState = when (operation) {
    is Actions.AddCameraImage -> handleAddCameraImage(currentState, operation)
    is Actions.AddGalleryImages -> handleAddGalleryImages(currentState, operation)
    is Actions.DeleteImage -> handleDeleteImage(currentState, operation)
    is Actions.EditEquipment -> handleEditEquipment(currentState, operation)
    is Actions.PriceUpdate -> handlePriceUpdate(currentState, operation)
    is Actions.Save -> currentState
    is Actions.TitleUpdate -> handleTitleUpdate(currentState, operation)
  }

  private fun handleAddCameraImage(
    currentState: AddEquipmentState,
    action: Actions.AddCameraImage,
  ): AddEquipmentState = action.imageUri?.let { image ->
    currentState.copy(
      input = currentState.input.copy(
        images = currentState.input.images + image,
      ),
    )
  } ?: currentState

  private fun handleAddGalleryImages(
    currentState: AddEquipmentState,
    action: Actions.AddGalleryImages,
  ): AddEquipmentState = currentState.copy(
    input = currentState.input.copy(
      images = currentState.input.images + action.imageUris,
    ),
  )

  private fun handleDeleteImage(
    currentState: AddEquipmentState,
    action: Actions.DeleteImage,
  ): AddEquipmentState = currentState.copy(
    input = currentState.input.copy(
      images = currentState.input.images.toMutableList().apply {
        removeAt(action.index)
      },
    ),
  )

  private fun handleEditEquipment(
    currentState: AddEquipmentState,
    action: Actions.EditEquipment,
  ): AddEquipmentState = currentState.copy(
    input = currentState.input.copy(
      images = action.equipment.images,
      title = action.equipment.title,
      price = action.equipment.price.toString(),
    ),
    mode = AddEquipmentMode.Edit(
      equipment = action.equipment,
    ),
  )

  private fun handlePriceUpdate(
    currentState: AddEquipmentState,
    action: Actions.PriceUpdate,
  ): AddEquipmentState = currentState.copy(
    input = currentState.input.copy(
      price = action.price,
    ),
  )

  private fun handleTitleUpdate(
    currentState: AddEquipmentState,
    action: Actions.TitleUpdate,
  ): AddEquipmentState = currentState.copy(
    input = currentState.input.copy(
      title = action.title,
    ),
  )
}