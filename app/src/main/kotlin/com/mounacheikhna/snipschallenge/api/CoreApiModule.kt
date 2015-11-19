package com.mounacheikhna.snipschallenge.api

import android.app.Application
import android.content.Context
import com.mounacheikhna.snipschallenge.BuildConfig
import com.mounacheikhna.snipschallenge.annotation.*
import com.squareup.moshi.Moshi
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import org.threeten.bp.Clock
import retrofit.MoshiConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton

@Module
public class CoreApiModule {

    private val FOURSQUARE_ENDPOINT_URL = "https://api.foursquare.com/";
    private val FOURSQUARE_API_VERSION: String = "20151114"
    private val FOURSQUARE_API_TYPE: String = "foursquare"

    @Provides @Singleton @ClientId
    fun provideClientId(): String = BuildConfig.FOURSQUARE_CLIENT_ID

    @Provides @Singleton @ClientSecret
    fun provideClientSecret(): String = BuildConfig.FOURSQUARE_CLIENT_SECRET

    @Provides @Singleton @FoursquareApiVersion
    fun provideFoursquareApiVersion(): String = FOURSQUARE_API_VERSION

    @Provides @Singleton @FoursquareType
    fun provideFoursquareType(): String = FOURSQUARE_API_TYPE

    @Provides @Singleton
    fun provideFoursquareInterceptor(@ClientId clientId: String,
                                     @ClientSecret clientSecret: String,
                                     @FoursquareApiVersion apiVersion: String,
                                     @FoursquareType apiType: String): FoursquareInterceptor {
        return FoursquareInterceptor(clientId, clientSecret, apiVersion, apiType)
    }

    @Provides @Singleton @Named("Api")
    fun provideApiClient(client: OkHttpClient,
                         @AppInterceptors interceptors: List<out Interceptor>,
                         @NetworkInterceptors networkInterceptors: List<out Interceptor>): OkHttpClient {
        var okClient = client.clone()
        okClient.interceptors().addAll(interceptors)
        okClient.networkInterceptors().addAll(networkInterceptors)
        return okClient
    }

    @Provides @Singleton @AppInterceptors
    fun provideAppInterceptors(
        foursquareInterceptor: FoursquareInterceptor): List<out Interceptor> {
        return arrayListOf(foursquareInterceptor);
    }

    @Provides @Singleton
    fun provideRetrofit(@Named("Api") apiClient: OkHttpClient,
                        moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .client(apiClient)
            .baseUrl(HttpUrl.parse(FOURSQUARE_ENDPOINT_URL))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
    }

    @Provides @Singleton
    fun provideFoursquareApi(retrofit: Retrofit): FoursquareApi {
        return retrofit.create(FoursquareApi::class.java)
    }

    @Provides @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides @Singleton
    public fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides @Singleton
    fun provideClock(): Clock = Clock.systemDefaultZone()

    @Provides @Singleton
    fun providePicasso(@ApplicationContext context: Context, client: OkHttpClient): Picasso {
        return Picasso.Builder(context).downloader(
            OkHttpDownloader(client)).listener { picasso, uri, e ->
            Timber.e(e, "Failed to load image: %s", uri)
        }.build()
    }

}
