package co.kluvaka.cmp

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import co.kluvaka.cmp.features.auth.ui.profile.ProfileScreen
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsScreen
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsScreen
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesScreen
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.bottom_nav_equipments
import kluvaka.composeapp.generated.resources.bottom_nav_profile
import kluvaka.composeapp.generated.resources.bottom_nav_sessions
import kluvaka.composeapp.generated.resources.bottom_nav_trophies
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavigationBar(navigator: Navigator) {
  val current = navigator.lastItem
  val bottomNavItems = listOf(
    BottomNavItem(
      screen = SessionsScreen,
      label = stringResource(Res.string.bottom_nav_sessions),
      icon = painterResource(Res.drawable.bottom_nav_sessions),
    ),
    BottomNavItem(
      screen = TrophiesScreen,
      label = stringResource(Res.string.bottom_nav_trophies),
      icon = painterResource(Res.drawable.bottom_nav_trophies),
    ),
    BottomNavItem(
      screen = EquipmentsScreen,
      label = stringResource(Res.string.bottom_nav_equipments),
      icon = painterResource(Res.drawable.bottom_nav_equipments),
    ),
    BottomNavItem(
      screen = ProfileScreen,
      label = stringResource(Res.string.bottom_nav_profile),
      icon = painterResource(Res.drawable.bottom_nav_profile),
    ),
  )
  val rootScreens = listOf(
    SessionsScreen,
    TrophiesScreen,
    EquipmentsScreen,
    ProfileScreen,
  )

  if (current in rootScreens) {
    NavigationBar {
      bottomNavItems.forEach { item ->
        NavigationBarItem(
          icon = { Icon(item.icon, contentDescription = item.label) },
          label = { Text(item.label) },
          alwaysShowLabel = false,
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
