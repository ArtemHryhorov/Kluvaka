package co.kluvaka.cmp.features.trophies.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import co.kluvaka.cmp.features.common.domain.DateFormatter
import co.kluvaka.cmp.features.trophies.domain.model.Trophy
import coil3.compose.rememberAsyncImagePainter
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.back
import kluvaka.composeapp.generated.resources.date_format
import kluvaka.composeapp.generated.resources.edit
import kluvaka.composeapp.generated.resources.length_format
import kluvaka.composeapp.generated.resources.location_format
import kluvaka.composeapp.generated.resources.notes_label
import kluvaka.composeapp.generated.resources.trophy_photo_content_description
import kluvaka.composeapp.generated.resources.trophy_photo_section
import kluvaka.composeapp.generated.resources.weight_format
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TrophyDetailsTopAppBar(
  title: String,
  onNavigateBack: () -> Unit,
  onNavigateToEdit: () -> Unit,
) {
  TopAppBar(
    windowInsets = WindowInsets(0, 0, 0, 0),
    title = { Text(title) },
    navigationIcon = {
      IconButton(
        onClick = onNavigateBack,
      ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.back))
      }
    },
    actions = {
      IconButton(
        onClick = onNavigateToEdit,
      ) {
        Icon(Icons.Filled.Edit, contentDescription = stringResource(Res.string.edit))
      }
    },
    colors = TopAppBarDefaults.topAppBarColors()
  )
}

@Composable
internal fun MediaSection(
  images: List<String>,
  onImageClick: (Int) -> Unit,
) {
  Spacer(modifier = Modifier.padding(8.dp))
  Text(
    text = stringResource(Res.string.trophy_photo_section),
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
            painter = rememberAsyncImagePainter(images[index]),
            contentDescription = stringResource(Res.string.trophy_photo_content_description),
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

@Composable
internal fun TrophyDataColumn(
  trophy: Trophy,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.padding(horizontal = 16.dp)
  ) {
    trophy.weight?.let { weight ->
      Text(
        text = stringResource(Res.string.weight_format, weight),
        style = MaterialTheme.typography.bodyMedium
      )
    }
    trophy.length?.let { length ->
      Text(
        text = stringResource(Res.string.length_format, length),
        style = MaterialTheme.typography.bodyMedium
      )
    }
    trophy.location?.let { location ->
      Text(
        text = stringResource(Res.string.location_format, location),
        style = MaterialTheme.typography.bodyMedium
      )
    }
    trophy.date?.let { date ->
      Text(
        text = stringResource(Res.string.date_format, DateFormatter.format(date)),
        style = MaterialTheme.typography.bodyMedium
      )
    }
    trophy.notes?.let { notes ->
      Spacer(modifier = Modifier.padding(8.dp))
      Text(
        text = stringResource(Res.string.notes_label),
        style = MaterialTheme.typography.titleMedium
      )
      Text(
        text = notes,
        style = MaterialTheme.typography.bodyMedium
      )
    }
  }
}
