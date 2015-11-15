package com.mounacheikhna.snipschallenge.api

import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query
import rx.Observable

public interface FoursquareApi {

    @GET("/v2/venues/search")
    fun searchVenues(@Query("ll") location: String): Observable<SearchVenuesResponse>

    @GET("/v2/venues/{venue_id}")
    fun venueDetails(@Path("venue_id") venueId: String): Observable<VenueDetailsResponse>

}