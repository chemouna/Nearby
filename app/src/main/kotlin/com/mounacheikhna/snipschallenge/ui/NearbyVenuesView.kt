package com.mounacheikhna.snipschallenge.ui

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import butterknife.bindView
import com.mounacheikhna.snipschallenge.FoursquareApp
import com.mounacheikhna.snipschallenge.R
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import javax.inject.Inject

class NearbyVenuesView: LinearLayout {

    val venuesList: RecyclerView by bindView(R.id.venues_list)
    val venuesAnimator: BetterViewAnimator by bindView(R.id.venues_animator)

    val venuesAdapter: VenuesAdapter by lazy(LazyThreadSafetyMode.NONE) { VenuesAdapter() }

    @Inject
    lateinit var locationProvider: ReactiveLocationProvider

    public constructor(context: Context) : super(context) {
        init(context);
    }

    public constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context);
    }

    public constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context);
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.nearby_venues_view, this, true)
        //TODO: use merge & set orientation vertical
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        FoursquareApp.appComponent.inject(this)

        venuesList.adapter = venuesAdapter
        venuesList.layoutManager = LinearLayoutManager(context)

        //TODO: maybe have a presenter here ?
        //get location
        locationProvider.lastKnownLocation
            .subscribe({ location -> // Timber.d(" location received : %s", location)
                Toast.makeText(context, " Received location : " + location, Toast.LENGTH_LONG).show()
            });

    }

}