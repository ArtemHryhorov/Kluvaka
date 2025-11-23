package co.kluvaka.cmp.features.trophies.ui.add.trophy

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.trophies.domain.model.Trophy
import co.kluvaka.cmp.features.trophies.domain.rememberPhotoPicker
import coil3.compose.rememberAsyncImagePainter
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

data class AddTrophyScreen(
  val trophy: Trophy? = null,
) : Screen {
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<AddTrophyViewModel>()
    val state by viewModel.state.collectAsState()
    val photoPicker = rememberPhotoPicker()

    // Set current date as default
    LaunchedEffect(Unit) {
      val now = kotlinx.datetime.Clock.System.now()
      val localDateTime = now.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
      val dateString = "${localDateTime.year}-${
        localDateTime.monthNumber.toString().padStart(2, '0')
      }-${localDateTime.dayOfMonth.toString().padStart(2, '0')}"
      viewModel.updateDate(dateString)
    }

    // Set AddTrophyMode
    LaunchedEffect(Unit) {
      val trophyMode: AddTrophyMode = trophy
        ?.let { AddTrophyMode.Edit(it) }
        ?: AddTrophyMode.New

      viewModel.setAddTrophyMode(trophyMode)
    }

    Column(modifier = Modifier.fillMaxSize()) {
      TopAppBar(
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = { Text("Добавить трофей") },
        navigationIcon = {
          IconButton(onClick = { navigator?.pop() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors()
      )
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        OutlinedTextField(
          value = state.trophyInput.fishType,
          onValueChange = viewModel::updateFishType,
          label = { Text("Тип рыбы") },
          modifier = Modifier.fillMaxWidth()
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = state.trophyInput.weight,
            onValueChange = viewModel::updateWeight,
            label = { Text("Вес (кг)") },
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = state.trophyInput.length,
            onValueChange = viewModel::updateLength,
            label = { Text("Длина (см)") },
            modifier = Modifier.weight(1f)
          )
        }

        OutlinedTextField(
          value = state.trophyInput.location,
          onValueChange = viewModel::updateLocation,
          label = { Text("Место ловли") },
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = state.trophyInput.date,
          onValueChange = viewModel::updateDate,
          label = { Text("Дата") },
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
            onClick = {
              photoPicker.pickFromCamera { imageUri ->
                imageUri?.let { viewModel.addImage(it) }
              }
            },
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.Camera, contentDescription = "Camera")
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Камера")
          }

          // Gallery button
          Button(
            onClick = {
              photoPicker.pickMultipleFromGallery { imageUris ->
                imageUris.forEach { viewModel.addImage(it) }
              }
            },
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.Photo, contentDescription = "Gallery")
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Галерея")
          }
        }

        // Display selected images
        if (state.trophyInput.images.isNotEmpty()) {
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            itemsIndexed(state.trophyInput.images) { index, imageUri ->
              Box(
                modifier = Modifier
                  .height(120.dp)
                  .width(120.dp)
              ) {
                // Delete button
                IconButton(
                  onClick = { viewModel.removeImage(index) },
                  modifier = Modifier
                    .align(Alignment.TopEnd)
                    .zIndex(1f)
                    .padding(4.dp)
                    .size(24.dp),
                  colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.5f),
                    contentColor = Color.White
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
          value = state.trophyInput.notes,
          onValueChange = viewModel::updateNotes,
          label = { Text("Заметки") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3
        )

        Button(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
          onClick = {
            viewModel.save()
            navigator?.pop()
          },
        ) {
          when (state.mode) {
            is AddTrophyMode.Edit -> Text("Сохранить изменения")
            AddTrophyMode.New -> Text("Добавить трофей")
          }
        }
      }
    }
  }
}
