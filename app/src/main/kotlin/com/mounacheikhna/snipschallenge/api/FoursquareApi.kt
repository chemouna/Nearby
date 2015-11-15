package com.mounacheikhna.snipschallenge.api

import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query
import rx.Observable

public interface FoursquareApi {

    @GET("/v2/venues/search")
    fun searchVenues(
                    /*@Query("client_id") clientId: String,
                     @Query("client_secret") clientSecret: String,
                     @Query("v") version: String,
                     @Query("m") type: String,*/
                     @Query("ll") location: String): Observable<SearchVenuesResponse>;

    //@Query("intent") intent: String, @Query("limit") limit: Int
    //maybe observable of SearchVenuesResponse ,git

    @GET("/v2/venues/{venue_id}")
    fun venueDetails(@Path("venue_id") venueId: String): Observable<Venue>

}