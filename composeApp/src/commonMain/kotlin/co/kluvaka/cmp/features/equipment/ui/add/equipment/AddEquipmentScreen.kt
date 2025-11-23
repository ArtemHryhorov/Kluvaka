package co.kluvaka.cmp.features.equipment.ui.add.equipment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.equipment.domain.model.Equipment
import co.kluvaka.cmp.features.equipment.ui.add.equipment.composable.AddEquipmentTopBar
import co.kluvaka.cmp.features.trophies.domain.rememberPhotoPicker
import coil3.compose.rememberAsyncImagePainter
import org.koin.compose.viewmodel.koinViewModel

data class AddEquipmentScreen(
  private val equipment: Equipment? = null,
) : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<AddEquipmentViewModel>()
    val photoPicker = rememberPhotoPicker()
    val state by viewModel.state.collectAsState()

    // Set AddTrophyMode
    LaunchedEffect(Unit) {
      val mode: AddEquipmentMode = equipment
        ?.let { AddEquipmentMode.Edit(it) }
        ?: AddEquipmentMode.New

      viewModel.setAddEquipmentMode(mode)
    }

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
          value = state.input.title,
          onValueChange = viewModel::updateTitle,
          label = { Text("Name") },
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(12.dp))
        OutlinedTextField(
          value = state.input.price,
          onValueChange = viewModel::updatePrice,
          label = { Text("Price") },
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
          ),
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(12.dp))
        // Photo picker section
        Text(
          text = "Фото приблуды",
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
        if (state.input.images.isNotEmpty()) {
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp)
          ) {
            itemsIndexed(state.input.images) { index, imageUri ->
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
                    contentDescription = "Selected equipment photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                  )
                }
              }
            }
          }
        }
        Spacer(modifier = Modifier.weight(1f))
        Box {
          Button(
            onClick = {
              viewModel.save()
              navigator?.pop()
            },
            enabled = state.input.title.isNotBlank(),
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
