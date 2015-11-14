package com.mounacheikhna.snipschallenge

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
public class AppModule(val app: FoursquareApp) {

    @Provides @Singleton
    fun provideApp(): FoursquareApp = app

    @Provides @Singleton
    fun provideApplicationContext(): Context = app



}