package com.mounacheikhna.snipschallenge

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

public class FoursquareApp: Application() {

    companion object {
        lateinit public var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            //TODO: add crashkytics tree ?
        }

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        appComponent.inject(this)
    }

}