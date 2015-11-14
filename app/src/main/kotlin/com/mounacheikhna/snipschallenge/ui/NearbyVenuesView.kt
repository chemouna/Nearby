package com.mounacheikhna.snipschallenge.ui

import android.Manifest
import android.content.Context
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import butterknife.bindView
import com.mounacheikhna.snipschallenge.FoursquareApp
import com.mounacheikhna.snipschallenge.R
import com.tbruyelle.rxpermissions.RxPermissions
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import timber.log.Timber
import javax.inject.Inject

class NearbyVenuesView : LinearLayout {

    val venuesList: RecyclerView by bindView(R.id.venues_list)
    val venuesAnimator: BetterViewAnimator by bindView(R.id.venues_animator)

    val venuesAdapter: VenuesAdapter by lazy(LazyThreadSafetyMode.NONE) { VenuesAdapter() }

    @Inject
    lateinit var rxPermissions: RxPermissions

    @Inject
    lateinit var locationProvider: ReactiveLocationProvider

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

    private fun fetchVenues() {
        locationProvider.lastKnownLocation
            .subscribe({ location -> // Timber.d(" location received : %s", location)
                Toast.makeText(context, " Received location : " + location,
                    Toast.LENGTH_LONG).show()
            },
                { error ->
                    Toast.makeText(context, " Error location : " + error, Toast.LENGTH_LONG).show()
                    Timber.d(" TEST - Error e  : " + error)
                });
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
            }, { error ->
                showSnackbar(error.message ?: "error")
            })
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showSnackbar(@StringRes message: Int) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
    }

}