package com.mounacheikhna.nearby

import android.util.Log
import rx.plugins.DebugHook
import rx.plugins.DebugNotification
import rx.plugins.DebugNotificationListener
import rx.plugins.RxJavaPlugins

/**
 * Extends app to add debug build specific configurations.
 */
class DebugFoursquareApp: FoursquareApp() {

    override fun onCreate() {
        super.onCreate()
        //RxJava calls logging.
        RxJavaPlugins.getInstance().registerObservableExecutionHook(
            DebugHook(LoggingListener()))
    }

    public class LoggingListener : DebugNotificationListener<Any>() {
        val TAG = "RxLog"
        override fun <T> onNext(n: DebugNotification<T>?): T? {
            Log.v(TAG, " onNext ${n?.observer} ${n?.value}")
            return super.onNext(n)
        }

        override fun <T> start(n: DebugNotification<T>?): Any? {
            val contextObject = n?.observer.toString()
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