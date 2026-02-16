package co.kluvaka.cmp.features.sessions.ui.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import co.kluvaka.cmp.features.common.domain.DateFormatter
import co.kluvaka.cmp.features.common.ui.Dialog
import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.model.SessionMode
import co.kluvaka.cmp.features.sessions.domain.model.totalFishCount
import co.kluvaka.cmp.features.sessions.domain.model.totalFishWeight
import co.kluvaka.cmp.features.sessions.ui.sessions.ProgressMetric.FishCount
import co.kluvaka.cmp.features.sessions.ui.sessions.ProgressMetric.FishWeight
import co.kluvaka.cmp.features.sessions.ui.sessions.ProgressMetric.SessionsCount
import co.kluvaka.cmp.features.sessions.ui.session.SessionScreen
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Actions.DeleteSessionCancel
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Actions.DeleteSessionConfirm
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Actions.DeleteSessionRequest
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Actions.FetchSessions
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsOperation.Actions.ToggleProgressMetric
import co.kluvaka.cmp.features.sessions.ui.sessions.composable.SessionsEmptyState
import co.kluvaka.cmp.features.sessions.ui.sessions.composable.SessionsListContent
import co.kluvaka.cmp.features.sessions.ui.start.session.StartSessionScreen
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.cancel
import kluvaka.composeapp.generated.resources.delete_session_dialog_confirm
import kluvaka.composeapp.generated.resources.delete_session_dialog_title
import kluvaka.composeapp.generated.resources.sessions_progress_fish_count
import kluvaka.composeapp.generated.resources.sessions_progress_fish_weight
import kluvaka.composeapp.generated.resources.sessions_progress_label
import kluvaka.composeapp.generated.resources.sessions_topbar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

object SessionsScreen : Screen {

  data class Actions(
    val onActiveSessionClick: (Int?) -> Unit,
    val onCompletedSessionClick: (Int) -> Unit,
    val onDeleteCancel: () -> Unit,
    val onDeleteConfirm: (Int) -> Unit,
    val onDeleteRequest: (Session) -> Unit,
    val onStartNewSessionClick: () -> Unit,
    val onToggleProgressMetric: () -> Unit,
  ) {
    companion object Companion {
      val Empty = Actions(
        onActiveSessionClick = {},
        onCompletedSessionClick = {},
        onDeleteCancel = {},
        onDeleteConfirm = {},
        onDeleteRequest = {},
        onStartNewSessionClick = {},
        onToggleProgressMetric = {},
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
      onDeleteConfirm = { id -> viewModel.handleAction(DeleteSessionConfirm(id)) },
      onDeleteRequest = { session -> viewModel.handleAction(DeleteSessionRequest(session)) },
      onStartNewSessionClick = { navigator?.push(StartSessionScreen) },
      onToggleProgressMetric = { viewModel.handleAction(ToggleProgressMetric) }
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
    val formattedDate = DateFormatter.format(session.dateMillis)
    Dialog(
      title = stringResource(Res.string.delete_session_dialog_title),
      description = "\"${session.location}\" от $formattedDate",
      cancelButtonText = stringResource(Res.string.cancel),
      confirmButtonText = stringResource(Res.string.delete_session_dialog_confirm),
      onConfirmClick = { actions.onDeleteConfirm(session.id!!) },
      onDismissClick = actions.onDeleteCancel,
    )
  }

  Box(
    modifier = Modifier.fillMaxSize()
  ) {
    val totalFishCount = state.sessions.sumOf { it.events.totalFishCount() }
    val totalFishWeight = state.sessions.sumOf { it.events.totalFishWeight() }

    Column {
      SessionsHistoryTopBar(
        sessionsCount = state.sessions.size,
        progressMetric = state.progressMetric,
        fishCount = totalFishCount,
        fishWeight = totalFishWeight,
        onToggleMetric = { actions.onToggleProgressMetric() },
      )
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
private fun SessionsHistoryTopBar(
  sessionsCount: Int,
  progressMetric: ProgressMetric,
  fishCount: Int,
  fishWeight: Double,
  onToggleMetric: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    ProgressLabel(
      metric = progressMetric,
      sessionsCount = sessionsCount,
      fishCount = fishCount,
      fishWeight = fishWeight,
      onClick = onToggleMetric,
    )
    TopAppBar(
      windowInsets = WindowInsets(0, 0, 0, 0),
      title = {
        Text(
          modifier = Modifier.fillMaxWidth(),
          text = stringResource(Res.string.sessions_topbar),
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
          fontSize = 34.sp,
        )
      }
    )
  }
}

@Composable
private fun ProgressLabel(
  metric: ProgressMetric,
  sessionsCount: Int,
  fishCount: Int,
  fishWeight: Double,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val weightRounded = ((fishWeight * 10).toInt() / 10.0).toString()
  val labelText = when (metric) {
    SessionsCount -> stringResource(Res.string.sessions_progress_label, sessionsCount)
    FishCount -> stringResource(Res.string.sessions_progress_fish_count, fishCount)
    FishWeight -> stringResource(Res.string.sessions_progress_fish_weight, weightRounded)
  }
  Box(
    modifier = modifier
      .clickable { onClick() }
      .background(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
        shape = RoundedCornerShape(24.dp),
      )
      .padding(
        horizontal = 16.dp,
        vertical = 4.dp,
      )
  ) {
    Text(
      text = labelText,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurface,
    )
  }
}

