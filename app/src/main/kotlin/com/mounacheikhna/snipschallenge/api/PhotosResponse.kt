package com.mounacheikhna.snipschallenge.api

public data class PhotoResponse(
    val count: Int,
    val items: Array<Photo>?
)

public data class Photo(
    val id: String,
    val prefix: String,
    val suffix: String
)
