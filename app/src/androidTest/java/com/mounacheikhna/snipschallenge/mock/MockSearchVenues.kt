package com.mounacheikhna.snipschallenge.mock

import com.mounacheikhna.snipschallenge.api.*

interface MockSearchVenues {
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
            null, null, false, 2.0, "La Bistro De La Place de la loi."
        );
        val FOURSQUARE_VENUES = arrayOf(BISTRO_DE_LA_PLACE_VENUE)

        val PLACE_NAME = "Versailles"

        val SEARCH_VENUES_FILE_NAME = "search_venues.json"

        val SUCCESS_META = Meta(200)

        val SEARCH_RESPONSE = SearchVenuesResponse(SUCCESS_META,
            Response(FOURSQUARE_VENUES.toList(), true))

    }
}
