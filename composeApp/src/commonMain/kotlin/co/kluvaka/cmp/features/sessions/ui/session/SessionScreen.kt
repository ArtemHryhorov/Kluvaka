package co.kluvaka.cmp.features.sessions.ui.session

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.common.ui.Dialog
import co.kluvaka.cmp.features.common.domain.DateFormatter
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEventType
import co.kluvaka.cmp.features.sessions.domain.model.SessionMode
import co.kluvaka.cmp.features.sessions.domain.model.totalFishCount
import co.kluvaka.cmp.features.sessions.domain.model.totalFishWeight
import co.kluvaka.cmp.features.sessions.ui.event.DetailedSessionEventScreen
import co.kluvaka.cmp.features.trophies.domain.rememberPhotoPicker
import coil3.compose.rememberAsyncImagePainter
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.active_session
import kluvaka.composeapp.generated.resources.add
import kluvaka.composeapp.generated.resources.add_photo_content_description
import kluvaka.composeapp.generated.resources.cancel
import kluvaka.composeapp.generated.resources.caught
import kluvaka.composeapp.generated.resources.choose_rod
import kluvaka.composeapp.generated.resources.count
import kluvaka.composeapp.generated.resources.count_label
import kluvaka.composeapp.generated.resources.event_type
import kluvaka.composeapp.generated.resources.event_type_fish
import kluvaka.composeapp.generated.resources.event_type_loose
import kluvaka.composeapp.generated.resources.event_type_media_content_description
import kluvaka.composeapp.generated.resources.event_type_spomb
import kluvaka.composeapp.generated.resources.events
import kluvaka.composeapp.generated.resources.finish_session_content_description
import kluvaka.composeapp.generated.resources.finish_session_dialog_confirm
import kluvaka.composeapp.generated.resources.finish_session_dialog_title
import kluvaka.composeapp.generated.resources.fish_caught
import kluvaka.composeapp.generated.resources.kilogram
import kluvaka.composeapp.generated.resources.loose_event_dialog_title
import kluvaka.composeapp.generated.resources.navigate_back_icon_content_description
import kluvaka.composeapp.generated.resources.new_event
import kluvaka.composeapp.generated.resources.notes
import kluvaka.composeapp.generated.resources.peaces
import kluvaka.composeapp.generated.resources.remove_image_content_description
import kluvaka.composeapp.generated.resources.rod
import kluvaka.composeapp.generated.resources.rods
import kluvaka.composeapp.generated.resources.session
import kluvaka.composeapp.generated.resources.session_empty_state
import kluvaka.composeapp.generated.resources.spomb_event_dialog_title
import kluvaka.composeapp.generated.resources.weight
import kluvaka.composeapp.generated.resources.weight_kg
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

