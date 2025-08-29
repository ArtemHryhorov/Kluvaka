package co.kluvaka.cmp

import android.app.Application
import co.kluvaka.cmp.di.androidModule
import co.kluvaka.cmp.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@MainApplication)
      modules(sharedModule, androidModule)
    }
  }
}