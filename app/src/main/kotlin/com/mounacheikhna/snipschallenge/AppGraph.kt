package com.mounacheikhna.snipschallenge

import com.mounacheikhna.snipschallenge.ui.VenueDetailsActivity2
import com.mounacheikhna.snipschallenge.ui.details.VenueDetailsView
import com.mounacheikhna.snipschallenge.ui.venues.VenuesView

/**
 * Declares components for which dagger injects dependencies.
 */
public interface AppGraph {
    fun inject(application: FoursquareApp)
    fun inject(venuesView: VenuesView)
    fun inject(venueDetailsView: VenueDetailsView)
}