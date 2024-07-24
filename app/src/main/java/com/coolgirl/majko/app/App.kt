package com.coolgirl.majko.app

import android.app.Application
import com.coolgirl.majko.di.apiModule
import com.coolgirl.majko.di.appModule
import com.coolgirl.majko.di.dataStoreModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(listOf(dataStoreModule, apiModule, appModule) )
        }
    }
}