package co.kluvaka.cmp.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent

@Composable
actual fun ConfigureSystemBars(isDarkTheme: Boolean) {
    SideEffect {
        val statusBarStyle = if (isDarkTheme) {
            // Dark theme: light status bar (white icons)
            UIStatusBarStyleLightContent
        } else {
            // Light theme: dark status bar (black icons)
            UIStatusBarStyleDarkContent
        }
        
        UIApplication.sharedApplication.setStatusBarStyle(statusBarStyle, false)
    }
}