class SessionScreen(
  private val mode: SessionMode,
  private val sessionId: Int? = null,
) : Screen {
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<SessionViewModel>()
    val state by viewModel.state.collectAsState()
    val photoPicker = rememberPhotoPicker()
    val isActiveMode = state.mode == SessionMode.Active

    LaunchedEffect(mode, sessionId) {
      viewModel.loadSession(mode, sessionId)
    }

    Scaffold(
      topBar = {
        TopAppBar(
          windowInsets = WindowInsets(0, 0, 0, 0),
          title = {
            Text(
              text = if (isActiveMode) {
                stringResource(Res.string.active_session)
              } else {
                stringResource(Res.string.session)
              }
            )
          },
          navigationIcon = {
            IconButton(
              onClick = { navigator?.pop() },
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = stringResource(Res.string.navigate_back_icon_content_description),
              )
            }
          },
          actions = {
            if (isActiveMode) {
              IconButton(
                onClick = { viewModel.showFinishSessionDialog() },
              ) {
                Icon(
                  imageVector = Icons.Default.Done,
                  contentDescription = stringResource(Res.string.finish_session_content_description),
                )
              }
            }
          },
          colors = TopAppBarDefaults.topAppBarColors()
        )
      },
      floatingActionButton = {
        if (isActiveMode) {
          FloatingActionButton(
            onClick = { viewModel.showEventTypeDialog() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = stringResource(Res.string.new_event),
            )
          }
        }
      }
    ) { paddingValues ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .padding(horizontal = 16.dp)
      ) {
        // Session info
        state.session?.let { session ->
          val formattedDate = DateFormatter.format(session.dateMillis)
          Spacer(modifier = Modifier.height(16.dp))
          val fishCount = state.events.totalFishCount()
          val fishWeight = state.events.totalFishWeight()
          Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
          ) {
            Column(
              modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                text = session.location,
                style = MaterialTheme.typography.headlineSmall
              )
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                text = formattedDate,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = stringResource(Res.string.rods) + "${session.rods.size}",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              val countMetric = stringResource(Res.string.peaces)
              val weightMetric = stringResource(Res.string.kilogram)
              val sessionsStatisticText = when {
                fishCount > 0 && fishWeight > 0 -> "$fishCount $countMetric · $fishWeight $weightMetric"
                fishCount > 0 -> "$fishCount $countMetric"
                fishWeight > 0 -> "$fishWeight $weightMetric"
                else -> null
              }
              sessionsStatisticText?.let { text ->
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = stringResource(Res.string.fish_caught),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                  )
                }
              }
            }
          }
          Spacer(modifier = Modifier.height(24.dp))
        }

        // Events list
        if (state.events.isNotEmpty()) {
          Text(
            text = stringResource(Res.string.events),
            style = MaterialTheme.typography.titleLarge
          )
          Spacer(modifier = Modifier.height(16.dp))

          val events = state.events.reversed()
          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
          ) {
            items(events, key = { it.id }) { event ->
              val currentSessionId = state.session?.id
              EventCard(
                event = event,
                onClick = {
                  currentSessionId?.let { sessionId ->
                    navigator?.push(DetailedSessionEventScreen(sessionId, event.id))
                  }
                },
              )
            }
          }
        } else {
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = stringResource(Res.string.session_empty_state),
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }
    }

    if (isActiveMode && state.showEventTypeDialog) {
      EventTypeDialog(
        onDismiss = { viewModel.hideEventTypeDialog() },
        onSelectEventType = { eventType -> viewModel.selectEventType(eventType) }
      )
    }

    if (isActiveMode && state.showRodSelectionDialog) {
      RodSelectionDialog(
        onDismiss = { viewModel.hideRodSelectionDialog() },
        onSelectRod = { rodId -> viewModel.selectRod(rodId) },
        rodsCount = state.session?.rods?.size ?: 3
      )
    }

    if (isActiveMode && state.showFishEventDialog) {
      FishEventDialog(
        onDismiss = { viewModel.hideFishEventDialog() },
        onAddEvent = { viewModel.addFishEvent() },
        weight = state.newEventWeight,
        onWeightChange = { viewModel.updateEventWeight(it) },
        notes = state.newEventNotes,
        onNotesChange = { viewModel.updateEventNotes(it) },
        photos = state.newEventPhotos,
        onAddPhoto = {
          photoPicker.pickMultipleFromGallery { uris ->
            uris.forEach { viewModel.addEventPhoto(it) }
          }
        },
        onRemovePhoto = { viewModel.removeEventPhoto(it) }
      )
    }

    if (isActiveMode && state.showSpombEventDialog) {
      SpombEventDialog(
        onDismiss = { viewModel.hideSpombEventDialog() },
        onAddEvent = { viewModel.addSpombEvent() },
        count = state.newSpombCount,
        onCountChange = { viewModel.updateSpombCount(it) },
        notes = state.newEventNotes,
        onNotesChange = { viewModel.updateEventNotes(it) },
        photos = state.newEventPhotos,
        onAddPhoto = {
          photoPicker.pickMultipleFromGallery { uris ->
            uris.forEach { viewModel.addEventPhoto(it) }
          }
        },
        onRemovePhoto = { viewModel.removeEventPhoto(it) }
      )
    }

    if (isActiveMode && state.showFishLooseDialog) {
      LooseEventDialog(
        onDismiss = { viewModel.hideFishLooseDialog() },
        onAddEvent = { viewModel.confirmFishLooseEvent() },
        notes = state.newEventNotes,
        onNotesChange = { viewModel.updateEventNotes(it) },
        photos = state.newEventPhotos,
        onAddPhoto = {
          photoPicker.pickMultipleFromGallery { uris ->
            uris.forEach { viewModel.addEventPhoto(it) }
          }
        },
        onRemovePhoto = { viewModel.removeEventPhoto(it) }
      )
    }

    if (isActiveMode && state.showFinishSessionDialog) {
      Dialog(
        title = stringResource(Res.string.finish_session_dialog_title),
        cancelButtonText = stringResource(Res.string.cancel),
        confirmButtonText = stringResource(Res.string.finish_session_dialog_confirm),
        onConfirmClick = {
          viewModel.finishSessionWithNotes()
          navigator?.popUntilRoot()
        },
        onDismissClick = { viewModel.hideFinishSessionDialog() },
      )
    }
  }
}

