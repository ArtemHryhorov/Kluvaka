package co.kluvaka.cmp.features.sessions.ui.active

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEventType
import co.kluvaka.cmp.features.trophies.domain.rememberPhotoPicker
import coil3.compose.rememberAsyncImagePainter
import org.koin.compose.viewmodel.koinViewModel

class ActiveSessionScreen(private val sessionId: Int? = null) : Screen {
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<ActiveSessionViewModel>()
    val state by viewModel.state.collectAsState()
    val photoPicker = rememberPhotoPicker()

    LaunchedEffect(sessionId) {
      if (sessionId != null) {
        viewModel.getActiveSessionById(sessionId)
      } else {
        viewModel.getActiveSession()
      }
    }

    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text("Активная сессия") },
          navigationIcon = {
            IconButton(onClick = { navigator?.pop() }) {
              Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
            }
          },
          actions = {
            IconButton(onClick = { viewModel.showFinishSessionDialog() }) {
              Icon(Icons.Default.Done, contentDescription = "Finish")
            }
          },
          colors = TopAppBarDefaults.topAppBarColors()
        )
      },
      floatingActionButton = {
        FloatingActionButton(
          onClick = { viewModel.showEventTypeDialog() },
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
          Icon(Icons.Default.Add, contentDescription = "Новое событие")
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
          Spacer(modifier = Modifier.height(16.dp))
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
                  text = session.date,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = "Удочек: ${session.rods.size}",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(24.dp))
        }

        // Events list
        if (state.events.isNotEmpty()) {
          Text(
            text = "События",
            style = MaterialTheme.typography.titleLarge
          )
          Spacer(modifier = Modifier.height(16.dp))
          
          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
          ) {
            items(state.events.reversed()) { event ->
              EventCard(event = event)
            }
          }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Пока нет событий", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            }
        }
      }
    }

    if (state.showEventTypeDialog) {
      EventTypeDialog(
        onDismiss = { viewModel.hideEventTypeDialog() },
        onSelectEventType = { eventType -> viewModel.selectEventType(eventType) }
      )
    }

    if (state.showRodSelectionDialog) {
      RodSelectionDialog(
        onDismiss = { viewModel.hideRodSelectionDialog() },
        onSelectRod = { rodId -> viewModel.selectRod(rodId) },
        rodsCount = state.session?.rods?.size ?: 3
      )
    }

    if (state.showFishEventDialog) {
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

    if (state.showSpombEventDialog) {
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

    if (state.showFishLooseDialog) {
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

    if (state.showFinishSessionDialog) {
      FinishSessionDialog(
        onDismiss = { viewModel.hideFinishSessionDialog() },
        onFinish = { 
          viewModel.finishSessionWithNotes()
          navigator?.popUntilRoot()
        },
        notes = state.sessionNotes,
        onNotesChange = { viewModel.updateSessionNotes(it) }
      )
    }
  }
}

@Composable
fun EventCard(event: FishingSessionEvent) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Event type indicator
      Box(
        modifier = Modifier
          .size(12.dp)
          .clip(CircleShape)
          .background(
            when (event.type) {
              is FishingSessionEventType.Fish -> Color.Green
              is FishingSessionEventType.Loose -> Color.Red
              is FishingSessionEventType.Spomb -> Color.Yellow
            }
          )
      )
      Spacer(modifier = Modifier.width(12.dp))
      
      Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
              text = when (event.type) {
                is FishingSessionEventType.Fish -> "Карп"
                is FishingSessionEventType.Loose -> "Сход"
                is FishingSessionEventType.Spomb -> "Спомб"
              },
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = event.timestamp,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        when (event.type) {
          is FishingSessionEventType.Fish -> {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                event.weight?.let { weight ->
                  Text("Вес: ${weight} кг", style = MaterialTheme.typography.bodyMedium)
                }
                Text("Удочка: ${event.type.rodId}", style = MaterialTheme.typography.bodyMedium)
            }
          }
          is FishingSessionEventType.Loose -> {
            Text("Удочка: ${event.type.rodId}", style = MaterialTheme.typography.bodyMedium)
          }
          is FishingSessionEventType.Spomb -> {
            Text("Количество: ${event.type.count} шт", style = MaterialTheme.typography.bodyMedium)
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

        if (event.photos.isNotEmpty()) {
          Spacer(modifier = Modifier.height(8.dp))
          Image(
            painter = rememberAsyncImagePainter(event.photos.first()),
            contentDescription = "Event photo",
            modifier = Modifier
              .height(150.dp)
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
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
            contentDescription = "Remove",
            tint = Color.White,
            modifier = Modifier.background(Color.Black.copy(alpha=0.5f), CircleShape)
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
          contentDescription = "Add photo",
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
    title = { Text("Тип события") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
          modifier = Modifier.fillMaxWidth(),
          onClick = { onSelectEventType(FishingSessionEventType.Fish(1)) },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
          Text("Рыба")
        }
        Button(
          modifier = Modifier.fillMaxWidth(),
          onClick = { onSelectEventType(FishingSessionEventType.Spomb(1)) },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107), contentColor = Color.Black)
        ) {
          Text("Спомб")
        }
        Button(
          modifier = Modifier.fillMaxWidth(),
          onClick = { onSelectEventType(FishingSessionEventType.Loose(1)) },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Сход")
        }
      }
    },
    confirmButton = {},
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Отмена")
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
    title = { Text("Выберите удочку") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(rodsCount) { index ->
          Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSelectRod(index + 1) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
          ) {
            Text("Удочка #${index + 1}", color = Color.White)
          }
        }
      }
    },
    confirmButton = {},
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Отмена")
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
    title = { Text("Улов") },
    text = {
      Column {
        PhotoSelectionRow(photos, onAddPhoto, onRemovePhoto)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
          value = weight,
          onValueChange = onWeightChange,
          label = { Text("Вес (кг)") },
          modifier = Modifier.fillMaxWidth(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = notes,
          onValueChange = onNotesChange,
          label = { Text("Заметки") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 2
        )
      }
    },
    confirmButton = {
      Button(onClick = onAddEvent) {
        Text("Добавить")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Отмена")
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
    title = { Text("Кормление") },
    text = {
      Column {
        PhotoSelectionRow(photos, onAddPhoto, onRemovePhoto)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
          value = count,
          onValueChange = onCountChange,
          label = { Text("Количество") },
          modifier = Modifier.fillMaxWidth(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = notes,
          onValueChange = onNotesChange,
          label = { Text("Заметки") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 2
        )
      }
    },
    confirmButton = {
      Button(onClick = onAddEvent) {
        Text("Добавить")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Отмена")
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
    title = { Text("Сход") },
    text = {
      Column {
        PhotoSelectionRow(photos, onAddPhoto, onRemovePhoto)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
          value = notes,
          onValueChange = onNotesChange,
          label = { Text("Заметки") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3
        )
      }
    },
    confirmButton = {
      Button(onClick = onAddEvent) {
        Text("Добавить")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Отмена")
      }
    }
  )
}

@Composable
fun FinishSessionDialog(
  onDismiss: () -> Unit,
  onFinish: () -> Unit,
  notes: String,
  onNotesChange: (String) -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Завершить сессию") },
    text = {
      Column {
        Text("Вы можете добавить заметки и фото позже", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
          value = notes,
          onValueChange = onNotesChange,
          label = { Text("Заметки") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Photo placeholders
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          repeat(3) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onFinish,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
      ) {
        Text("Завершить")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Отмена")
      }
    }
  )
}