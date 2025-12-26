package co.kluvaka.cmp.features.equipment.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EquipmentDetailsTopBar(
  title: String,
  onNavigateBackClick: () -> Unit,
  onNavigateToAddEquipmentClick: () -> Unit,
) {
  TopAppBar(
    windowInsets = WindowInsets(0, 0, 0, 0),
    title = { Text(title) },
    navigationIcon = {
      IconButton(
        onClick = onNavigateBackClick,
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
        )
      }
    },
    actions = {
      IconButton(
        onClick = onNavigateToAddEquipmentClick,
      ) {
        Icon(
          imageVector = Icons.Filled.Edit,
          contentDescription = "Edit",
        )
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(),
  )
}

@Composable
internal fun EquipmentDetailsImagesSection(
  images: List<String>,
  onImageClick: (Int) -> Unit,
) {
  Spacer(modifier = Modifier.padding(8.dp))
  Text(
    text = "Фото приблуды:",
    modifier = Modifier.padding(horizontal = 16.dp),
    style = MaterialTheme.typography.titleMedium
  )
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    item {
      Spacer(modifier = Modifier.width(16.dp))
    }
    items(images.size) { index ->
      val image = images[index]
      Box(
        modifier = Modifier
          .height(200.dp)
          .width(300.dp),
      ) {
        Card(
          onClick = { onImageClick(index) },
          modifier = Modifier.fillMaxSize(),
          shape = RoundedCornerShape(8.dp)
        ) {
          Image(
            painter = rememberAsyncImagePainter(image),
            contentDescription = "Equipment photo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
          )
        }
      }
    }
    item {
      Spacer(modifier = Modifier.width(16.dp))
    }
  }
}
