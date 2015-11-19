package com.mounacheikhna.snipschallenge.ui.venues

import com.mounacheikhna.snipschallenge.ui.VenueResult
import com.mounacheikhna.snipschallenge.ui.base.PresenterScreen
import rx.Observable
import rx.functions.Func1

interface VenuesScreen : PresenterScreen {
    fun onNewLocationUpdate()

    fun cancelRefreshForLocation(): Observable<Void>
}