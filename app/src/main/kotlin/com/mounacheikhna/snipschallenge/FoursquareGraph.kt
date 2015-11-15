package com.mounacheikhna.snipschallenge

import com.mounacheikhna.snipschallenge.ui.MainActivity
import com.mounacheikhna.snipschallenge.ui.NearbyVenuesView

public interface FoursquareGraph {

    fun inject(application: FoursquareApp)
    fun inject(mainActivity: MainActivity)
    fun inject(venuesView: NearbyVenuesView)

}