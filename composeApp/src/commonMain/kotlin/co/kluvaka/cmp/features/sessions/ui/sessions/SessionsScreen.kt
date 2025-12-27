package co.kluvaka.cmp.features.sessions.ui.sessions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.common.ui.Dialog
import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.model.SessionMode
import co.kluvaka.cmp.features.sessions.ui.active.SessionScreen
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Actions.DeleteSessionCancel
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Actions.DeleteSessionConfirm
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Actions.DeleteSessionRequest
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Actions.FetchSessions
import co.kluvaka.cmp.features.sessions.ui.sessions.composable.SessionsEmptyState
import co.kluvaka.cmp.features.sessions.ui.sessions.composable.SessionsListContent
import co.kluvaka.cmp.features.sessions.ui.start.session.StartSessionScreen
import org.koin.compose.viewmodel.koinViewModel

object SessionsScreen : Screen {

  data class Actions(
    val onActiveSessionClick: (Int?) -> Unit,
    val onCompletedSessionClick: (Int) -> Unit,
    val onDeleteCancel: () -> Unit,
    val onDeleteConfirm: (Int) -> Unit,
    val onDeleteRequest: (Session) -> Unit,
    val onStartNewSessionClick: () -> Unit,
  ) {
    companion object Companion {
      val Empty = Actions(
        onActiveSessionClick = {},
        onCompletedSessionClick = {},
        onDeleteCancel = {},
        onDeleteConfirm = {},
        onDeleteRequest = {},
        onStartNewSessionClick = {},
      )
    }
  }

  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<SessionsViewModel>()
    val state by viewModel.state.collectAsState()

    val actions = Actions(
      onActiveSessionClick = { id ->
        navigator?.push(SessionScreen(mode = SessionMode.Active, sessionId = id))
      },
      onCompletedSessionClick = { id ->
        navigator?.push(SessionScreen(mode = SessionMode.Completed, sessionId = id))
      },
      onDeleteCancel = { viewModel.handleAction(DeleteSessionCancel) },
      onDeleteConfirm = { id ->
        viewModel.handleAction(DeleteSessionConfirm(id))
      },
      onDeleteRequest = { session ->
        viewModel.handleAction(DeleteSessionRequest(session))
      },
      onStartNewSessionClick = { navigator?.push(StartSessionScreen) },
    )

    LaunchedEffect(Unit) {
      viewModel.handleAction(FetchSessions)
    }

    SessionsScreenContent(
      actions = actions,
      state = state,
    )
  }
}

@Composable
private fun SessionsScreenContent(
  actions: SessionsScreen.Actions,
  state: SessionsState,
) {
  (state.deleteConfirmationDialog as? DialogState.Shown<Session>)?.value?.let { session ->
    Dialog(
      title = "Удалить сессию?",
      description = "\"${session.location}\" от ${session.date}",
      cancelButtonText = "Отмена",
      confirmButtonText = "Да, удалить",
      onConfirmClick = { actions.onDeleteConfirm(session.id!!) },
      onDismissClick = actions.onDeleteCancel,
    )
  }

  Box(
    modifier = Modifier.fillMaxSize()
  ) {
    Column {
      SessionsHistoryTopBar()
      if (state.sessions.isNotEmpty()) {
        SessionsListContent(
          sessions = state.sessions,
          onSessionClick = { session ->
            if (session.isActive) {
              actions.onActiveSessionClick(session.id)
            } else {
              session.id?.let { sessionId ->
                actions.onCompletedSessionClick(sessionId)
              }
            }
          },
          onSessionDelete = actions.onDeleteRequest,
        )
      } else {
        SessionsEmptyState(modifier = Modifier.fillMaxWidth())
      }
    }
    FabMenu(
      modifier = Modifier.align(Alignment.BottomEnd),
      onClick = actions.onStartNewSessionClick,
    )
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
