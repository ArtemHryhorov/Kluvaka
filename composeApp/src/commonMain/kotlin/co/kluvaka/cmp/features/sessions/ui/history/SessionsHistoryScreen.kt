package co.kluvaka.cmp.features.sessions.ui.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
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
import co.kluvaka.cmp.features.sessions.ui.active.ActiveSessionScreen
import co.kluvaka.cmp.features.sessions.ui.completed.CompletedSessionScreen
import co.kluvaka.cmp.features.sessions.ui.history.composable.FishingSessionList
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
      FishingSessionList(
        sessions = state.sessions,
        onSessionClick = { session ->
          if (session.isActive) {
            navigator?.push(ActiveSessionScreen(session.id))
          } else {
            session.id?.let { sessionId ->
              navigator?.push(CompletedSessionScreen(sessionId))
            }
          }
        }
      )
      FloatingActionButton(
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(all = 16.dp),
        onClick = {
          if (state.anyActiveSession) {
            navigator?.push(ActiveSessionScreen())
          } else {
            navigator?.push(StartSessionScreen)
          }
        },
      ) {
        Text(
          modifier = Modifier
            .align(Alignment.Center)
            .padding(horizontal = 16.dp),
          text = if (state.anyActiveSession) {
            "Открыть активную рыбалку"
          } else {
            "Новая рыбалка"
          },
        )
      }
    }
  }
}
