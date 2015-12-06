package com.mounacheikhna.nearby.api

data class SearchVenuesResponse(
    val meta: Meta,
    val response: Response
)

data class Meta(
    val code: Int
)

data class Response(
    val venues: List<Venue>,
    val isConfident: Boolean
)