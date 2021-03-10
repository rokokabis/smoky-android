package com.rokokabis.smoky

import android.app.Application
import com.rokokabis.smoky.di.networkModules
import com.rokokabis.smoky.di.repositoryModules
import com.rokokabis.smoky.di.useCaseModules
import com.rokokabis.smoky.di.viewModels
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@ObsoleteCoroutinesApi
class MediaCodecApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MediaCodecApp)
            modules(
                viewModels +
                        repositoryModules +
                        useCaseModules +
                        networkModules
            )
        }
        instance = this

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    companion object {
        lateinit var instance: MediaCodecApp
            private set
    }
}