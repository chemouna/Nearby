package com.mounacheikhna.snipschallenge.api

import com.facebook.stetho.okhttp.StethoInterceptor
import com.mounacheikhna.snipschallenge.annotation.NetworkInterceptors
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import dagger.Module
import dagger.Provides
import org.threeten.bp.Clock
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class DebugApiModule {

    /**
     * We need logging and stetho interceptor only for debug builds
     * since we don't information about server api to be logged in release mode
     */
    @Provides @Singleton @NetworkInterceptors
    fun provideNetworkInterceptors(clock: Clock): List<out Interceptor> { //dagger needs 'out' here to generate the correct binding with '? extend'
        return arrayListOf(StethoInterceptor(), LoggingInterceptor(clock))
    }


}