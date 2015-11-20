package com.mounacheikhna.snipschallenge.ui.venues

import com.mounacheikhna.snipschallenge.ui.base.PresenterScreen
import rx.Observable

/**
 * Venues screen contract.
 */
interface VenuesScreen : PresenterScreen {
    fun onNewLocationUpdate()

    fun cancelRefreshForLocation(): Observable<Void>
}