package com.mounacheikhna.nearby.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.bindView
import com.mounacheikhna.nearby.R
import com.mounacheikhna.nearby.ui.details.VenueDetailsView

class VenueDetailsActivity: AppCompatActivity() {

    val container: VenueDetailsView by bindView(R.id.container)

    companion object {
        public var EXTRA_VENUE = "extra_venue"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.venue_activity)
        container.bind(this)
    }


}