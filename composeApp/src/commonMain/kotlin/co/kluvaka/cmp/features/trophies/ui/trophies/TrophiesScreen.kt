package co.kluvaka.cmp.features.trophies.ui.trophies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.trophies.domain.model.Trophy
import co.kluvaka.cmp.features.trophies.ui.add.trophy.AddTrophyScreen
import co.kluvaka.cmp.features.trophies.ui.detail.TrophyDetailScreen
import org.koin.compose.viewmodel.koinViewModel

object TrophiesScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<TrophiesViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
      viewModel.fetchTrophies()
    }

    Box(Modifier.fillMaxSize()) {
      Column(modifier = Modifier.fillMaxSize()) {
        TrophiesTopBar()
        Box(
          modifier = Modifier.fillMaxSize(),
        ) {
          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
              .fillMaxSize()
              .padding(16.dp)
          ) {
            items(
              items = state.trophies,
              key = { it.id }
            ) { trophy ->
              TrophyItem(
                trophy = trophy,
                onRemove = { viewModel.delete(trophy.id) },
                onClick = { navigator?.push(TrophyDetailScreen(trophy.id)) }
              )
            }
          }
          FloatingActionButton(
            modifier = Modifier
              .padding(all = 16.dp)
              .align(Alignment.BottomEnd)
              .zIndex(3f),
            onClick = {
              navigator?.push(AddTrophyScreen)
            },
          ) {
            Text(
              modifier = Modifier.padding(horizontal = 16.dp),
              text = "Добавить рыбу",
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrophiesTopBar() {
  TopAppBar(
    windowInsets = WindowInsets(0, 0, 0, 0),
    title = {
      Text(
        text = "Мои Трофеи",
      )
    }
  )
}

@Composable
fun TrophyItem(
  trophy: Trophy,
  onRemove: (Trophy) -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
    confirmValueChange = {
      if (it == SwipeToDismissBoxValue.EndToStart) onRemove(trophy)
      it != SwipeToDismissBoxValue.StartToEnd
    }
  )

  SwipeToDismissBox(
    state = swipeToDismissBoxState,
    modifier = modifier.fillMaxSize(),
    enableDismissFromStartToEnd = false,
    backgroundContent = {
      when (swipeToDismissBoxState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Remove item",
            modifier = Modifier
              .fillMaxSize()
              .background(Color.Red)
              .wrapContentSize(Alignment.CenterEnd)
              .padding(12.dp),
            tint = Color.White
          )
        }
        SwipeToDismissBoxValue.StartToEnd -> {}
        SwipeToDismissBoxValue.Settled -> {}
      }
    }
  ) {
    TrophyCard(trophy, onClick)
  }
}

@Composable
private fun TrophyCard(
  trophy: Trophy,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    onClick = onClick
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(text = trophy.fishType, style = MaterialTheme.typography.titleMedium)
      Text(text = "Вес: ${trophy.weight} кг", style = MaterialTheme.typography.bodyMedium)
      trophy.length?.let { length ->
        Text(text = "Длина: $length см", style = MaterialTheme.typography.bodyMedium)
      }
      Text(text = "Место: ${trophy.location}", style = MaterialTheme.typography.bodyMedium)
      Text(text = "Дата: ${trophy.date}", style = MaterialTheme.typography.bodyMedium)
    }
  }
}