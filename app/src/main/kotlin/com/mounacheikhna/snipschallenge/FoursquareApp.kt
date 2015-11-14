package com.mounacheikhna.snipschallenge

import android.app.Application
import android.util.Log
import com.squareup.leakcanary.LeakCanary
import rx.plugins.DebugHook
import rx.plugins.DebugNotification
import rx.plugins.DebugNotificationListener
import rx.plugins.RxJavaPlugins
import timber.log.Timber

public class FoursquareApp : Application() {

    companion object {
        lateinit public var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        LeakCanary.install(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            RxJavaPlugins.getInstance().registerObservableExecutionHook(
                    DebugHook(LoggingListener()))
        } else {
            //TODO: add crashlytics tree ?
        }

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        appComponent.inject(this)
    }

    public class LoggingListener : DebugNotificationListener<Any>() {

        val TAG = "RxLog"
        override fun <T> onNext(n: DebugNotification<T>?): T? {
            Log.v(TAG, " onNext ${n?.observer} ${n?.value}")
            return super.onNext(n)
        }

        override fun <T> start(n: DebugNotification<T>?): Any? {
            var contextObject = n?.observer.toString()
            super.start(n)
            return contextObject
        }

        override fun complete(context: Any?) {
            super.complete(context)
        }

        override fun error(context: Any?, e: Throwable?) {
            super.error(context, e)
            Log.v(TAG, " error $context")
        }
    }

}