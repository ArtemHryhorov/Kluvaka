package co.kluvaka.cmp.features.sessions.ui.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.common.ui.Dialog
import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession
import co.kluvaka.cmp.features.sessions.domain.model.SessionMode
import co.kluvaka.cmp.features.sessions.ui.active.SessionScreen
import co.kluvaka.cmp.features.sessions.ui.history.composable.FishingSessionList
import co.kluvaka.cmp.features.sessions.ui.history.composable.SessionsEmptyState
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

    (state.deleteDialogState as? DialogState.Shown<FishingSession>)?.value?.let { session ->
      Dialog(
        title = "Удалить сессию?",
        description = "\"${session.location}\" от ${session.date}",
        cancelButtonText = "Отмена",
        confirmButtonText = "Да, удалить",
        onConfirmClick = {
          viewModel.deleteSession(session)
        },
        onDismissClick = { viewModel.hideDeleteDialog() }
      )
    }

    Box(
      modifier = Modifier.fillMaxSize()
    ) {
      Column {
        SessionsHistoryTopBar()
        if (state.sessions.isNotEmpty()) {
          FishingSessionList(
            sessions = state.sessions,
            onSessionClick = { session ->
              if (session.isActive) {
                navigator?.push(SessionScreen(mode = SessionMode.Active, sessionId = session.id))
              } else {
                session.id?.let { sessionId ->
                  navigator?.push(SessionScreen(mode = SessionMode.Completed, sessionId = sessionId))
                }
              }
            },
            onSessionDelete = { session ->
              viewModel.showDeleteDialog(session)
            }
          )
        } else {
          SessionsEmptyState(modifier = Modifier.fillMaxWidth())
        }
      }
      FloatingActionButton(
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(all = 16.dp),
        onClick = {
          if (state.anyActiveSession) {
            navigator?.push(SessionScreen(mode = SessionMode.Active))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionsHistoryTopBar() {
  TopAppBar(
    windowInsets = WindowInsets(0, 0, 0, 0),
    title = {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Журнал рыбалок",
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
      )
    }
  )
}