@Composable
fun EventCard(
  event: FishingSessionEvent,
  onClick: (() -> Unit)? = null,
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .let {
        if (onClick != null) {
          it.clickable { onClick() }
        } else {
          it
        }
      },
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      // Event type indicator
      Box(
        modifier = Modifier
          .size(12.dp)
          .clip(CircleShape)
          .background(
            when (event.type) {
              is FishingSessionEventType.Fish -> MaterialTheme.colorScheme.primary
              is FishingSessionEventType.Loose -> MaterialTheme.colorScheme.error
              is FishingSessionEventType.Spomb -> MaterialTheme.colorScheme.tertiary
            }
          )
      )
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = when (event.type) {
            is FishingSessionEventType.Fish -> stringResource(Res.string.event_type_fish)
            is FishingSessionEventType.Loose -> stringResource(Res.string.event_type_loose)
            is FishingSessionEventType.Spomb -> stringResource(Res.string.event_type_spomb)
          },
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        when (event.type) {
          is FishingSessionEventType.Fish -> {
            Row(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              event.weight?.let { weight ->
                Text(
                  text = stringResource(Res.string.weight)
                    + weight
                    + stringResource(Res.string.kilogram),
                  style = MaterialTheme.typography.bodyMedium,
                )
              }
              Text(
                text = stringResource(Res.string.rod) + "${event.type.rodId}",
                style = MaterialTheme.typography.bodyMedium,
              )
            }
          }

          is FishingSessionEventType.Loose -> {
            Text(
              text = stringResource(Res.string.rod) + "${event.type.rodId}",
              style = MaterialTheme.typography.bodyMedium,
            )
          }

          is FishingSessionEventType.Spomb -> {
            Text(
              text = stringResource(Res.string.count)
                + "${event.type.count}"
                + stringResource(Res.string.peaces),
              style = MaterialTheme.typography.bodyMedium,
            )
          }
        }

        if (!event.notes.isNullOrBlank()) {
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = event.notes,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
      Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End,
      ) {
        Text(
          text = event.timestamp,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (event.photos.isNotEmpty()) {
          Spacer(modifier = Modifier.height(8.dp))
          Image(
            painter = rememberAsyncImagePainter(event.photos.first()),
            contentDescription = stringResource(Res.string.event_type_media_content_description),
            modifier = Modifier
              .size(64.dp)
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
          )
        }
      }
    }
  }
}

@Composable
fun PhotoSelectionRow(
  photos: List<String>,
  onAddPhotoClick: () -> Unit,
  onRemovePhotoClick: (Int) -> Unit,
) {
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    itemsIndexed(photos) { index, photo ->
      Box(
        modifier = Modifier
          .size(80.dp)
      ) {
        Image(
          painter = rememberAsyncImagePainter(photo),
          contentDescription = null,
          modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp)),
          contentScale = ContentScale.Crop
        )
        IconButton(
          onClick = { onRemovePhotoClick(index) },
          modifier = Modifier
            .align(Alignment.TopEnd)
            .size(24.dp)
            .padding(4.dp)
        ) {
          Icon(
            Icons.Default.Close,
            contentDescription = stringResource(Res.string.remove_image_content_description),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.background(
              MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
              CircleShape
            )
          )
        }
      }
    }
    item {
      Box(
        modifier = Modifier
          .size(80.dp)
          .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp))
          .clip(RoundedCornerShape(8.dp))
          .clickable { onAddPhotoClick() },
        contentAlignment = Alignment.Center
      ) {
        Icon(
          Icons.Default.Add,
          contentDescription = stringResource(Res.string.add_photo_content_description),
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
fun EventTypeDialog(
  onDismiss: () -> Unit,
  onSelectEventType: (FishingSessionEventType) -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = stringResource(Res.string.event_type),
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
          modifier = Modifier.fillMaxWidth(),
          onClick = { onSelectEventType(FishingSessionEventType.Fish(1)) },
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
          ),
        ) {
          Text(
            text = stringResource(Res.string.event_type_fish),
          )
        }
        Button(
          modifier = Modifier.fillMaxWidth(),
          onClick = { onSelectEventType(FishingSessionEventType.Spomb(1)) },
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
          ),
        ) {
          Text(
            text = stringResource(Res.string.event_type_spomb),
          )
        }
        Button(
          modifier = Modifier.fillMaxWidth(),
          onClick = { onSelectEventType(FishingSessionEventType.Loose(1)) },
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
          ),
        ) {
          Text(
            text = stringResource(Res.string.event_type_loose),
          )
        }
      }
    },
    confirmButton = {},
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(
          text = stringResource(Res.string.cancel),
        )
      }
    }
  )
}

