package com.mounacheikhna.snipschallenge

import com.mounacheikhna.snipschallenge.api.CoreApiModule
import com.mounacheikhna.snipschallenge.api.ReleaseApiModule
import com.mounacheikhna.snipschallenge.location.LocationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, CoreApiModule::class, ReleaseApiModule::class,
    LocationModule::class))
public interface AppComponent: FoursquareGraph {
}
