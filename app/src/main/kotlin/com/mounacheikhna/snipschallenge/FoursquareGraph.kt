package com.mounacheikhna.snipschallenge

import com.mounacheikhna.snipschallenge.ui.MainActivity
import com.mounacheikhna.snipschallenge.ui.views.VenuesView

public interface FoursquareGraph {

    fun inject(application: FoursquareApp)
    fun inject(mainActivity: MainActivity)
    fun inject(venuesView: VenuesView)

}