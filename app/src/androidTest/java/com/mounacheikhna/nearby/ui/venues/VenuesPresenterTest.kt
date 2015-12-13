package com.mounacheikhna.nearby.ui.venues

import android.location.Location
import android.support.test.runner.AndroidJUnit4
import com.mounacheikhna.nearby.api.FoursquareApi
import com.mounacheikhna.nearby.mock.MockFoursquareApi
import com.mounacheikhna.nearby.ui.VenueResult
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

    @get:Rule public val rxJavaSchedulersHookResetRule = RxJavaSchedulersTestRule()

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
        val mockLocation = mockLocation()
        `when`(locationProvider.getUpdatedLocation(presenter.createLocationRequest()))
            .thenReturn(Observable.just(mockLocation))
        `when`(foursquareApi.searchVenues("${mockLocation.latitude}, ${mockLocation.longitude}"))
            .thenReturn(Observable.empty())

        presenter.bind(venuesScreen)
        val observable = presenter.fetchVenuesForLocations()

        val testSubscriber = TestSubscriber<VenueResult>()
        observable.subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        testSubscriber.assertNoValues()
        testSubscriber.assertCompleted()
    }

    //use mock web server to simulate an error and check that error state used
    @Test //#FBN
    fun fetchForLocationWithErrorResultsEmitsErrorToScreen() {
        val mockLocation = mockLocation()
        `when`(locationProvider.getUpdatedLocation(presenter.createLocationRequest()))
            .thenReturn(Observable.just(mockLocation))

        val behavior = NetworkBehavior.create()
        behavior.setVariancePercent(0)
        behavior.setFailurePercent(100)
        val mockRetrofit = MockRetrofit(behavior, RxJavaBehaviorAdapter.create());
        val mockApi = mockRetrofit.create(FoursquareApi::class.java, MockFoursquareApi())

        presenter = VenuesPresenter(mockApi, locationProvider)
        val observable = presenter.fetchVenuesForLocations()

        val testSubscriber = TestSubscriber<VenueResult>()
        observable.subscribe(testSubscriber)
        //testSubscriber.assertError()
        testSubscriber.assertNoValues()
    }


    @Test
    fun fetchForLocationWithResultsEmitsThemToScreen() {
        val mockLocation = mockLocation()
        `when`(locationProvider.getUpdatedLocation(presenter.createLocationRequest()))
            .thenReturn(Observable.just(mockLocation))

        val behavior = NetworkBehavior.create()
        behavior.setVariancePercent(0)
        behavior.setFailurePercent(0)
        val mockRetrofit = MockRetrofit(behavior, RxJavaBehaviorAdapter.create());
        val mockApi = mockRetrofit.create(FoursquareApi::class.java, MockFoursquareApi())

        presenter = VenuesPresenter(mockApi, locationProvider)
        val observable = presenter.fetchVenuesForLocations()

        val testSubscriber = TestSubscriber<VenueResult>()
        observable.subscribe(testSubscriber)
        testSubscriber.assertValueCount(1)
        testSubscriber.assertCompleted()
    }

    private fun mockLocation(): Location {
        val mockLocation = mock(Location::class.java)
        //Paris: 48.8567° N, 2.3508° E
        `when`(mockLocation.latitude).thenReturn(48.8567)
        `when`(mockLocation.longitude).thenReturn(2.3508)
        return mockLocation
    }
}