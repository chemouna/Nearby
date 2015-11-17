package com.mounacheikhna.snipschallenge.ui.views

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
import com.mounacheikhna.snipschallenge.ui.BetterViewAnimator
import com.mounacheikhna.snipschallenge.ui.DividerItemDecoration
import com.mounacheikhna.snipschallenge.ui.VenuesAdapter
import com.squareup.picasso.Picasso
import rx.Observable
import rx.functions.Func2
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

class NearbyVenuesView: LinearLayout {

    val venuesList: RecyclerView by bindView(R.id.venues_list)
    val venuesAnimator: BetterViewAnimator by bindView(R.id.venues_animator)

    lateinit var venuesAdapter: VenuesAdapter
    lateinit var nearbyVenuesSubscription: Subscription
    var subscriptions: CompositeSubscription = CompositeSubscription()

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
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST, dividerPaddingStart,
                forRtl))
        checkLocationPermission()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscriptions.unsubscribe() //unsubscribe from all to avoid leaks
    }

    /**
     * Check for location permissions (granted pre-Marshmallow and asks the use for them
     * in Marshmallow and up).
     *
     */
    private fun checkLocationPermission() {
        subscriptions.add(Observable.subscribe({ granted ->
            if (granted) {
                fetchNearbyVenues()
            } else {
                val message = R.string.error_permission_not_granted
                showSnackbar(message)
            }
        },
            { error ->
                showSnackbar(error.message ?: "error")
            }))
    }

    /**
     * Fetch nearby venues each time a location change but gives the user a message saying it will
     * fetch so that if they don't want to they can cancel it.
     *
     */
    private fun fetchNearbyVenues() {
        val updatedLocation = locationProvider.getUpdatedLocation(createLocationRequest())
        var locationSubscription = Observable.subscribe { location ->
            nearbyVenuesSubscription = startNearbyVenuesSearch(location)
            var snackBar = createForNewLocationSnackbar()
            snackBar.show()
        }
        subscriptions.add(locationSubscription)
    }

    private fun createForNewLocationSnackbar(): Snackbar {
        var snackBar = Snackbar.setAction(R.string.cancel,
            { /* the click action is specified in dismiss subscribe method*/ })
        var snackBarSubscription = Observable.subscribe {
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
        subscriptions.add(snackBarSubscription)
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
        val searchObservable = Observable.flatMapIterable { it -> it.response.venues }
        val venueDetailsObservable = Observable.flatMap { it -> foursquareApi.venueDetails(it.id) }
        val venuePhotosObservable = Observable.flatMap { it -> foursquareApi.venuePhotos(it.id, 1) }

        var subscription = Observable.subscribe(
            { venue ->
                if (venuesAnimator.getDisplayedChildId() !== R.id.venues_list) {
                    venuesAnimator.setDisplayedChildId(R.id.venues_list)
                }
                venuesAdapter.call(venue)
            },
            { error -> displayError() },
            { Timber.d(" completed! ") }
        )

        subscriptions.add(subscription)
        return subscription
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
            .setInterval(1000000)
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