@file:OptIn(ExperimentalMaterial3Api::class)

package co.kluvaka.cmp.features.sessions.ui.start.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.sessions.domain.model.Rod
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

object StartSessionScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<StartSessionViewModel>()
    val state by viewModel.state.collectAsState()

    Column(
      modifier = Modifier.fillMaxSize()
    ) {
      StartSessionTopAppBar(
        onNavigateBackClick = { navigator?.pop() },
        onStartSessionClick = {
          viewModel.saveSession()
          navigator?.pop()
        },
      )
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 16.dp),
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            modifier = Modifier.padding(end = 16.dp),
            text = "Локация:",
          )
          TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.location,
            onValueChange = { viewModel.changeSessionLocation(it) },
          )
        }
        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            modifier = Modifier.padding(end = 16.dp),
            text = "Дата:",
          )
          TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.date,
            onValueChange = { viewModel.changeSessionDate(it) },
          )
        }
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.weight(1f),
        ) {
          items(
            items = state.rods,
            key = { it.order }
          ) { rod ->
            RodCard(
              rod = rod,
              onBaitChange = { newBait ->
                viewModel.changeRodBait(rod.order, newBait)
              },
              onDistanceChange = { newDistance ->
                viewModel.changeRodDistance(rod.order, newDistance)
              },
            )
          }
        }
        Button(
          modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
          onClick = { viewModel.addEmptyRod() }
        ) {
          Text(
            text = "Добавить удочку",
          )
        }
      }
    }
  }
}

@Composable
private fun StartSessionTopAppBar(
  onNavigateBackClick: () -> Unit,
  onStartSessionClick: () -> Unit,
) {
  TopAppBar(
    windowInsets = WindowInsets(0, 0, 0, 0),
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Начало сессии",
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
          onClick = onStartSessionClick,
        ) {
          Text(
            text = "Старт",
          )
        }
      }
    },
    navigationIcon = {
      IconButton(
        onClick = onNavigateBackClick,
        content = {
          Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = "Navigate back",
          )
        }
      )
    }
  )
}

@Composable
fun RodCard(
  rod: Rod,
  onDistanceChange: (String) -> Unit,
  onBaitChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(8.dp),
    shape = MaterialTheme.shapes.medium,
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = "Удочка #${rod.order}",
        style = MaterialTheme.typography.titleMedium
      )
      Spacer(modifier = Modifier.height(4.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Дистинция:",
          style = MaterialTheme.typography.bodyMedium
        )
        TextField(
          modifier = Modifier.fillMaxWidth(),
          value = rod.distance.toString(),
          onValueChange = onDistanceChange,
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Наживка:",
          style = MaterialTheme.typography.bodyMedium
        )
        TextField(
          modifier = Modifier.fillMaxWidth(),
          value = rod.bait,
          onValueChange = onBaitChange,
        )
      }
    }
  }
}

@Composable
@Preview
internal fun PreviewStartSessionScreen() {
  StartSessionScreen.Content()
}