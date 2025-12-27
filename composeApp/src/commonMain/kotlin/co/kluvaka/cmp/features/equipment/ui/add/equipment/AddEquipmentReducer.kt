package co.kluvaka.cmp.features.equipment.ui.add.equipment

import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentOperation.*

class AddEquipmentReducer {
  fun updateState(
    operation: AddEquipmentOperation,
    currentState: AddEquipmentState,
  ): AddEquipmentState = when (operation) {
    is Actions.AddCameraImage -> handleAddCameraImage(currentState, operation)
    is Actions.AddGalleryImages -> handleAddGalleryImages(currentState, operation)
    is Actions.DeleteImage -> handleDeleteImage(currentState, operation)
    is Actions.EditEquipment -> currentState
    is Actions.PriceUpdate -> handlePriceUpdate(currentState, operation)
    is Actions.Save -> currentState
    is Actions.TitleUpdate -> handleTitleUpdate(currentState, operation)
    is Events.EditEquipmentObserved -> handleEditEquipmentObserved(currentState, operation)
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

  private fun handleEditEquipmentObserved(
    currentState: AddEquipmentState,
    event: Events.EditEquipmentObserved,
  ): AddEquipmentState = currentState.copy(
    input = currentState.input.copy(
      images = event.payload.images,
      title = event.payload.title,
      price = event.payload.price.toString(),
    ),
    mode = AddEquipmentMode.Edit(
      equipment = event.payload,
    ),
  )
}