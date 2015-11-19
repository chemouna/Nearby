package com.mounacheikhna.snipschallenge

import android.content.Context
import com.mounacheikhna.snipschallenge.annotation.ApplicationContext
import com.mounacheikhna.snipschallenge.api.FoursquareApi
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions.RxPermissions
import pl.charmas.android.reactivelocation.ReactiveLocationProvider

public interface AppDependencies {
    @ApplicationContext fun provideApplicationContext(): Context
    fun provideFoursquareApi(): FoursquareApi
    fun providesLocationProvider(): ReactiveLocationProvider
    fun providePicasso(): Picasso
    fun provideRxPermissions(): RxPermissions
}