package com.mounacheikhna.nearby

import com.mounacheikhna.nearby.ui.details.VenueDetailsView
import com.mounacheikhna.nearby.ui.venues.VenuesView

/**
 * Declares components for which dagger injects dependencies.
 */
public interface AppGraph {
    fun inject(application: FoursquareApp)
    fun inject(venuesView: VenuesView)
    fun inject(venueDetailsView: VenueDetailsView)
}