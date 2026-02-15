package co.kluvaka.cmp.features.trophies.ui.add.trophy

import co.kluvaka.cmp.features.trophies.ui.add.trophy.AddTrophyOperation.Actions
import co.kluvaka.cmp.features.trophies.ui.add.trophy.AddTrophyOperation.Events


class AddTrophyReducer {
  fun updateState(
    currentState: AddTrophyState,
    operation: AddTrophyOperation,
  ): AddTrophyState = when (operation) {
    is Actions.AddCameraImage -> handleAddCameraImage(currentState, operation)
    is Actions.AddGalleryImages -> handleAddGalleryImages(currentState, operation)
    is Actions.DateUpdate -> handleDateUpdate(currentState, operation)
    is Actions.DeleteImage -> handleDeleteImage(currentState, operation)
    is Actions.EditTrophy -> currentState
    is Actions.FishTypeUpdate -> handleFishTypeUpdate(currentState, operation)
    is Actions.LengthUpdate -> handleLengthUpdate(currentState, operation)
    is Actions.LocationUpdate -> handleLocationUpdate(currentState, operation)
    is Actions.NotesUpdate -> handleNotesUpdate(currentState, operation)
    is Actions.Save -> currentState
    is Actions.SetCurrentDate -> handleSetCurrentDate(currentState, operation)
    is Actions.WeightUpdate -> handleWeightUpdate(currentState, operation)
    is Events.EditTrophyObserved -> handleEditTrophyObserved(currentState, operation)
  }

  private fun handleAddCameraImage(
    currentState: AddTrophyState,
    action: Actions.AddCameraImage,
  ): AddTrophyState = action.imageUri?.let { image ->
    currentState.copy(
      input = currentState.input.copy(
        images = currentState.input.images + image,
      ),
    )
  } ?: currentState

  private fun handleAddGalleryImages(
    currentState: AddTrophyState,
    action: Actions.AddGalleryImages,
  ): AddTrophyState = currentState.copy(
    input = currentState.input.copy(
      images = currentState.input.images + action.imageUris,
    ),
  )

  private fun handleDateUpdate(
    currentState: AddTrophyState,
    action: Actions.DateUpdate,
  ): AddTrophyState = currentState.copy(
    input = currentState.input.copy(
      date = action.date,
    ),
  )

  private fun handleDeleteImage(
    currentState: AddTrophyState,
    action: Actions.DeleteImage,
  ): AddTrophyState = currentState.copy(
    input = currentState.input.copy(
      images = currentState.input.images.toMutableList().apply {
        removeAt(action.index)
      },
    ),
  )

  private fun handleFishTypeUpdate(
    currentState: AddTrophyState,
    action: Actions.FishTypeUpdate,
  ): AddTrophyState = currentState.copy(
    input = currentState.input.copy(
      fishType = action.type,
    ),
  )

  private fun handleLengthUpdate(
    currentState: AddTrophyState,
    action: Actions.LengthUpdate,
  ): AddTrophyState = currentState.copy(
    input = currentState.input.copy(
      length = action.length.filter { it.isDigit() },
    ),
  )

  private fun handleLocationUpdate(
    currentState: AddTrophyState,
    action: Actions.LocationUpdate,
  ): AddTrophyState = currentState.copy(
    input = currentState.input.copy(
      location = action.location,
    ),
  )

  private fun handleNotesUpdate(
    currentState: AddTrophyState,
    action: Actions.NotesUpdate,
  ): AddTrophyState = currentState.copy(
    input = currentState.input.copy(
      notes = action.notes,
    ),
  )


  private fun handleSetCurrentDate(
    currentState: AddTrophyState,
    action: Actions.SetCurrentDate,
  ): AddTrophyState = currentState.copy(
    input = currentState.input.copy(
      date = action.date,
    ),
  )

  private fun handleWeightUpdate(
    currentState: AddTrophyState,
    action: Actions.WeightUpdate,
  ): AddTrophyState = currentState.copy(
    input = currentState.input.copy(
      weight = sanitizeDecimal(action.weight),
    ),
  )

  private fun handleEditTrophyObserved(
    currentState: AddTrophyState,
    event: Events.EditTrophyObserved,
  ): AddTrophyState =  currentState.copy(
    input = currentState.input.copy(
      images = event.payload.images,
      date = event.payload.date,
      fishType = event.payload.fishType,
      length = event.payload.length?.toString().orEmpty(),
      location = event.payload.location.orEmpty(),
      notes = event.payload.notes.orEmpty(),
    ),
    mode = AddTrophyMode.Edit(
      trophy = event.payload,
    ),
  )

  private fun sanitizeDecimal(raw: String): String {
    val normalized = raw.replace(",", ".")
    val builder = StringBuilder()
    var dotSeen = false
    normalized.forEach { ch ->
      when {
        ch.isDigit() -> builder.append(ch)
        ch == '.' && !dotSeen -> {
          builder.append(ch)
          dotSeen = true
        }
      }
    }
    return builder.toString()
  }
}