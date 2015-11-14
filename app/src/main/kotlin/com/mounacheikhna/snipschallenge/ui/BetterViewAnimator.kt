package com.mounacheikhna.snipschallenge.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ViewAnimator

class BetterViewAnimator(context: Context?, attrs: AttributeSet?): ViewAnimator(context, attrs) {

    fun setDisplayedChildId(id: Int) {
        if (getDisplayedChildId() == id) {
            return
        }
        var i = 0
        val count = getChildCount()
        while (i < count) {
            if (getChildAt(i).getId() == id) {
                setDisplayedChild(i)
                return
            }
            i++
        }
        val name = getResources().getResourceEntryName(id)
        throw IllegalArgumentException("No view with ID " + name)
    }

    fun getDisplayedChildId(): Int {
        return getChildAt(getDisplayedChild()).getId()
    }
}
