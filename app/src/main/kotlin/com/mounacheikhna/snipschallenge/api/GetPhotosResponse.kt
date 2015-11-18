package com.mounacheikhna.snipschallenge.api

public data class GetPhotosResponse(
    val meta: Meta,
    val response: PhotosResponse
)

public data class PhotosResponse(
    val photos: Photos
)

public data class Photos(
    val count: Int,
    val items: Array<Photo>?,
    val dupesRemoved: Int
)

public data class Photo(
    val id: String,
    val prefix: String,
    val suffix: String
)