@Composable
fun RodSelectionDialog(
  onDismiss: () -> Unit,
  onSelectRod: (Int) -> Unit,
  rodsCount: Int
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = stringResource(Res.string.choose_rod),
      )
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(rodsCount) { index ->
          Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSelectRod(index + 1) },
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
            ),
          ) {
            Text(
              text = stringResource(Res.string.rod) + " #${index + 1}",
            )
          }
        }
      }
    },
    confirmButton = {},
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(
          text = stringResource(Res.string.cancel),
        )
      }
    }
  )
}

@Composable
fun FishEventDialog(
  onDismiss: () -> Unit,
  onAddEvent: () -> Unit,
  weight: String,
  onWeightChange: (String) -> Unit,
  notes: String,
  onNotesChange: (String) -> Unit,
  photos: List<String>,
  onAddPhoto: () -> Unit,
  onRemovePhoto: (Int) -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = stringResource(Res.string.caught),
      )
    },
    text = {
      Column {
        PhotoSelectionRow(photos, onAddPhoto, onRemovePhoto)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
          value = weight,
          onValueChange = onWeightChange,
          label = {
            Text(
              text = stringResource(Res.string.weight_kg),
            )
          },
          modifier = Modifier.fillMaxWidth(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = notes,
          onValueChange = onNotesChange,
          label = {
            Text(
              text = stringResource(Res.string.notes),
            )
          },
          modifier = Modifier.fillMaxWidth(),
          minLines = 2
        )
      }
    },
    confirmButton = {
      Button(onClick = onAddEvent) {
        Text(stringResource(Res.string.add))
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(stringResource(Res.string.cancel))
      }
    }
  )
}

@Composable
fun SpombEventDialog(
  onDismiss: () -> Unit,
  onAddEvent: () -> Unit,
  count: String,
  onCountChange: (String) -> Unit,
  notes: String,
  onNotesChange: (String) -> Unit,
  photos: List<String>,
  onAddPhoto: () -> Unit,
  onRemovePhoto: (Int) -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(stringResource(Res.string.spomb_event_dialog_title)) },
    text = {
      Column {
        PhotoSelectionRow(photos, onAddPhoto, onRemovePhoto)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
          value = count,
          onValueChange = onCountChange,
          label = { Text(stringResource(Res.string.count_label)) },
          modifier = Modifier.fillMaxWidth(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = notes,
          onValueChange = onNotesChange,
          label = { Text(stringResource(Res.string.notes)) },
          modifier = Modifier.fillMaxWidth(),
          minLines = 2
        )
      }
    },
    confirmButton = {
      Button(onClick = onAddEvent) {
        Text(stringResource(Res.string.add))
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(stringResource(Res.string.cancel))
      }
    }
  )
}

@Composable
fun LooseEventDialog(
  onDismiss: () -> Unit,
  onAddEvent: () -> Unit,
  notes: String,
  onNotesChange: (String) -> Unit,
  photos: List<String>,
  onAddPhoto: () -> Unit,
  onRemovePhoto: (Int) -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(stringResource(Res.string.loose_event_dialog_title)) },
    text = {
      Column {
        PhotoSelectionRow(photos, onAddPhoto, onRemovePhoto)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
          value = notes,
          onValueChange = onNotesChange,
          label = { Text(stringResource(Res.string.notes)) },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3
        )
      }
    },
    confirmButton = {
      Button(onClick = onAddEvent) {
        Text(stringResource(Res.string.add))
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(stringResource(Res.string.cancel))
      }
    }
  )
}
