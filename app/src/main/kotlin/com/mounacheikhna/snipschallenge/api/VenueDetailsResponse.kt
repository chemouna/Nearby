package com.mounacheikhna.snipschallenge.api

public data class VenueDetailsResponse(
    val meta: VenueMeta,
    val response: VenueResponse
)

public data class VenueMeta(
    val code: Int,
    val requestId: String
)

public data class VenueResponse(
    val venue: Venue
)

