package co.kluvaka.cmp

import androidx.compose.ui.window.ComposeUIViewController
import co.kluvaka.cmp.di.authModule
import co.kluvaka.cmp.di.equipmentModule
import co.kluvaka.cmp.di.iosModule
import co.kluvaka.cmp.di.sessionModule
import co.kluvaka.cmp.di.trophyModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
  startKoin {
    // Load platform-specific module first (provides AuthRepository)
    modules(iosModule)
    // Then load feature modules that depend on platform-specific dependencies
    val featureModules = listOf(
      authModule,
      sessionModule,
      equipmentModule,
      trophyModule
    )
    modules(featureModules)
  }
  return ComposeUIViewController { App() }
}
