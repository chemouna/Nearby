package com.mounacheikhna.snipschallenge.ui.venues

import android.location.Location
import android.support.test.runner.AndroidJUnit4
import com.mounacheikhna.snipschallenge.api.FoursquareApi
import com.mounacheikhna.snipschallenge.ui.VenueResult
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations.initMocks
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import retrofit.mock.MockRetrofit
import retrofit.mock.NetworkBehavior
import retrofit.mock.RxJavaBehaviorAdapter
import rx.Observable
import rx.observers.TestSubscriber
import rx.plugins.RxJavaSchedulersTestRule

/**
 * NOTE: this is a unit test but we need to put it here (instead of test/ folder) to use
 * RxJavaSchedulersTestRule which replaces observeOn(mainThread) in code we run test against
 * with {@code Schedulers.immediate()}
 *
 * @see RxJavaSchedulersTestRule
 */
@RunWith(AndroidJUnit4::class)
public class VenuesPresenterTest {

    @get:Rule public var rxJavaSchedulersHookResetRule = RxJavaSchedulersTestRule()

    lateinit var presenter: VenuesPresenter
    @Mock lateinit var locationProvider: ReactiveLocationProvider
    @Mock lateinit var foursquareApi: FoursquareApi
    @Mock lateinit var venuesScreen: VenuesScreen

    @Before
    fun setUp() {
        initMocks(this)
        presenter = VenuesPresenter(foursquareApi, locationProvider)
    }

    @Test
    fun fetchForLocationWithEmptyResultsDoesntEmitValuesToScreen() {
        var mockLocation = mockLocation()
        `when`(locationProvider.getUpdatedLocation(presenter.createLocationRequest()))
            .thenReturn(Observable.just(mockLocation))
        `when`(foursquareApi.searchVenues("${mockLocation.latitude}, ${mockLocation.longitude}"))
            .thenReturn(Observable.empty())

        presenter.bind(venuesScreen)
        var observable = presenter.fetchVenuesForLocations()

        val testSubscriber = TestSubscriber<VenueResult>()
        observable.subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertNoValues()
        testSubscriber.assertCompleted()
    }

    //TODO: use mock web server to simulate an error and check error state displayed
    @Test //#FBN
    fun fetchForLocationWithErrorResultsEmitsErrorToScreen() {
        var mockLocation = mockLocation()
        `when`(locationProvider.getUpdatedLocation(presenter.createLocationRequest()))
            .thenReturn(Observable.just(mockLocation))

        val behavior = NetworkBehavior.create()
        behavior.setVariancePercent(0)
        behavior.setFailurePercent(100)
        var mockRetrofit = MockRetrofit(behavior, RxJavaBehaviorAdapter.create());
        //mockRetrofit.create(FoursquareApi::class.java, MockFoursquareApi());
    }

    @Test
    fun fetchForLocationWithNoNetworkEmitsNetworkError() {
        //TODO
    }

    @Test
    fun fetchForLocationWithResultsEmitsThemToScreen() {

    }
    /*private fun mockFoursquareApi(): FoursquareApi {
        val behavior = NetworkBehavior.create()
        behavior.setDelay(100, TimeUnit.MILLISECONDS)
        behavior.setFailurePercent(100)
        behavior.setVariancePercent(networkVariancePercent.get())

        MockRetrofit(behavior, RxJavaBehaviorAdapter.create());
    }*/

    private fun mockLocation(): Location {
        var mockLocation = mock(Location::class.java)
        //Paris: 48.8567° N, 2.3508° E
        `when`(mockLocation.latitude).thenReturn(48.8567)
        `when`(mockLocation.longitude).thenReturn(2.3508)
        return mockLocation
    }
}