package kg.tutorialapp.myweather

import android.app.Application
import kg.tutorialapp.myweather.di.dataModule
import kg.tutorialapp.myweather.di.vmModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(vmModule, dataModule))
        }
    }
}