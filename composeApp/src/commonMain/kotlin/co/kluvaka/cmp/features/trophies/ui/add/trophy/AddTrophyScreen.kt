@file:OptIn(ExperimentalTime::class)

package co.kluvaka.cmp.features.trophies.ui.add.trophy

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.common.ui.DatePickerField
import co.kluvaka.cmp.features.trophies.domain.rememberPhotoPicker
import co.kluvaka.cmp.features.trophies.ui.add.trophy.AddTrophyOperation.Actions.*
import coil3.compose.rememberAsyncImagePainter
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class AddTrophyScreen(
  val trophyId: Int? = null,
) : Screen {

  data class Actions(
    val onDateUpdate: (Long) -> Unit,
    val onEditTrophy: (Int) -> Unit,
    val onFishTypeUpdate: (String) -> Unit,
    val onImageDelete: (Int) -> Unit,
    val onLengthUpdate: (String) -> Unit,
    val onLocationUpdate: (String) -> Unit,
    val onNavigateBack: () -> Unit,
    val onNotesUpdate: (String) -> Unit,
    val onOpenCamera: () -> Unit,
    val onOpenGallery: () -> Unit,
    val onSave: () -> Unit,
    val onSetCurrentDate: (Long) -> Unit,
    val onWeightUpdate: (String) -> Unit,
  ) {
    companion object {
      val Empty = Actions(
        onDateUpdate = {},
        onEditTrophy = {},
        onFishTypeUpdate = {},
        onImageDelete = {},
        onLengthUpdate = {},
        onLocationUpdate = {},
        onNavigateBack = {},
        onNotesUpdate = {},
        onOpenCamera = {},
        onOpenGallery = {},
        onSave = {},
        onSetCurrentDate = {},
        onWeightUpdate = {},
      )
    }
  }

  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<AddTrophyViewModel>()
    val state by viewModel.state.collectAsState()
    val photoPicker = rememberPhotoPicker()

    val actions = Actions(
      onDateUpdate = { date -> viewModel.handleAction(DateUpdate(date)) },
      onEditTrophy = { id -> viewModel.handleAction(EditTrophy(id)) },
      onFishTypeUpdate = { type -> viewModel.handleAction(FishTypeUpdate(type)) },
      onImageDelete = { index -> viewModel.handleAction(DeleteImage(index)) },
      onLengthUpdate = { length -> viewModel.handleAction(LengthUpdate(length)) },
      onLocationUpdate = { location -> viewModel.handleAction(LocationUpdate(location)) },
      onNavigateBack = { navigator?.pop() },
      onNotesUpdate = { notes -> viewModel.handleAction(NotesUpdate(notes)) },
      onOpenCamera = {
        photoPicker.pickFromCamera { imageUri ->
          viewModel.handleAction(AddCameraImage(imageUri))
        }
      },
      onOpenGallery = {
        photoPicker.pickMultipleFromGallery { imageUris ->
          viewModel.handleAction(AddGalleryImages(imageUris))
        }
      },
      onSave = {
        viewModel.handleAction(Save)
        navigator?.pop()
      },
      onSetCurrentDate = { date -> viewModel.handleAction(SetCurrentDate(date)) },
      onWeightUpdate = { weight -> viewModel.handleAction(WeightUpdate(weight)) },
    )

    LaunchedEffect(trophyId) {
      trophyId
        ?.let { actions.onEditTrophy(it) }
        ?: run { actions.onSetCurrentDate(Clock.System.now().toEpochMilliseconds()) }
    }

    AddTrophyScreenContent(
      actions = actions,
      state = state,
    )
  }
}

@Composable
private fun AddTrophyScreenContent(
  actions: AddTrophyScreen.Actions,
  state: AddTrophyState,
) {
  Column(modifier = Modifier.fillMaxSize()) {
    AddTrophyScreenTopAppBar(
      onNavigateBackClick = actions.onNavigateBack,
    )
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      OutlinedTextField(
        value = state.input.fishType,
        onValueChange = actions.onFishTypeUpdate,
        label = { Text("Тип рыбы") },
        modifier = Modifier.fillMaxWidth()
      )
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = state.input.weight,
          onValueChange = actions.onWeightUpdate,
          label = { Text("Вес (кг)") },
          modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
          value = state.input.length,
          onValueChange = actions.onLengthUpdate,
          label = { Text("Длина (см)") },
          modifier = Modifier.weight(1f)
        )
      }

      OutlinedTextField(
        value = state.input.location,
        onValueChange = actions.onLocationUpdate,
        label = { Text("Место ловли") },
        modifier = Modifier.fillMaxWidth()
      )

      DatePickerField(
        value = state.input.date ?: Clock.System.now().toEpochMilliseconds(),
        onDateSelected = actions.onDateUpdate,
        label = "Дата",
        modifier = Modifier.fillMaxWidth()
      )

      // Photo picker section
      Text(
        text = "Фото трофея",
        style = MaterialTheme.typography.titleMedium
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Camera button
        Button(
          onClick = actions.onOpenCamera,
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Camera, contentDescription = "Camera")
          Spacer(modifier = Modifier.padding(4.dp))
          Text("Камера")
        }

        // Gallery button
        Button(
          onClick = actions.onOpenGallery,
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Photo, contentDescription = "Gallery")
          Spacer(modifier = Modifier.padding(4.dp))
          Text("Галерея")
        }
      }

      // Display selected images
      if (state.input.images.isNotEmpty()) {
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          itemsIndexed(state.input.images) { index, imageUri ->
            Box(
              modifier = Modifier
                .height(120.dp)
                .width(120.dp)
            ) {
              // Delete button
              IconButton(
                onClick = { actions.onImageDelete(index) },
                modifier = Modifier
                  .align(Alignment.TopEnd)
                  .zIndex(1f)
                  .padding(4.dp)
                  .size(24.dp),
                colors = IconButtonDefaults.iconButtonColors(
                  containerColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
                  contentColor = MaterialTheme.colorScheme.onSurface
                )
              ) {
                Icon(
                  Icons.Default.Close,
                  contentDescription = "Remove image",
                  modifier = Modifier.padding(4.dp)
                )
              }

              Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(8.dp)
              ) {
                Image(
                  painter = rememberAsyncImagePainter(imageUri),
                  contentDescription = "Selected trophy photo",
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )
              }
            }
          }
        }
      }

      OutlinedTextField(
        value = state.input.notes,
        onValueChange = actions.onNotesUpdate,
        label = { Text("Заметки") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3
      )

      Button(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        onClick = actions.onSave,
        enabled = state.input.fishType.isNotBlank(),
      ) {
        when (state.mode) {
          is AddTrophyMode.Edit -> Text("Сохранить изменения")
          AddTrophyMode.New -> Text("Добавить трофей")
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}
