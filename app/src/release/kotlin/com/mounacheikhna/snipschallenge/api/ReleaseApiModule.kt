package com.mounacheikhna.snipschallenge.api

import com.facebook.stetho.okhttp.StethoInterceptor
import com.squareup.okhttp.Interceptor
import dagger.Module
import dagger.Provides
import org.threeten.bp.Clock
import javax.inject.Singleton

@Module
class ReleaseApiModule {

    /**
     * For release builds we don't want to log anything
     */
    @Provides @Singleton @NetworkInterceptors
    fun provideNetworkInterceptors(): List<Interceptor> {
        return arrayListOf()
    }

}