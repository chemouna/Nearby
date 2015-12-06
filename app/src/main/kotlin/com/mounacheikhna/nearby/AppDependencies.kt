package com.mounacheikhna.nearby

import android.content.Context
import com.mounacheikhna.nearby.annotation.ApplicationContext
import com.mounacheikhna.nearby.api.FoursquareApi
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