package com.mounacheikhna.nearby.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ViewAnimator

class BetterViewAnimator(context: Context, attrs: AttributeSet) : ViewAnimator(context, attrs) {

    fun setDisplayedChildId(id: Int) {
        if (getDisplayedChildId() == id) {
            return
        }
        var i = 0
        val count = childCount
        while (i < count) {
            if (getChildAt(i).id == id) {
                displayedChild = i
                return
            }
            i++
        }
        val name = resources.getResourceEntryName(id)
        throw IllegalArgumentException("No view with ID " + name)
    }

    fun getDisplayedChildId(): Int {
        return getChildAt(displayedChild).id
    }
}
