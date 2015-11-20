package com.mounacheikhna.snipschallenge.ui

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.ImageButton

/**
 * A {@link Checkable} {@link ImageButton} which has a minimum offset i.e. translation Y.
 */
class CheckableFab(context: Context, attrs: AttributeSet) : ImageButton(context, attrs), Checkable {

    private var isChecked = false
    private var minOffset: Int = 0

    fun setOffset(offset: Int) {
        var offset = offset
        offset = Math.max(minOffset, offset)
        if (translationY != offset.toFloat()) {
            translationY = offset.toFloat()
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun setMinOffset(minOffset: Int) {
        this.minOffset = minOffset
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun setChecked(isChecked: Boolean) {
        if (this.isChecked != isChecked) {
            this.isChecked = isChecked
            refreshDrawableState()
        }
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked()) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    companion object {

        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }
}
