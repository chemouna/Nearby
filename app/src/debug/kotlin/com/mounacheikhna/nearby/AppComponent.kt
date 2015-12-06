package com.mounacheikhna.nearby

import com.mounacheikhna.nearby.api.CoreApiModule
import com.mounacheikhna.nearby.api.DebugApiModule
import com.mounacheikhna.nearby.location.LocationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, CoreApiModule::class, DebugApiModule::class,
    LocationModule::class))
interface AppComponent: AppGraph, AppDependencies {
}
