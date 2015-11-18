package com.mounacheikhna.snipschallenge.ui.venues

import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.mounacheikhna.snipschallenge.api.FoursquareApi
import com.mounacheikhna.snipschallenge.ui.VenueResult
import com.mounacheikhna.snipschallenge.ui.base.BasePresenter
import com.mounacheikhna.snipschallenge.ui.base.ScopeSingleton
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

//@ScopeSingleton(VenuesView.VenuesComponent::class)
@Singleton //temp
class VenuesPresenter : BasePresenter<VenuesScreen> {

    lateinit var searchVenuesSubscription: Subscription
    var subscriptions: CompositeSubscription = CompositeSubscription()

    val locationProvider: ReactiveLocationProvider
    val foursquareApi: FoursquareApi

    //TODO: maybe inject it here and make it also injectable in its view ?
    @Inject
    public constructor(foursquareApi: FoursquareApi, locationProvider: ReactiveLocationProvider) : super() {
        this.foursquareApi = foursquareApi
        this.locationProvider = locationProvider
    }

    /**
     * Fetch nearby venues each time a location change but gives the user a message saying it will
     * fetch so that if they don't want to they can cancel it.
     *
     */
    fun fetchNearbyVenues() {
        val updatedLocation = locationProvider.getUpdatedLocation(createLocationRequest())
        var locationSubscription = updatedLocation.distinctUntilChanged()
            .subscribe { location ->
                view?.onNewLocationUpdate()
                searchVenuesSubscription = startNearbyVenuesSearch(location)
            }
        subscriptions.add(locationSubscription)
    }

    /**
     * Fetches venues near a {@link Location} then for each id fetches the {@link (
    com.mounacheikhna.snipschallenge.api.Venue)
    }
     * to get its ratings and photos then displays them.
     *
     * @param location to fetch venues near it.
     * @return subscription to unsubscribe from it.
     */
    private fun startNearbyVenuesSearch(location: Location): Subscription {
        val searchObservable = foursquareApi.searchVenues(
            "${location.latitude}, ${location.longitude}")
            .flatMapIterable { it -> it.response.venues }
        val venueDetailsObservable = searchObservable.flatMap { it ->
            foursquareApi.venueDetails(it.id)
        }
        val venuePhotosObservable = searchObservable.flatMap { it ->
            foursquareApi.venuePhotos(it.id, 1)
        }

        var subscription = Observable.zip(venueDetailsObservable, venuePhotosObservable,
            { respVenues, respPhotos ->
                VenueResult(respVenues.response.venue, respPhotos)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { venueResult -> view?.onVenueFetchSuccess(venueResult) },
                { error -> view?.onVenueFetchError() },
                { Timber.d(" completed! ") }
            )

        subscriptions.add(subscription)
        return subscription
    }

    fun cancelVenuesSearch() {
        searchVenuesSubscription.unsubscribe()
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

}