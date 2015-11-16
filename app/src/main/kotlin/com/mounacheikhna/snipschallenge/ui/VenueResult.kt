package com.mounacheikhna.snipschallenge.ui

import com.mounacheikhna.snipschallenge.api.PhotoResponse
import com.mounacheikhna.snipschallenge.api.Venue

public data class VenueResult(
    val venue: Venue,
    val photos: PhotoResponse
)