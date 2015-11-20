package com.mounacheikhna.snipschallenge.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.View
import com.mounacheikhna.snipschallenge.api.Venue
import javax.inject.Inject

class NavigationHelper
@Inject constructor(val activity: Activity) {

   /* fun navigateToVenue(venue: Venue, transitionView: View) {
        val intent = Intent()
        intent.setClass(activity, VenueActivity::class.java)
        intent.putExtra(EXTRA_VENUE, venue) //TODO: make venue parcelable ?

        //maybe better to just broadcast that we want to start a new activity ?
        //TODO: find a way to pass activity here without breaking mvp
        val options = ActivityOptions.makeSceneTransitionAnimation(activity,
            android.util.Pair.create<View, String>(transitionView,
                activity.getString(R.string.transition_venue)),
            android.util.Pair.create<View, String>(transitionView,
                activity.resources.getString(R.string.transition_venue_background)))
        activity.startActivity(intent, options.toBundle())
    }
*/
}