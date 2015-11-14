package com.mounacheikhna.snipschallenge.location

import android.content.Context
import android.location.LocationManager
import dagger.Module
import dagger.Provides
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import javax.inject.Singleton

@Module
class LocationModule {

   /* @Provides @Singleton
    fun provideLocationManager(context: Context): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }*/

   /* @ Provides @Singleton
    fun provideGoogleApiClient(context: Context): GoogleApiClient {
        return GoogleApiClient.Builder(context).addApi(LocationServices.API).addApi(Wearable.API).build()
    }*/

    @Provides @Singleton
    fun providesLocationProvider(context: Context): ReactiveLocationProvider {
        return ReactiveLocationProvider(context)
    }

}