package co.kluvaka.cmp.features.sessions.ui.active

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
import org.koin.compose.viewmodel.koinViewModel

object ActiveSessionScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<ActiveSessionViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
      viewModel.getActiveSession()
    }

    Box(
      modifier = Modifier.fillMaxSize()
    ) {
      Text("Active Session")
      FloatingActionButton(
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(all = 16.dp),
        onClick = {
          viewModel.finishSession()
          navigator?.popUntilRoot()
        },
      ) {
        Text(
          modifier = Modifier
            .align(Alignment.Center)
            .padding(horizontal = 16.dp),
          text = "Закончить рыбалку",
        )
      }
    }
  }
}