package com.mounacheikhna.snipschallenge.ui

import android.Manifest
import android.content.Context
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import butterknife.bindView
import com.mounacheikhna.snipschallenge.FoursquareApp
import com.mounacheikhna.snipschallenge.R
import com.mounacheikhna.snipschallenge.api.FoursquareApi
import com.mounacheikhna.snipschallenge.api.Venue
import com.tbruyelle.rxpermissions.RxPermissions
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import retrofit.Retrofit
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.lang.kotlin.onError
import rx.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NearbyVenuesView : LinearLayout {

    val venuesList: RecyclerView by bindView(R.id.venues_list)
    val venuesAnimator: BetterViewAnimator by bindView(R.id.venues_animator)
    val venuesAdapter: VenuesAdapter by lazy(LazyThreadSafetyMode.NONE) { VenuesAdapter() }

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

    private fun checkLocationPermission() {
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
            .subscribe({ granted ->
                if (granted) {
                    fetchVenues()
                } else {
                    val message = R.string.error_permission_not_granted
                    showSnackbar(message)
                }
            },
            { error ->
                showSnackbar(error.message ?: "error")
            })
    }

    private fun fetchVenues() {
        Log.d("TEST", "(simple log) TEST - from  fetchVenues ");
        Timber.d(" (log from timber) TEST - from  fetchVenues ");
        locationProvider.lastKnownLocation.asObservable()
            .delay(30, TimeUnit.SECONDS) //temp so i can see results on stetho
            .flatMap { location ->
                Log.d("TEST", " TEST received location lat: "+ location.latitude)
                foursquareApi.searchVenues("${location.latitude}, ${location.longitude}")
            }
            //.filter { it.meta.code.equals(200) } //TODO: maybe filter this for result and rest display error
            .flatMapIterable { it.response.venues }
            .flatMap { it -> foursquareApi.venueDetails(it.id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { venue -> venuesAdapter.call(venue) },
                { error -> displayError() },
                { /* TODO: hide progress*/ }
            )
            //.subscribe(venuesAdapter) //temp maybe we should instead collect a list and then pass it to adapter ?
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