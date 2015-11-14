package com.mounacheikhna.snipschallenge

import com.mounacheikhna.snipschallenge.api.ApiModule
import com.mounacheikhna.snipschallenge.location.LocationModule
import com.mounacheikhna.snipschallenge.ui.MainActivity
import com.mounacheikhna.snipschallenge.ui.NearbyVenuesView
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ApiModule::class, LocationModule::class))
public interface AppComponent {

    fun inject(application: FoursquareApp)
    fun inject(mainActivity: MainActivity)
    fun inject(venuesView: NearbyVenuesView)

}
