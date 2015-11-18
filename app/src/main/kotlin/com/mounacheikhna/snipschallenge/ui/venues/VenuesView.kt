package com.mounacheikhna.snipschallenge.ui.venues

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import butterknife.bindView
import com.jakewharton.rxbinding.support.design.widget.RxSnackbar
import com.mounacheikhna.snipschallenge.FoursquareApp
import com.mounacheikhna.snipschallenge.R
import com.mounacheikhna.snipschallenge.ui.BetterViewAnimator
import com.mounacheikhna.snipschallenge.ui.DividerItemDecoration
import com.mounacheikhna.snipschallenge.ui.VenueResult
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions.RxPermissions
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class VenuesView : LinearLayout, VenuesScreen {

    /* @ScopeSingleton(VenuesComponent::class)
     @Subcomponent(modules = arrayOf(VenuesModule::class),
         dependencies = arrayOf(AppComponent::class))
     public interface VenuesComponent*//*: BaseComponent*//* {
        fun inject(venuesView: VenuesView)
    }

    @Module
    class VenuesModule {}*/

    val venuesList: RecyclerView by bindView(R.id.venues_list)
    val venuesAnimator: BetterViewAnimator by bindView(R.id.venues_animator)

    lateinit var venuesAdapter: VenuesAdapter
    var subscriptions: CompositeSubscription = CompositeSubscription()

    @Inject lateinit var rxPermissions: RxPermissions
    @Inject lateinit var picasso: Picasso
    @Inject lateinit var presenter: VenuesPresenter

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

        /*DaggerVenuesView_VenuesComponent.builder()
            .venuesModule(VenuesModule())
            .appComponent(FoursquareApp.appComponent)
            .build()
            .inject(this)*/
        //createComponent(VenuesComponent::class.java, FoursquareApp.appComponent)
        FoursquareApp.appComponent.inject(this)
        presenter.bind(this)

        venuesAdapter = VenuesAdapter(picasso)
        venuesList.adapter = venuesAdapter
        venuesList.layoutManager = LinearLayoutManager(context)

        val dividerPaddingStart = context.resources.getDimension(
            R.dimen.venue_divider_padding_start)
        val forRtl = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isRtl()
        venuesList.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST, dividerPaddingStart,
                forRtl))
        checkLocationPermission()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscriptions.unsubscribe() //unsubscribe from all to avoid leaks
        presenter.unbind()
    }

    /**
     * Check for location permissions (granted pre-Marshmallow and asks the use for them
     * in Marshmallow and up).
     *
     */
    private fun checkLocationPermission() {
        subscriptions.add(rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe({ granted ->
                if (granted) {
                    presenter.fetchNearbyVenues()
                } else {
                    val message = R.string.error_permission_not_granted
                    showSnackbar(message)
                }
            },
                { error ->
                    showSnackbar(error.message ?: "error")
                }))
    }

    override fun onNewLocationUpdate() {
        var snackBar = Snackbar.make(this, R.string.info_location_changed_fetch,
            Snackbar.LENGTH_LONG)
            .setAction(R.string.cancel,
                { /* the click action is specified in dismiss subscribe method*/ })
        var snackBarSubscription = RxSnackbar.dismisses(snackBar)
            .firstOrDefault(0)
            .subscribe {
                eventId ->
                when (eventId) {
                    Snackbar.Callback.DISMISS_EVENT_ACTION -> {
                        presenter.cancelVenuesSearch()
                    }
                    else -> {
                        venuesAdapter.clear()
                    }
                }
            }
        subscriptions.add(snackBarSubscription)
        snackBar.show();
    }

    override fun onVenueFetchError() {
        displayError()
    }

    override fun onVenueFetchSuccess(venueResult: VenueResult) {
        if (venuesAnimator.getDisplayedChildId() !== R.id.venues_list) {
            venuesAnimator.setDisplayedChildId(R.id.venues_list)
        }
        venuesAdapter.call(venueResult)
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