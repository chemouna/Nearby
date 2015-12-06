package com.mounacheikhna.nearby.ui.venues

import com.mounacheikhna.nearby.ui.base.PresenterScreen
import rx.Observable

/**
 * Venues screen contract.
 */
interface VenuesScreen : PresenterScreen {
    fun onNewLocationUpdate()

    fun cancelRefreshForLocation(): Observable<Void>
}