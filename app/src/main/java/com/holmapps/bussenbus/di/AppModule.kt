package com.holmapps.bussenbus.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Named("timeFormatter")
    fun providesTimeFormatter() : SimpleDateFormat {
        return SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    }
}