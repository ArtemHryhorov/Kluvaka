package co.kluvaka.cmp.features.equipment.ui.add.equipment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.equipment.ui.add.equipment.composable.AddEquipmentTopBar
import co.kluvaka.cmp.features.trophies.domain.rememberPhotoPicker
import coil3.compose.rememberAsyncImagePainter
import org.koin.compose.viewmodel.koinViewModel

object AddEquipmentScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<AddEquipmentViewModel>()
    val photoPicker = rememberPhotoPicker()
    val state by viewModel.state.collectAsState()
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .imePadding(), // adds bottom padding when keyboard is shown
      verticalArrangement = Arrangement.Top
    )  {
      AddEquipmentTopBar { navigator?.pop() }
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Name") },
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(12.dp))
        OutlinedTextField(
          value = price,
          onValueChange = { price = it },
          label = { Text("Price") },
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
          ),
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(12.dp))
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
        Spacer(modifier = Modifier.weight(1f))
        Box {
          Button(
            onClick = {
              if (title.isNotBlank() && price.isNotBlank()) {
                viewModel.addEquipment(
                  title = title,
                  price = price.toDouble(),
                )
                navigator?.pop()
              }
            },
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .fillMaxWidth()
              .padding(bottom = 16.dp)
          ) {
            Text("Save")
          }
        }
      }
    }
  }
}
