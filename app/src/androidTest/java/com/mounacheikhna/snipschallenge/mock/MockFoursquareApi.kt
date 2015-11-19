package com.mounacheikhna.snipschallenge.mock

import com.mounacheikhna.snipschallenge.api.*
import com.squareup.moshi.Moshi
import retrofit.http.Path
import retrofit.http.Query
import rx.Observable
import java.io.IOException


class MockFoursquareApi { /*(private val mMoshi: Moshi) : FoursquareApi {

    @Throws(IOException::class)
    override fun searchVenues(location: String): SearchVenuesResponse {
        if (MockSearchVenues.PLACE_NAME.equals(location, true)) {
            return MockSearchVenues.SEARCH_RESPONSE)
        }

        return SearchVenuesResponse(true,
            mMoshi.(ResourceReader(SEARCH_VENUES_FILE_NAME), Venues::class.java))
    }

    override fun venueDetails(@Path("venue_id") venueId: String): Observable<VenueDetailsResponse> {
        return null
    }

    override fun venuePhotos(@Path("venue_id") venueId: String,
                             @Query("limit") limit: Int): Observable<GetPhotosResponse> {
        return null
    }
}
*/

}