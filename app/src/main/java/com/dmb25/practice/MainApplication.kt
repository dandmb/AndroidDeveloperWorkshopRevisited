package com.dmb25.practice

import android.app.Application
import com.dmb25.photogallerie.di.dataModule
import com.dmb25.photogallerie.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(presentationModule, dataModule)
        }

    }
}