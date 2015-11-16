package com.mounacheikhna.snipschallenge.ui

import android.Manifest
import android.annotation.TargetApi
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
import android.os.Build
import android.support.v4.content.ContextCompat
import com.mounacheikhna.snipschallenge.api.Venue
import com.mounacheikhna.snipschallenge.api.VenueDetailsResponse
import com.squareup.picasso.Picasso
import rx.Observable
import rx.functions.Func2

class NearbyVenuesView: LinearLayout {

    val venuesList: RecyclerView by bindView(R.id.venues_list)
    val venuesAnimator: BetterViewAnimator by bindView(R.id.venues_animator)

    lateinit var venuesAdapter: VenuesAdapter
    lateinit var nearbyVenuesSubscription: Subscription

    @Inject lateinit var rxPermissions: RxPermissions
    @Inject lateinit var locationProvider: ReactiveLocationProvider
    @Inject lateinit var foursquareApi: FoursquareApi
    @Inject lateinit var picasso: Picasso

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
        orientation = VERTICAL
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        FoursquareApp.appComponent.inject(this)

        venuesAdapter = VenuesAdapter(picasso)
        venuesList.adapter = venuesAdapter
        venuesList.layoutManager = LinearLayoutManager(context)

        val dividerPaddingStart = context.resources.getDimension(R.dimen.venue_divider_padding_start)
        val forRtl = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isRtl()
        venuesList.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST, dividerPaddingStart, forRtl))
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
        updatedLocation.distinctUntilChanged()
            .subscribe { location ->
            nearbyVenuesSubscription = startNearbyVenuesSearch(location)
            var snackBar = createForNewLocationSnackbar()
            snackBar.show()
        }
    }

    private fun createForNewLocationSnackbar(): Snackbar {
        var snackBar = Snackbar.make(this, R.string.info_location_changed_fetch,
            Snackbar.LENGTH_LONG)
            .setAction(R.string.cancel, View.OnClickListener {})
        RxSnackbar.dismisses(snackBar)
            .firstOrDefault(0)
            .subscribe {
                eventId ->
                when (eventId) {
                    Snackbar.Callback.DISMISS_EVENT_ACTION -> {
                        nearbyVenuesSubscription.unsubscribe()
                    }
                    else -> {
                        venuesAdapter.clear()
                    }
                }
            }
        return snackBar
    }

    /**
     * Fetches venues near a {@link Location} then for each id fetches the {@link Venue}
     * to get its ratings and photos then displays them.
     *
     * @param location to fetch venues near it.
     * @return subscription to unsubscribe from it.
     */
    private fun startNearbyVenuesSearch(location: Location): Subscription {
        val searchObservable = foursquareApi.searchVenues("${location.latitude}, ${location.longitude}")
            .flatMapIterable { it -> it.response.venues }
        val venueDetailsObservable = searchObservable.flatMap { it -> foursquareApi.venueDetails(it.id) }
        val venuePhotosObservable = searchObservable.flatMap { it -> foursquareApi.venuePhotos(it.id, 1) }
        return Observable.zip(venueDetailsObservable, venuePhotosObservable, { response, photos ->
                VenueResult(response.response.venue, photos)
             })
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
     * update interval and number of updates.
     *
     * @return locationRequest the request to define updates rate.
     */
    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        locationRequest.setInterval(50000)
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private fun isRtl(): Boolean {
        return layoutDirection == View.LAYOUT_DIRECTION_RTL
    }
}