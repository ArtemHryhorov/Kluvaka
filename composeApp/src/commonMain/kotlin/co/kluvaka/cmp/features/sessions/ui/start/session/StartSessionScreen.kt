@file:OptIn(ExperimentalMaterial3Api::class)

package co.kluvaka.cmp.features.sessions.ui.start.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.common.ui.DatePickerField
import co.kluvaka.cmp.features.sessions.domain.model.Rod
import co.kluvaka.cmp.features.sessions.domain.model.SessionMode
import co.kluvaka.cmp.features.sessions.ui.session.SessionScreen
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.navigate_back_icon_content_description
import kluvaka.composeapp.generated.resources.remove_rod_content_description
import kluvaka.composeapp.generated.resources.rod
import kluvaka.composeapp.generated.resources.rod_bite
import kluvaka.composeapp.generated.resources.rod_distance
import kluvaka.composeapp.generated.resources.start_session
import kluvaka.composeapp.generated.resources.start_session_add_rod
import kluvaka.composeapp.generated.resources.start_session_date
import kluvaka.composeapp.generated.resources.start_session_location
import kluvaka.composeapp.generated.resources.start_session_topbar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

object StartSessionScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<StartSessionViewModel>()
    val state by viewModel.state.collectAsState()

    Scaffold(
      topBar = {
        TopAppBar(
          windowInsets = WindowInsets(0, 0, 0, 0),
          title = {
            Text(
              text = stringResource(Res.string.start_session_topbar),
            )
          },
          navigationIcon = {
            IconButton(
              onClick = { navigator?.pop() },
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(Res.string.navigate_back_icon_content_description),
              )
            }
          },
          colors = TopAppBarDefaults.topAppBarColors()
        )
      }
    ) { paddingValues ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(top = paddingValues.calculateTopPadding())
          .padding(horizontal = 16.dp)
          .verticalScroll(rememberScrollState())
          .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        OutlinedTextField(
          value = state.location ?: "",
          onValueChange = viewModel::changeSessionLocation,
          label = {
            Text(
              text = stringResource(Res.string.start_session_location),
            )
          },
          modifier = Modifier.fillMaxWidth()
        )

        DatePickerField(
          value = state.date,
          onDateSelected = viewModel::changeSessionDate,
          label = stringResource(Res.string.start_session_date),
          modifier = Modifier.fillMaxWidth()
        )

        state.rods.forEach { rod ->
          RodCard(
            rod = rod,
            onBaitChange = { viewModel.changeRodBait(rod.order, it) },
            onDistanceChange = { viewModel.changeRodDistance(rod.order, it) },
            onRemoveRod = { viewModel.removeRod(rod.order) }
          )
        }

        Button(
          modifier = Modifier.fillMaxWidth(),
          onClick = { viewModel.addEmptyRod() }
        ) {
          Text(
            text = stringResource(Res.string.start_session_add_rod),
          )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp),
          onClick = {
            viewModel.saveSession()
            navigator?.replace(SessionScreen(mode = SessionMode.Active))
          },
          enabled = state.rods.isNotEmpty(),
        ) {
          Text(
            text = stringResource(Res.string.start_session),
          )
        }
      }
    }
  }
}

@Composable
fun RodCard(
  rod: Rod,
  onDistanceChange: (String) -> Unit,
  onBaitChange: (String) -> Unit,
  onRemoveRod: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = MaterialTheme.shapes.medium,
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = stringResource(Res.string.rod) + " #${rod.order}",
          style = MaterialTheme.typography.titleMedium
        )
        IconButton(
          onClick = { onRemoveRod() },
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(Res.string.remove_rod_content_description),
          )
        }
      }

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
          modifier = Modifier.weight(1f),
          value = if (rod.distance == 0) "" else rod.distance.toString(),
          onValueChange = onDistanceChange,
          label = {
            Text(
              text = stringResource(Res.string.rod_distance),
            )
          },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
          modifier = Modifier.weight(1f),
          value = rod.bait,
          onValueChange = onBaitChange,
          label = {
            Text(
              text = stringResource(Res.string.rod_bite),
            )
          }
        )
      }
    }
  }
}
