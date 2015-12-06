package com.mounacheikhna.nearby.ui.base

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

    fun addSubscription(subscription: Subscription) {
        compositeSubscription.add(subscription)
    }

    fun onAttach(view: T) {
        bind(view)
    }

    fun onDetach() {
        compositeSubscription.clear()
        unbind()
    }

}