package co.kluvaka.cmp.session

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

object StartSessionScreen : Screen {
  @Composable
  override fun Content() {
    Box(modifier = Modifier.fillMaxSize()) {
      Text(
        modifier = Modifier.align(Alignment.Center),
        text = "Конфигурация новой сессии",
      )
    }
  }
}