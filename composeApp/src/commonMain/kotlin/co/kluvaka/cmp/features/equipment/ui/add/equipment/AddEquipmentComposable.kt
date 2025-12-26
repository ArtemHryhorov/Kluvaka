package co.kluvaka.cmp.features.equipment.ui.add.equipment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddEquipmentTopBar(onClick: () -> Unit) {
  TopAppBar(
    windowInsets = WindowInsets(0, 0, 0, 0),
    title = {
      Text(
        text = "Новая Приблуда",
      )
    },
    navigationIcon = {
      IconButton(
        onClick = onClick,
        content = {
          Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = "Navigate back",
          )
        }
      )
    }
  )
}

@Composable
internal fun ImagesList(
  images: List<String>,
  onImageDelete: (Int) -> Unit,
) {
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp)
  ) {
    itemsIndexed(images) { index, imageUri ->
      Box(
        modifier = Modifier
          .height(120.dp)
          .width(120.dp)
      ) {
        // Delete button
        IconButton(
          onClick = { onImageDelete(index) },
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
            contentDescription = "Selected equipment photo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
        }
      }
    }
  }
}

@Composable
internal fun MediaSelection(
  onOpenCamera: () -> Unit,
  onOpenGallery: () -> Unit,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Button(
      onClick = onOpenCamera,
      modifier = Modifier.weight(1f)
    ) {
      Icon(Icons.Default.Camera, contentDescription = "Camera")
      Spacer(modifier = Modifier.padding(4.dp))
      Text("Камера")
    }
    Button(
      onClick = onOpenGallery,
      modifier = Modifier.weight(1f)
    ) {
      Icon(Icons.Default.Photo, contentDescription = "Gallery")
      Spacer(modifier = Modifier.padding(4.dp))
      Text("Галерея")
    }
  }
}

@Composable
internal fun SaveButton(
  enabled: Boolean,
  onClick: () -> Unit,
) {
  Box {
    Button(
      onClick = onClick,
      enabled = enabled,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .padding(bottom = 16.dp)
    ) {
      Text("Save")
    }
  }
}
