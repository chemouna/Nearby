package com.mounacheikhna.snipschallenge

import com.mounacheikhna.snipschallenge.ui.venues.VenuesView

public interface AppGraph {
    fun inject(application: FoursquareApp)
    fun inject(venuesView: VenuesView)
}