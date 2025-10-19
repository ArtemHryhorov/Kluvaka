package co.kluvaka.cmp.features.sessions.ui.completed

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

class CompletedSessionScreen(private val sessionId: Int) : Screen {
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<CompletedSessionViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(sessionId) {
      viewModel.loadSession(sessionId)
    }

    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text("Completed Session") },
          navigationIcon = {
            IconButton(onClick = { navigator?.pop() }) {
              Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
            }
          },
          colors = TopAppBarDefaults.topAppBarColors()
        )
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
              Text("Status: ${if (session.isActive) "Active" else "Completed"}")
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
