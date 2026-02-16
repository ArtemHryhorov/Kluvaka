package co.kluvaka.cmp.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent

@Composable
actual fun ConfigureSystemBars(isDarkTheme: Boolean) {
    // iOS status bar styling is handled through Info.plist
    // or at the app delegate level, not dynamically per composable
}

