package com.holmapps.bussenbus.application

import android.app.Application
import com.holmapps.bussenbus.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
// i is ded
    // i is computer talking
    //hoo is u?
    //oooh spooky computer ghost
}