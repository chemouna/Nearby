package com.mounacheikhna.snipschallenge

import com.mounacheikhna.snipschallenge.api.FoursquareApi
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions.RxPermissions
import pl.charmas.android.reactivelocation.ReactiveLocationProvider

public interface BaseComponent {
    fun provideFoursquareApi(): FoursquareApi
    fun providesLocationProvider(): ReactiveLocationProvider
    fun providePicasso(): Picasso
    fun provideRxPermissions(): RxPermissions
}