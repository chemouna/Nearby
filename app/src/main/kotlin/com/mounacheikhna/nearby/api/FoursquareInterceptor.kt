package com.mounacheikhna.nearby.api

import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.Response

/**
 * Interceptor to add common values to all requests sent to foursquare
 */
class FoursquareInterceptor(val clientId: String, val clientSecret: String, val apiVersion: String, val apiType: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().httpUrl().newBuilder()
            .setQueryParameter("client_id", clientId)
            .setQueryParameter("client_secret", clientSecret)
            .setQueryParameter("v", apiVersion)
            .setQueryParameter("m", apiType)
            .build()
        val request = chain.request().newBuilder()
            .url(url)
            .build()
        return chain.proceed(request)
    }
}
