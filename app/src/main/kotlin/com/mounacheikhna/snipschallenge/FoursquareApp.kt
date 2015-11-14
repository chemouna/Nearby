package com.mounacheikhna.snipschallenge

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class FoursquareApp: Application() {

    override fun onCreate() {
        super.onCreate()

        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            //TODO: add crashkytics tree ?
        }
    }

}