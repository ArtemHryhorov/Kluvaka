package co.kluvaka.cmp.features.sessions.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession
import co.kluvaka.cmp.features.sessions.ui.start.session.StartSessionScreen
import org.koin.compose.viewmodel.koinViewModel

object SessionsHistoryScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<SessionsHistoryViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
      viewModel.getAllSessions()
    }

    Box(
      modifier = Modifier.fillMaxSize()
    ) {
      FishingSessionList(state.sessions)
      FloatingActionButton(
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(all = 16.dp),
        onClick = {
          navigator?.push(StartSessionScreen)
        },
      ) {
        Text(
          modifier = Modifier
            .align(Alignment.Center)
            .padding(horizontal = 16.dp),
          text = "Новая рыбалка",
        )
      }
    }
  }
}

@Composable
fun FishingSessionList(sessions: List<FishingSession>) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    items(
      items = sessions,
      key = { it.id ?: it.hashCode() }
    ) { session ->
      FishingSessionCard(session)
    }
  }
}

@Composable
fun FishingSessionCard(session: FishingSession) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = session.location,
        style = MaterialTheme.typography.titleMedium
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = session.date,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}