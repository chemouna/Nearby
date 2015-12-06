package com.mounacheikhna.nearby.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * A divider for items in a {@link RecyclerView}.
 */
class DividerItemDecoration(context: Context, orientation: Int, paddingStart: Float,
                            private val rtl: Boolean) : RecyclerView.ItemDecoration() {
    private val divider: Drawable
    private var orientation: Int = 0
    private var paddingStart: Float = 0.toFloat()

    init {

        val a = context.obtainStyledAttributes(ATTRS)
        divider = a.getDrawable(0)
        a.recycle()
        setOrientation(orientation)
        setPaddingStart(paddingStart)
    }

    fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw IllegalArgumentException("invalid orientation")
        }
        this.orientation = orientation
    }

    fun setPaddingStart(paddingStart: Float) {
        if (paddingStart < 0) {
            throw IllegalArgumentException("paddingStart < 0")
        }
        this.paddingStart = paddingStart
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        if (orientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = (parent.paddingLeft + (if (rtl) 0 else paddingStart.toInt()))
        val right = (parent.width - parent.paddingRight + (if (rtl) paddingStart.toInt() else 0)).toInt()

        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin + Math.round(
                ViewCompat.getTranslationY(child))
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin + Math.round(
                ViewCompat.getTranslationX(child))
            val right = left + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State?) {
        if (orientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, divider.intrinsicHeight)
        } else {
            outRect.set(0, 0, divider.intrinsicWidth, 0)
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
        val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }
}
