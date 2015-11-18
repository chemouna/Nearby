package com.mounacheikhna.snipschallenge.ui.base

import rx.Subscription
import rx.subscriptions.CompositeSubscription

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