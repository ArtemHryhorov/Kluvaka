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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import co.kluvaka.cmp.features.auth.ui.SignInScreen
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsScreen
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsScreen
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesScreen
import co.kluvaka.cmp.theme.KluvakaTheme
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.bottom_nav_equipments
import kluvaka.composeapp.generated.resources.bottom_nav_sessions
import kluvaka.composeapp.generated.resources.bottom_nav_trophies
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun App() {
  KluvakaTheme {
    val authRepository: AuthRepository = koinInject()
    val authState by authRepository.observeAuthState().collectAsState(initial = null)
    
    // Use authState to determine initial screen instead of calling getCurrentUser() eagerly
    val initialScreen: Screen = remember(authState) {
      if (authState == null) SignInScreen else SessionsScreen
    }
    
    Navigator(initialScreen) { navigator ->
      // Reactively navigate based on auth state changes
      LaunchedEffect(authState) {
        val currentScreen = navigator.lastItem
        if (authState == null) {
          // User logged out or not authenticated - go to sign in
          if (currentScreen != SignInScreen) {
            navigator.popUntilRoot()
            navigator.replace(SignInScreen)
          }
        } else {
          // User authenticated - go to sessions
          if (currentScreen == SignInScreen) {
            navigator.popUntilRoot()
            navigator.replace(SessionsScreen)
          }
        }
      }
      
      // Only show bottom navigation if not on sign in screen
      val showBottomBar = navigator.lastItem != SignInScreen
      
      Scaffold(
        bottomBar = {
          if (showBottomBar) {
            BottomNavigationBar(navigator)
          }
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
