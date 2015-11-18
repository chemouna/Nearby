package com.mounacheikhna.snipschallenge.ui.venues

import com.mounacheikhna.snipschallenge.ui.VenueResult
import com.mounacheikhna.snipschallenge.ui.base.PresenterScreen

interface VenuesScreen : PresenterScreen {
    fun onVenueFetchSuccess(venueResult: VenueResult)
    fun onVenueFetchError()

    fun onNewLocationUpdate()
}