package com.example.filterphoto.utilities

import android.app.Application
import com.example.filterphoto.depInjection.repoModule
import com.example.filterphoto.depInjection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
@Suppress("unused")
class AppConfig : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin { androidContext(this@AppConfig)
        modules(listOf(repoModule, viewModelModule))}
    }

}