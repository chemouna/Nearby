package com.mounacheikhna.snipschallenge.ui.presenters

import com.mounacheikhna.snipschallenge.api.FoursquareApi
import com.mounacheikhna.snipschallenge.ui.screens.PresenterScreen
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

abstract class BasePresenter<T : PresenterScreen>() {

  protected val compositeSubscription = CompositeSubscription()
  protected var view: T? = null

  fun bind(view: T) {
    this.view = view
  }

  fun unbind() {
    this.view = null
  }

  protected fun addSubscription(subscription: Subscription) {
    compositeSubscription.add(subscription)
  }

  fun onDestroy() {
    compositeSubscription.clear()
    unbind()
  }

}