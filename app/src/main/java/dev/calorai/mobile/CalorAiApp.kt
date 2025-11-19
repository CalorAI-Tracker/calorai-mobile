package dev.calorai.mobile

import android.app.Application
import dev.calorai.mobile.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CalorAiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CalorAiApp)
            modules(appModule)
        }
    }
}
