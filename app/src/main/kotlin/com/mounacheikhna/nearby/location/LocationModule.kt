package com.mounacheikhna.nearby.location

import android.content.Context
import com.mounacheikhna.nearby.annotation.ApplicationContext
import dagger.Module
import dagger.Provides
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import javax.inject.Singleton

@Module
public class LocationModule {

    @Provides @Singleton
    fun providesLocationProvider(@ApplicationContext context: Context): ReactiveLocationProvider {
        return ReactiveLocationProvider(context)
    }

}