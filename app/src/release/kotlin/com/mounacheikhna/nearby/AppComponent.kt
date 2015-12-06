package com.mounacheikhna.nearby

import com.mounacheikhna.nearby.api.CoreApiModule
import com.mounacheikhna.nearby.api.ReleaseApiModule
import com.mounacheikhna.nearby.location.LocationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, CoreApiModule::class, ReleaseApiModule::class,
    LocationModule::class))
public interface AppComponent: FoursquareGraph {
}
