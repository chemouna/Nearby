package com.mounacheikhna.snipschallenge.api

data class GetPhotosResponse(
    val meta: Meta,
    val response: PhotosResponse
)

data class PhotosResponse(
    val photos: Photos
)

data class Photos(
    val count: Int,
    val items: Array<Photo>?,
    val dupesRemoved: Int
)

data class Photo(
    val id: String,
    val prefix: String,
    val suffix: String
)

