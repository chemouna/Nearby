package com.mounacheikhna.nearby

import android.content.Context
import com.mounacheikhna.nearby.annotation.ApplicationContext
import com.tbruyelle.rxpermissions.RxPermissions
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
public class AppModule(val app: FoursquareApp) {

    @Provides @Singleton
    fun provideApp(): FoursquareApp = app

    @Provides @Singleton @ApplicationContext
    fun provideApplicationContext(): Context = app

    @Provides @Singleton
    fun provideRxPermissions(): RxPermissions = RxPermissions.getInstance(app)

}