package co.kluvaka.cmp.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun ConfigureSystemBars(isDarkTheme: Boolean) {
    val view = LocalView.current
    val context = LocalContext.current
    
    SideEffect {
        val activity = context as? Activity ?: return@SideEffect
        val window = activity.window
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, view)
        
        // In dark theme: light system bars (white icons)
        // In light theme: dark system bars (black icons)
        windowInsetsController.isAppearanceLightStatusBars = !isDarkTheme
        windowInsetsController.isAppearanceLightNavigationBars = !isDarkTheme
        
        // Set system bar colors to transparent so content shows through
        window.statusBarColor = Color.Transparent.hashCode()
        window.navigationBarColor = Color.Transparent.hashCode()
    }
}

