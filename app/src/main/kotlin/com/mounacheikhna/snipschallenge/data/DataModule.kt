package com.mounacheikhna.snipschallenge.data

import android.content.Context
import com.mounacheikhna.snipschallenge.annotation.ApplicationContext
import com.squareup.okhttp.OkHttpClient
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Singleton

@Module
public class DataModule {

    @Provides @Singleton
    fun providePicasso(@ApplicationContext context: Context, okHttpClient: OkHttpClient): Picasso {
        return Picasso.Builder(context).downloader(
            OkHttpDownloader(okHttpClient)).listener { picasso, uri, e ->
            Timber.e(e, "Failed to load image with url : %s", uri)
        }.build()
    }

}