package co.kluvaka.cmp.features.sessions.ui.active

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEvent
import co.kluvaka.cmp.features.sessions.domain.model.FishingSessionEventType
import org.koin.compose.viewmodel.koinViewModel

class ActiveSessionScreen(private val sessionId: Int? = null) : Screen {
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<ActiveSessionViewModel>()
    val state by viewModel.state.collectAsState()

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
        onWeightChange = { viewModel.updateEventWeight(it) }
      )
    }

    if (state.showSpombEventDialog) {
      SpombEventDialog(
        onDismiss = { viewModel.hideSpombEventDialog() },
        onAddEvent = { viewModel.addSpombEvent() },
        count = state.newSpombCount,
        onCountChange = { viewModel.updateSpombCount(it) }
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
      }
    }
  }
}

@Composable
fun EventTypeDialog(
  onDismiss: () -> Unit,
  onSelectEventType: (FishingSessionEventType) -> Unit
) {
  androidx.compose.material3.AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Событие") },
    text = {
      Column {
        Button(
          onClick = { onSelectEventType(FishingSessionEventType.Fish(1)) },
          colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Green)
        ) {
          Text("Рыба", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
          onClick = { onSelectEventType(FishingSessionEventType.Spomb(1)) },
          colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Yellow)
        ) {
          Text("Спомб", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
          onClick = { onSelectEventType(FishingSessionEventType.Loose(1)) },
          colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
          Text("Сход", color = Color.White)
        }
      }
    },
    confirmButton = {},
    dismissButton = {
      Button(onClick = onDismiss) {
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
  androidx.compose.material3.AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Удочка") },
    text = {
      Column {
        repeat(rodsCount) { index ->
          Button(
            onClick = { onSelectRod(index + 1) },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Green)
          ) {
            Text("${index + 1}", color = Color.White)
          }
          if (index < rodsCount - 1) {
            Spacer(modifier = Modifier.height(8.dp))
          }
        }
      }
    },
    confirmButton = {},
    dismissButton = {
      Button(onClick = onDismiss) {
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
  onWeightChange: (String) -> Unit
) {
  androidx.compose.material3.AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Фото рыбы") },
    text = {
      Column {
        // Photo placeholders
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          repeat(3) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .background(Color.Gray.copy(alpha = 0.3f))
            ) {
              Text(
                text = "+",
                modifier = Modifier.align(Alignment.Center)
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
          value = weight,
          onValueChange = onWeightChange,
          label = { Text("Weight (kg)") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(onClick = onAddEvent) {
        Text("Добавить")
      }
    },
    dismissButton = {
      Button(onClick = onDismiss) {
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
  onCountChange: (String) -> Unit
) {
  androidx.compose.material3.AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Кол спомбов") },
    text = {
      OutlinedTextField(
        value = count,
        onValueChange = onCountChange,
        label = { Text("Количество") },
        modifier = Modifier.fillMaxWidth()
      )
    },
    confirmButton = {
      Button(onClick = onAddEvent) {
        Text("Добавить")
      }
    },
    dismissButton = {
      Button(onClick = onDismiss) {
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
  androidx.compose.material3.AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Закончить") },
    text = {
      Column {
        Text("Вы можете добавить заметки и фото позже")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
          value = notes,
          onValueChange = onNotesChange,
          label = { Text("Заметки") },
          modifier = Modifier.fillMaxWidth()
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
                .background(Color.Gray.copy(alpha = 0.3f))
            ) {
              Text(
                text = "+",
                modifier = Modifier.align(Alignment.Center)
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onFinish,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Red)
      ) {
        Text("Finish", color = Color.White)
      }
    },
    dismissButton = {
      Button(onClick = onDismiss) {
        Text("Отмена")
      }
    }
  )
}