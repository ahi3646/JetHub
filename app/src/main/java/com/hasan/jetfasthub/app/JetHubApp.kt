package com.hasan.jetfasthub.app

import android.app.Application
import com.hasan.jetfasthub.di.appModule
import com.hasan.jetfasthub.di.basicAuthViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class JetHubApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@JetHubApp)
            modules(
                appModule,
                basicAuthViewModelModule
            )
        }
    }

}