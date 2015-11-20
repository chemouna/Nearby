package com.mounacheikhna.snipschallenge.mock

import com.mounacheikhna.snipschallenge.api.*

interface MockFoursquareResponses {
    companion object {

        val BISTRO_DE_LA_PLACE_VENUE = Venue(
            "4fca14e1e4b098b3eefb6962",
            "La Bistro De La Place",
            null,
            VenueLocation(
                48.81497365439279,
                2.1285873513644904,
                60,
                "43 boulevard du roi",
                null,
                "Versailles",
                "Île-de-France",
                "78000",
                "France",
                arrayOf("43 boulevard du roi",
                    "78000 Versailles", "France")
            ),
            null, null, false, 2.0, "La Bistro De La Place de la loi.",
            Price(3, "Medium price")
        );
        val FOURSQUARE_VENUES = arrayOf(BISTRO_DE_LA_PLACE_VENUE)

        val PLACE_NAME = "Versailles"

        val SEARCH_VENUES_FILE_NAME = "search_venues.json"

        val SUCCESS_META = Meta(200)

        val SEARCH_RESPONSE = SearchVenuesResponse(SUCCESS_META,
            Response(FOURSQUARE_VENUES.toList(), true))

        val VENUE_SUCCESS_META = VenueMeta(200, "4fca14e1e4b098b3eefb6962")
        val VENUE_DETAIL_RESPONSE = VenueDetailsResponse(VENUE_SUCCESS_META,
            VenueResponse(BISTRO_DE_LA_PLACE_VENUE))

        //https://irs1.4sqi.net/img/general/width960/5858343_o2t48KiJEnBaXPE82GNBrunUYBrdk_B7ZjyJ_E20gEg.jpg
        var photo1 = Photo("4fca14e1e4b098b3eefb6962", "https://irs1.4sqi.net/img/",
            "5858343_o2t48KiJEnBaXPE82GNBrunUYBrdk_B7ZjyJ_E20gEg.jpg")

        val VENUE_PHOTOS_RESPONSE = GetPhotosResponse(SUCCESS_META,
            PhotosResponse(Photos(1, arrayOf(photo1), 0)))

    }
}
