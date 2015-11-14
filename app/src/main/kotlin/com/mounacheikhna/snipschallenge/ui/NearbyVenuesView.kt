package com.mounacheikhna.snipschallenge.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.bindView
import com.mounacheikhna.snipschallenge.R

class NearbyVenuesView(context: Context?, attrs: AttributeSet?): LinearLayout(context, attrs) {

    val content: ViewGroup by bindView(R.id.main_content)
    val venuesList: RecyclerView by bindView(R.id.venues_list)
    val venuesAnimator: BetterViewAnimator by bindView(R.id.venues_animator)

    val venusAdapter: VenuesAdapter by lazy(LazyThreadSafetyMode.NONE) { VenuesAdapter() }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        //get location

    }

}