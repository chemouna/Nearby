package com.mounacheikhna.snipschallenge.api

import retrofit.http.GET
import retrofit.http.Query

interface FoursquareApi {

    @GET("/v2/venues/search")
    fun searchVenues(@Query("client_id") clientId: String,
                     @Query("client_secret") clientSecret: String,
                     @Query("v") version: Int,
                     @Query("ll") location: String,
                     @Query("intent") intent: String,
                     @Query("limit") limit: Int): SearchVenuesResponse;
                     //maybe observable of SearchVenuesResponse ,

}