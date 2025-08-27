package co.kluvaka.cmp

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
  startKoin {
    modules(sharedModule, iosModule)
  }
  return ComposeUIViewController { App() }
}
