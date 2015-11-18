package com.mounacheikhna.snipschallenge

import com.mounacheikhna.snipschallenge.ui.venues.VenuesView

public interface FoursquareGraph {
    fun inject(application: FoursquareApp)
    fun inject(venuesView: VenuesView) //temp
}