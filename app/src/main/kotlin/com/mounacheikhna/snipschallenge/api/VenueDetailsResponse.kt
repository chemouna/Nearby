package com.mounacheikhna.snipschallenge.api

data class VenueDetailsResponse(
    val meta: VenueMeta,
    val response: VenueResponse
)

data class VenueMeta(
    val code: Int,
    val requestId: String
)

data class VenueResponse(
    val venue: Venue
)

