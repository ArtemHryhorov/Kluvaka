package co.kluvaka.cmp.features.sessions.ui.event

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.photos.ui.DetailedPhotoViewScreen
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEventType.Fish
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEventType.Loose
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEventType.Spomb
import coil3.compose.rememberAsyncImagePainter
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.back
import kluvaka.composeapp.generated.resources.count_label
import kluvaka.composeapp.generated.resources.count_value_format
import kluvaka.composeapp.generated.resources.event_details_title
import kluvaka.composeapp.generated.resources.event_not_found
import kluvaka.composeapp.generated.resources.event_photo_content_description
import kluvaka.composeapp.generated.resources.event_time_label
import kluvaka.composeapp.generated.resources.event_type_fish
import kluvaka.composeapp.generated.resources.event_type_label
import kluvaka.composeapp.generated.resources.event_type_loose
import kluvaka.composeapp.generated.resources.event_type_spomb
import kluvaka.composeapp.generated.resources.notes_label
import kluvaka.composeapp.generated.resources.photos_label
import kluvaka.composeapp.generated.resources.rod_label
import kluvaka.composeapp.generated.resources.weight_label
import kluvaka.composeapp.generated.resources.weight_value_format
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
class DetailedSessionEventScreen(
  private val sessionId: Int,
  private val eventId: Int,
) : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<DetailedSessionEventViewModel>(parameters = {
      parametersOf(sessionId, eventId)
    })
    val state by viewModel.state.collectAsState()

    Scaffold(
      topBar = {
        TopAppBar(
          windowInsets = WindowInsets(0, 0, 0, 0),
          title = { Text(stringResource(Res.string.event_details_title)) },
          navigationIcon = {
            androidx.compose.material3.IconButton(onClick = { navigator?.pop() }) {
              androidx.compose.material3.Icon(
                Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = stringResource(Res.string.back)
              )
            }
          },
          colors = TopAppBarDefaults.topAppBarColors()
        )
      }
    ) { paddingValues ->
      when {
        state.isLoading -> {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(paddingValues),
            contentAlignment = Alignment.Center
          ) {
            CircularProgressIndicator()
          }
        }

        state.errorMessage != null || state.event == null -> {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(paddingValues),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = state.errorMessage ?: stringResource(Res.string.event_not_found),
              style = MaterialTheme.typography.bodyLarge
            )
          }
        }

        else -> state.event?.let { event ->
          EventDetailsContent(
            modifier = Modifier.padding(paddingValues),
            sessionName = state.session?.location.orEmpty(),
            sessionDate = state.session?.date.orEmpty(),
            event = event,
            onPhotoClick = { index ->
              navigator?.push(
                DetailedPhotoViewScreen(
                  images = event.photos,
                  initialIndex = index
                )
              )
            }
          )
        }
      }
    }
  }
}

@Composable
private fun EventDetailsContent(
  modifier: Modifier = Modifier,
  sessionName: String,
  sessionDate: String,
  event: FishingSessionEvent,
  onPhotoClick: (Int) -> Unit = {},
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      )
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(sessionName, style = MaterialTheme.typography.titleMedium)
        Text(sessionDate, style = MaterialTheme.typography.bodyMedium)
        Text(
          text = stringResource(
            Res.string.event_type_label,
            when (event.type) {
              is Fish -> stringResource(Res.string.event_type_fish)
              is Loose -> stringResource(Res.string.event_type_loose)
              is Spomb -> stringResource(Res.string.event_type_spomb)
            }
          ),
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Medium
        )
        Text(
          text = stringResource(Res.string.event_time_label, event.timestamp),
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }

    when (event.type) {
      is Fish -> {
        InfoRow(label = stringResource(Res.string.rod_label), value = "#${event.type.rodId}")
        event.weight?.let { weight ->
          InfoRow(label = stringResource(Res.string.weight_label), value = stringResource(Res.string.weight_value_format, weight))
        }
      }

      is Loose -> {
        InfoRow(label = stringResource(Res.string.rod_label), value = "#${event.type.rodId}")
      }

      is Spomb -> {
        InfoRow(label = stringResource(Res.string.count_label), value = stringResource(Res.string.count_value_format, event.type.count))
      }
    }

    if (!event.notes.isNullOrBlank()) {
      Text(
        text = stringResource(Res.string.notes_label),
        style = MaterialTheme.typography.titleMedium
      )
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Text(
          text = event.notes!!,
          modifier = Modifier.padding(16.dp),
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }

    if (event.photos.isNotEmpty()) {
      Text(
        text = stringResource(Res.string.photos_label),
        style = MaterialTheme.typography.titleMedium
      )
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        itemsIndexed(event.photos) { index, photo ->
          Image(
            painter = rememberAsyncImagePainter(photo),
            contentDescription = stringResource(Res.string.event_photo_content_description),
            modifier = Modifier
              .size(160.dp)
              .clip(RoundedCornerShape(12.dp))
              .clickable { onPhotoClick(index) },
            contentScale = ContentScale.Crop
          )
        }
      }
    }
  }
}

@Composable
private fun InfoRow(label: String, value: String) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
  }
}


