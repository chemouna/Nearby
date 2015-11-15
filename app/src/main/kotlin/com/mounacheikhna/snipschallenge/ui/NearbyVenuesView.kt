package com.mounacheikhna.snipschallenge.ui

import android.Manifest
import android.content.Context
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import butterknife.bindView
import com.google.android.gms.location.LocationRequest
import com.jakewharton.rxbinding.support.design.widget.RxSnackbar
import com.mounacheikhna.snipschallenge.FoursquareApp
import com.mounacheikhna.snipschallenge.R
import com.mounacheikhna.snipschallenge.api.FoursquareApi
import com.tbruyelle.rxpermissions.RxPermissions
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import android.location.Location;

class NearbyVenuesView : LinearLayout {

    val venuesList: RecyclerView by bindView(R.id.venues_list)
    val venuesAnimator: BetterViewAnimator by bindView(R.id.venues_animator)
    val venuesAdapter: VenuesAdapter by lazy(LazyThreadSafetyMode.NONE) { VenuesAdapter() }

    lateinit var nearbyVenuesSubscription: Subscription

    @Inject lateinit var rxPermissions: RxPermissions
    @Inject lateinit var locationProvider: ReactiveLocationProvider
    @Inject lateinit var foursquareApi: FoursquareApi

    public constructor(context: Context) : super(context) {
        init(context);
    }

    public constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context);
    }

    public constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,
        attrs, defStyleAttr) {
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
        checkLocationPermission()
    }

    /**
     * Check for location permissions (granted pre-Marshmallow and asks the use for them
     * in Marshmallow and up).
     *
     */
    private fun checkLocationPermission() {
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe({ granted ->
                if (granted) {
                    fetchNearbyVenues()
                } else {
                    val message = R.string.error_permission_not_granted
                    showSnackbar(message)
                }
            },
                { error ->
                    showSnackbar(error.message ?: "error")
                })
    }

    /**
     * Fetch nearby venues each time a location change but gives the user a message saying it will
     * fetch so that if they don't want to they can cancel it.
     *
     */
    private fun fetchNearbyVenues() {
        val updatedLocation = locationProvider.getUpdatedLocation(createLocationRequest())
        updatedLocation.subscribe { location ->
            nearbyVenuesSubscription = startNearbyVenuesSearch(location)
            var snackbar = Snackbar.make(this, "You have moved.. Fetching places near you.",
                Snackbar.LENGTH_SHORT)
                .setAction("Cancel", View.OnClickListener {})
            RxSnackbar.dismisses(snackbar)
                .firstOrDefault(0)
                .subscribe {
                    eventId ->
                    when (eventId) {
                        Snackbar.Callback.DISMISS_EVENT_ACTION -> {
                            Timber.d(" TEST - Canceling locations nearby new fetch ");
                            //TODO: cancel observable to requery update
                            nearbyVenuesSubscription.unsubscribe()
                        }
                        else -> {
                            venuesAdapter.clear()
                        }
                    }
                }
        }
    }

    /**
     * Fetches venues near a {@link Location} then for each id fetches the {@link Venue}
     * to get its ratings and then displays them.
     *
     * @param location to fetch venues near it.
     * @return subscription to unsubscribe from it.
     */
    private fun startNearbyVenuesSearch(location: Location): Subscription {
        return foursquareApi.searchVenues("${location.latitude}, ${location.longitude}")
            .flatMapIterable { it -> it.response.venues }
            .flatMap { it -> foursquareApi.venueDetails(it.id) }
            .map { it -> it.response.venue }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { venue ->
                    if (venuesAnimator.getDisplayedChildId() !== R.id.venues_list) {
                        venuesAnimator.setDisplayedChildId(R.id.venues_list)
                    }
                    venuesAdapter.call(venue)
                },
                { error -> displayError() },
                { Timber.d(" completed! ") }
            )
    }

    /**
     * Creates a {@link LocationRequest} which defines the rate of location updates : accuracy,
     * update interval and number of updates (using location request of library reactive location).
     *
     * @return locationRequest the request to define updates rate.
     */
    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        locationRequest.setFastestInterval(260).setInterval(500/*1000*/)
        locationRequest.setNumUpdates(1)
        return locationRequest
    }

    private fun displayError() {
        //TODO: find a good error state icon
        venuesAnimator.setDisplayedChildId(R.id.venues_error)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showSnackbar(@StringRes message: Int) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
    }

}