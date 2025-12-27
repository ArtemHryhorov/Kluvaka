package co.kluvaka.cmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsScreen
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsScreen
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesScreen
import co.kluvaka.cmp.theme.KluvakaTheme
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.bottom_nav_equipments
import kluvaka.composeapp.generated.resources.bottom_nav_sessions
import kluvaka.composeapp.generated.resources.bottom_nav_trophies
import org.jetbrains.compose.resources.painterResource

@Composable
fun App() {
  KluvakaTheme {
    Navigator(SessionsScreen) { navigator ->
      Scaffold(
        bottomBar = {
          BottomNavigationBar(navigator)
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
        ) {
          CurrentScreen()
        }
      }
    }
  }
}

data class BottomNavItem(
  val screen: Screen,
  val label: String,
  val icon: Painter,
)
