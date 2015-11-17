package com.mounacheikhna.snipschallenge.ui

import com.mounacheikhna.snipschallenge.api.GetPhotosResponse
import com.mounacheikhna.snipschallenge.api.Venue
import com.mounacheikhna.snipschallenge.api.Photo

public data class VenueResult(
    val venue: Venue,
    val getPhotosResponse: GetPhotosResponse?
) {

    fun getAllPhotos(): Array<Photo>? = getPhotosResponse?.response?.photos?.items
}