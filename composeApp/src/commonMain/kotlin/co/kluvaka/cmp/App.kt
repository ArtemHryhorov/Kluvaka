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
      val bottomNavItems = listOf(
        BottomNavItem(
          screen = SessionsScreen,
          label = "Sessions",
          icon = painterResource(Res.drawable.bottom_nav_sessions),
        ),
        BottomNavItem(
          screen = TrophiesScreen,
          label = "Trophies",
          icon = painterResource(Res.drawable.bottom_nav_trophies),
        ),
        BottomNavItem(
          screen = EquipmentsScreen,
          label = "Equipments",
          icon = painterResource(Res.drawable.bottom_nav_equipments),
        ),
      )
      Scaffold(
        bottomBar = {
          BottomNavigationBar(navigator, bottomNavItems)
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

@Composable
fun BottomNavigationBar(navigator: Navigator, items: List<BottomNavItem>) {
  val current = navigator.lastItem
  val rootScreens = listOf(
    SessionsScreen,
    TrophiesScreen,
    EquipmentsScreen,
  )
  val isRootScreen = current in rootScreens

  if (isRootScreen) {
    NavigationBar {
      items.forEach { item ->
        NavigationBarItem(
          icon = { Icon(item.icon, contentDescription = item.label) },
          label = { Text(item.label) },
          selected = current == item.screen,
          onClick = {
            if (current != item.screen) {
              navigator.push(item.screen)
            }
          },
        )
      }
    }
  }
}