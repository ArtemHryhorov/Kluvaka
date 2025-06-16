package co.kluvaka.cmp.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.session.SessionsHistoryScreen
import co.kluvaka.cmp.session.StartSessionScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

object HomeScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(all = 24.dp),
    ) {
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
      ) {
        Box(
          modifier = Modifier
            .align(Alignment.Center)
            .size(156.dp)
            .clip(CircleShape)
            .background(Color.Red)
            .clickable {
              navigator?.push(StartSessionScreen)
            },
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Начать\nрыбалку",
            color = Color.White,
          )
        }
      }
      Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { navigator?.push(SessionsHistoryScreen) }
      ) {
        Text(
          text = "История рыбалок",
        )
      }
    }
  }
}

@Composable
@Preview
internal fun PreviewHomeScreen() {
  HomeScreen.Content()
}