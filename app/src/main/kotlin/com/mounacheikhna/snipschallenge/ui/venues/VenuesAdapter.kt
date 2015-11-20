package com.mounacheikhna.snipschallenge.ui.venues

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mounacheikhna.snipschallenge.R
import com.mounacheikhna.snipschallenge.ui.VenueResult
import com.squareup.picasso.Picasso
import rx.functions.Action1
import java.util.*

/**
 * Adapter for venues list.
 */
class VenuesAdapter(picasso: Picasso) : RecyclerView.Adapter<VenuesAdapter.VenueViewHolder>(), Action1<VenueResult> {

    var venues: MutableList<VenueResult> = ArrayList()
    var picasso: Picasso

    init {
        this.picasso = picasso
    }

    fun setData(list: MutableList<VenueResult>) {
        venues = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return venues.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): VenueViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.venue_item, parent,
            false)
        return VenueViewHolder(view as VenueItemView)
    }

    override fun onBindViewHolder(viewHolder: VenueViewHolder, position: Int) {
        viewHolder.bindTo(venues[position])
    }

    inner class VenueViewHolder : RecyclerView.ViewHolder {
        lateinit var itemVenueItemView: VenueItemView

        public constructor(item: VenueItemView) : super(item) {
            this.itemVenueItemView = item
        }

        fun bindTo(venue: VenueResult) {
            itemVenueItemView.bindTo(venue, picasso)
        }
    }

    override fun call(v: VenueResult) {
        venues.add(v)
        setData(venues)
    }

    fun clear() {
        setData(ArrayList())
    }

}