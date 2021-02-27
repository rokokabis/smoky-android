package com.rokokabis.smoky

import android.app.Application
import timber.log.Timber

class MediaCodecApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}