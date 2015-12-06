package com.mounacheikhna.nearby.api

data class GetPhotosResponse(
    val meta: Meta,
    val response: PhotosResponse
) {
    fun getMainPhotoUrl(): String = getPhotoUrlByType("300x300")
    fun getBestQualityPhotoUrl(): String = getPhotoUrlByType("500x500")

    fun getPhotoUrlByType(type: String): String {
        val photos = response?.photos?.items
        if (photos != null && photos.size > 0) {
            return "${photos[0].prefix}$type${photos[0].suffix}"
        }
        return "";
    }
}

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

