package com.kaushaltechnology.india.utils

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideNetworkHelper(application: Application): NetworkHelper {
        return NetworkHelper(application.applicationContext)
    }
}