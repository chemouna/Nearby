package com.mounacheikhna.snipschallenge.api

public data class FoursquareResponse(
    val meta: Meta,
    val response: Response
)

public data class Meta(
    val code: Int
)

public data class Response(
    val venues: List<Venue>,
    val isConfident: Boolean
)