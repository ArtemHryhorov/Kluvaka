package co.kluvaka.cmp.features.trophies.ui.trophies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.common.ui.Dialog
import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.trophies.domain.model.Trophy
import co.kluvaka.cmp.features.trophies.ui.add.trophy.AddTrophyScreen
import co.kluvaka.cmp.features.trophies.ui.detail.TrophyDetailScreen
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesOperation.Actions.DeleteTrophyCancel
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesOperation.Actions.DeleteTrophyConfirm
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesOperation.Actions.DeleteTrophyRequest
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesOperation.Actions.FetchTrophies
import org.koin.compose.viewmodel.koinViewModel

object TrophiesScreen : Screen {

  data class Actions(
    val onAddTrophyClick: () -> Unit,
    val onDeleteCancel: () -> Unit,
    val onDeleteConfirm: (id: Int) -> Unit,
    val onDeleteRequest: (trophy: Trophy) -> Unit,
    val onTrophyClick: (id: Int) -> Unit,
  ) {
    companion object {
      val Empty = Actions(
        onAddTrophyClick = {},
        onDeleteCancel = {},
        onDeleteConfirm = {},
        onDeleteRequest = {},
        onTrophyClick = {},
      )
    }
  }

  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<TrophiesViewModel>()
    val state by viewModel.state.collectAsState()

    val actions = Actions(
      onAddTrophyClick = { navigator?.push(AddTrophyScreen()) },
      onDeleteCancel = { viewModel.handleAction(DeleteTrophyCancel) },
      onDeleteConfirm = { id -> viewModel.handleAction(DeleteTrophyConfirm(id)) },
      onDeleteRequest = { trophy -> viewModel.handleAction(DeleteTrophyRequest(trophy)) },
      onTrophyClick = { id -> navigator?.push(TrophyDetailScreen(id)) },
    )

    // TODO: Delete when migrated to flow
    LaunchedEffect(Unit) {
      viewModel.handleAction(FetchTrophies)
    }

    TrophiesScreenContent(
      state = state,
      actions = actions,
    )
  }
}

@Composable
private fun TrophiesScreenContent(
  state: TrophiesState,
  actions: TrophiesScreen.Actions,
) {
  (state.deleteConfirmationDialog as? DialogState.Shown<Trophy>)?.value?.let { trophy ->
    Dialog(
      title = "Удалить ${trophy.fishType} из Ваших трофеев?",
      cancelButtonText = "Отмена",
      confirmButtonText = "Да, удалить",
      onConfirmClick = { actions.onDeleteConfirm(trophy.id) },
      onDismissClick = actions.onDeleteCancel,
    )
  }

  Column(
    modifier = Modifier.fillMaxSize(),
  ) {
    TrophiesTopBar()
    Box(
      modifier = Modifier.fillMaxSize(),
    ) {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp)
      ) {
        item {
          Spacer(modifier = Modifier.height(8.dp))
        }
        items(
          items = state.trophies,
          key = { it.id }
        ) { trophy ->
          TrophyItem(
            trophy = trophy,
            onRemove = { actions.onDeleteRequest(trophy) },
            onClick = { actions.onTrophyClick(trophy.id) }
          )
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
        }
      }
      FloatingActionButton(
        modifier = Modifier
          .padding(all = 16.dp)
          .align(Alignment.BottomEnd)
          .zIndex(3f),
        onClick = actions.onAddTrophyClick,
      ) {
        Text(
          modifier = Modifier.padding(horizontal = 16.dp),
          text = "Добавить",
        )
      }
    }
  }
}
