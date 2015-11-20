package com.mounacheikhna.snipschallenge.mock

import com.mounacheikhna.snipschallenge.api.*
import com.squareup.moshi.Moshi
import retrofit.http.Path
import retrofit.http.Query
import rx.Observable
import java.io.IOException


class MockFoursquareApi() : FoursquareApi {

    @Throws(IOException::class)
    override fun searchVenues(location: String): Observable<SearchVenuesResponse> {
        return Observable.from(arrayOf(MockFoursquareResponses.SEARCH_RESPONSE))
    }

    override fun venueDetails(venueId: String): Observable<VenueDetailsResponse> {
        return Observable.just(MockFoursquareResponses.VENUE_DETAIL_RESPONSE)
    }

    override fun venuePhotos(venueId: String, limit: Int): Observable<GetPhotosResponse> {
        return Observable.just(MockFoursquareResponses.VENUE_PHOTOS_RESPONSE)
    }

}