package com.mounacheikhna.snipschallenge.ui

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Checkable
import android.widget.ImageButton
import android.widget.LinearLayout
import com.mounacheikhna.snipschallenge.R

/**
 * A {@link Checkable} {@link ImageButton} which has a minimum offset i.e. translation Y.
 */
class CheckableFab: FloatingActionButton, Checkable {

    private var isChecked = false
    private var minOffset: Int = 0

    public constructor(context: Context) : super(context) {
        init();
    }

    public constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init();
    }

    public constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,
        attrs, defStyleAttr) {
        init();
    }

    private fun init() {
        setOnClickListener({ toggle() })
    }

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
        //temp
        jumpDrawablesToCurrentState()
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
