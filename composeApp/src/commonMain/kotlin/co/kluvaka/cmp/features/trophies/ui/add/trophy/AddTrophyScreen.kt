package co.kluvaka.cmp.features.trophies.ui.add.trophy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.trophies.domain.rememberPhotoPicker
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter

object AddTrophyScreen : Screen {
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<AddTrophyViewModel>()
    val state by viewModel.state.collectAsState()
    val photoPicker = rememberPhotoPicker()

    LaunchedEffect(Unit) {
      // Set current date as default
      val now = kotlinx.datetime.Clock.System.now()
      val localDateTime = now.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
      val dateString = "${localDateTime.year}-${localDateTime.monthNumber.toString().padStart(2, '0')}-${localDateTime.dayOfMonth.toString().padStart(2, '0')}"
      viewModel.updateDate(dateString)
    }

    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text("Добавить трофей") },
          navigationIcon = {
            IconButton(onClick = { navigator?.pop() }) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
          },
          colors = TopAppBarDefaults.topAppBarColors()
        )
      }
    ) { paddingValues ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .padding(16.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        OutlinedTextField(
          value = state.fishType,
          onValueChange = viewModel::updateFishType,
          label = { Text("Тип рыбы") },
          modifier = Modifier.fillMaxWidth()
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = state.weight,
            onValueChange = viewModel::updateWeight,
            label = { Text("Вес (кг)") },
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = state.length,
            onValueChange = viewModel::updateLength,
            label = { Text("Длина (см)") },
            modifier = Modifier.weight(1f)
          )
        }

        OutlinedTextField(
          value = state.location,
          onValueChange = viewModel::updateLocation,
          label = { Text("Место ловли") },
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = state.date,
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
                imageUri?.let { viewModel.updateImage(it) }
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
              photoPicker.pickFromGallery { imageUri ->
                imageUri?.let { viewModel.updateImage(it) }
              }
            },
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.Photo, contentDescription = "Gallery")
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Галерея")
          }
        }
        
        // Display selected image
        state.image?.let { imageUri ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp)
              .clip(RoundedCornerShape(8.dp))
          ) {
            Box(
              modifier = Modifier.fillMaxSize(),
              contentAlignment = Alignment.Center
            ) {
              // Display the actual image
              Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Selected trophy photo",
                modifier = Modifier
                  .fillMaxSize()
                  .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
              )
            }
          }
        }

        OutlinedTextField(
          value = state.notes,
          onValueChange = viewModel::updateNotes,
          label = { Text("Заметки") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Button(
          onClick = {
            viewModel.addTrophy()
            navigator?.pop()
          },
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Добавить трофей")
        }
      }
    }
  }
}
