package co.kluvaka.cmp

import androidx.compose.ui.window.ComposeUIViewController
import co.kluvaka.cmp.di.equipmentModule
import co.kluvaka.cmp.di.iosModule
import co.kluvaka.cmp.di.sessionModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
  startKoin {
    val featureModules = listOf(
      sessionModule,
      equipmentModule
    )
    modules(featureModules + iosModule)
  }
  return ComposeUIViewController { App() }
}
