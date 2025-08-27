package co.kluvaka.cmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import co.kluvaka.cmp.equipment.ui.equipments.EquipmentsScreen
import co.kluvaka.cmp.sessions.SessionsHistoryScreen
import co.kluvaka.cmp.trophies.TrophiesScreen

@Composable
fun App() {
  MaterialTheme {
    Navigator(SessionsHistoryScreen) { navigator ->
      val bottomNavItems = listOf(
        BottomNavItem(SessionsHistoryScreen, "Sessions", Icons.Default.History),
        BottomNavItem(TrophiesScreen, "Trophies", Icons.Default.EmojiEvents),
        BottomNavItem(EquipmentsScreen, "Equipments", Icons.Default.Inventory),
      )
      Scaffold(
        bottomBar = {
          BottomNavigationBar(navigator, bottomNavItems)
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding) // ← This line is critical
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
  val icon: ImageVector,
)

@Composable
fun BottomNavigationBar(navigator: Navigator, items: List<BottomNavItem>) {
  NavigationBar {
    val current = navigator.lastItem

    items.forEach { item ->
      NavigationBarItem(
        icon = { Icon(item.icon, contentDescription = item.label) },
        label = { Text(item.label) },
        selected = current == item.screen,
        onClick = {
          if (current != item.screen) {
            navigator.push(item.screen)
          }
        }
      )
    }
  }
}