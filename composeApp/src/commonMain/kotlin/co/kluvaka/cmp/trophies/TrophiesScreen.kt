package co.kluvaka.cmp.trophies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

object TrophiesScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current

    Box(modifier = Modifier.Companion.fillMaxSize()) {
      Text(
        modifier = Modifier.Companion.align(Alignment.Companion.Center),
        text = "Мои Трофеи",
      )
    }
  }
}