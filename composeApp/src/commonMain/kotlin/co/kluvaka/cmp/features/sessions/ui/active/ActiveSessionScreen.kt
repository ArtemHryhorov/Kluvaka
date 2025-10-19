package co.kluvaka.cmp.features.sessions.ui.active

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
          title = { Text("Active Session") },
          navigationIcon = {
            IconButton(onClick = { navigator?.pop() }) {
              Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
            }
          },
          colors = TopAppBarDefaults.topAppBarColors()
        )
      },
      floatingActionButton = {
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FloatingActionButton(
            onClick = { viewModel.showAddEventDialog() },
            containerColor = Color.Green
          ) {
            Text("Новое событие", color = Color.White)
          }
          FloatingActionButton(
            onClick = { viewModel.showFinishSessionDialog() },
            containerColor = Color.Red
          ) {
            Text("Finish", color = Color.White)
          }
        }
      }
    ) { paddingValues ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .padding(16.dp)
      ) {
        // Session info
        state.session?.let { session ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text("Location: ${session.location}", fontWeight = FontWeight.Bold)
              Text("Date: ${session.date}")
              Text("Rods: ${session.rods.size}")
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
        }

        // Events list
        Text("События", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(state.events) { event ->
            EventCard(event = event)
          }
        }
      }
    }

    // Dialogs
    if (state.showAddEventDialog) {
      AddEventDialog(
        onDismiss = { viewModel.hideAddEventDialog() },
        onAddEvent = { viewModel.showEventTypeDialog() }
      )
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
      containerColor = when (event.type) {
        is FishingSessionEventType.Fish -> Color.Green.copy(alpha = 0.1f)
        is FishingSessionEventType.Loose -> Color.Red.copy(alpha = 0.1f)
        is FishingSessionEventType.Spomb -> Color.Yellow.copy(alpha = 0.1f)
      }
    )
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
        Text(
          text = when (event.type) {
            is FishingSessionEventType.Fish -> "Карп"
            is FishingSessionEventType.Loose -> "Сход"
            is FishingSessionEventType.Spomb -> "Спомб"
          },
          fontWeight = FontWeight.Bold
        )
        Text(
          text = event.timestamp,
          style = MaterialTheme.typography.bodySmall
        )
        when (event.type) {
          is FishingSessionEventType.Fish -> {
            event.weight?.let { weight ->
              Text("Weight: ${weight}kg")
            }
            Text("Rod: ${event.type.rodId}")
          }
          is FishingSessionEventType.Loose -> {
            Text("Rod: ${event.type.rodId}")
          }
          is FishingSessionEventType.Spomb -> {
            Text("Count: ${event.type.count} шт")
          }
        }
      }
    }
  }
}

@Composable
fun AddEventDialog(
  onDismiss: () -> Unit,
  onAddEvent: () -> Unit
) {
  androidx.compose.material3.AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Событие") },
    text = { Text("Выберите тип события") },
    confirmButton = {
      Button(onClick = onAddEvent) {
        Text("Продолжить")
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