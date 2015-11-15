package com.mounacheikhna.snipschallenge.api

import com.mounacheikhna.snipschallenge.annotation.ClientId
import com.mounacheikhna.snipschallenge.annotation.ClientSecret
import com.mounacheikhna.snipschallenge.annotation.FoursquareApiVersion
import com.mounacheikhna.snipschallenge.annotation.FoursquareType
import rx.Observable
import javax.inject.Inject

public class FoursquareManager {

    //@Inject lateinit var foursquareApi: FoursquareApi
    /*@Inject @ClientId lateinit var clientId: String
    @Inject @ClientSecret lateinit var clientSecret: String*/
    //@Inject @FoursquareApiVersion lateinit var foursquareApiVersion: String
    //@Inject @FoursquareType lateinit var foursquareType: String

    /*fun searchVenuesNear(latitude: Double, longitude: Double): Observable<List<Venue>> {
        return foursquareApi.searchVenues(/*clientId, clientSecret, foursquareApiVersion,
            foursquareType, */ "$latitude,$longitude").take(1).map { resp -> resp.venues }
    }

    fun getDetails(venueId: String): Observable<Venue> = foursquareApi.venueDetails(venueId)

*/
}