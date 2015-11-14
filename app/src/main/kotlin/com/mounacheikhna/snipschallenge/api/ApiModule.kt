package com.mounacheikhna.snipschallenge.api

import android.app.Application
import com.mounacheikhna.snipschallenge.annotation.ClientId
import com.mounacheikhna.snipschallenge.annotation.ClientSecret
import com.squareup.moshi.Moshi
import com.squareup.okhttp.OkHttpClient
import dagger.Module
import dagger.Provides
import retrofit.MoshiConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {

    private val FOURSQUARE_ENDPOINT = "https://api.foursquare.com/";
    private val FOURSQUARE_CLIENT_ID = "DTK233D1LP2FQGDZDEYQIDUAVEAUX54IJ54IKWH0FE4ZBUL0"
    private val FOURSQUARE_CLIENT_SECRET = "5KH33YETVNSEQ42MFJ5UYCCGCIRBX5NBGM5JYY2S2PC4RIR1"

    @Provides @Singleton @ClientId
    fun provideClientId(): String {
        return FOURSQUARE_CLIENT_ID;
    }

    @Provides @Singleton @ClientSecret
    fun provideClientSecret(): String {
        return FOURSQUARE_CLIENT_SECRET;
    }

    @Provides
    @Singleton
    fun provideFoursquareApi(retrofit: Retrofit): FoursquareApi {
        return retrofit.create(FoursquareApi::class.java)
    }

    @Provides @Singleton
    fun provideRetrofitBuilder(@Named("Api") apiClient: OkHttpClient, moshi: Moshi): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(FOURSQUARE_ENDPOINT).client(apiClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    //Maybe move this into a moshi module ?
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }


    @Provides
    @Named("Api")
    fun provideApiClient(client: OkHttpClient): OkHttpClient = client.clone()

    @Provides
    public fun provideOkHttpClient(app: Application): OkHttpClient {
        val client = OkHttpClient()
        return client
    }


}
