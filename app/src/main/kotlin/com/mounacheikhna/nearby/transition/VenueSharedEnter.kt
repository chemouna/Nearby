package com.mounacheikhna.nearby.transition

import android.content.Context
import android.graphics.Rect
import android.transition.ChangeBounds
import android.transition.TransitionValues
import android.util.AttributeSet
import android.view.View

/**
 * Enter transition for clicking in a venue to expand into venue's detail.
 */
class VenueSharedEnter(context: Context, attrs: AttributeSet) : ChangeBounds(context, attrs) {

    override fun captureEndValues(transitionValues: TransitionValues) {
        super.captureEndValues(transitionValues)
        val width = (transitionValues.values[PROPNAME_PARENT] as View).width
        val bounds = transitionValues.values[PROPNAME_BOUNDS] as Rect
        bounds.right = width
        bounds.bottom = width * 3 / 4
        transitionValues.values.put(PROPNAME_BOUNDS, bounds)
    }

    companion object {
        private val PROPNAME_BOUNDS = "android:changeBounds:bounds"
        private val PROPNAME_PARENT = "android:changeBounds:parent"
    }

}
