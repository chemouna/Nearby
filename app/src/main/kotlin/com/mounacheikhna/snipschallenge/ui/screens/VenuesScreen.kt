package com.mounacheikhna.snipschallenge.ui.screens

import com.mounacheikhna.snipschallenge.ui.VenueResult

interface VenuesScreen: PresenterScreen {
    fun onVenueFetchSuccess(venueResult: VenueResult)
    fun onVenueFetchError()

    fun onNewLocationUpdate()
}