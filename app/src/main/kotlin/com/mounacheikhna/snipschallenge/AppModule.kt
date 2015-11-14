package com.mounacheikhna.snipschallenge

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
public class AppModule(val app: FoursquareApp) {

    @Provides
    @Singleton
    fun provideApp(): FoursquareApp {
        return app
    }

}