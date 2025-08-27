package co.kluvaka.cmp.sessions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

object SessionsHistoryScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current

    Box(
      modifier = Modifier.fillMaxSize()
    ) {
      Text(
        modifier = Modifier.align(Alignment.Center),
        text = "История рыбалок",
      )
      FloatingActionButton(
        onClick = {
          navigator?.push(StartSessionScreen)
        },
      ) {

      }
    }
  }
}