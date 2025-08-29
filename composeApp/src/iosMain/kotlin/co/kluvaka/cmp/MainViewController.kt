package co.kluvaka.cmp

import androidx.compose.ui.window.ComposeUIViewController
import co.kluvaka.cmp.di.iosModule
import co.kluvaka.cmp.di.sharedModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
  startKoin {
    modules(sharedModule, iosModule)
  }
  return ComposeUIViewController { App() }
}
