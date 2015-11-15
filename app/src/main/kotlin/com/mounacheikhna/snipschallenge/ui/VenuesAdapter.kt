package com.mounacheikhna.snipschallenge.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mounacheikhna.snipschallenge.api.Venue
import com.mounacheikhna.snipschallenge.R;
import rx.functions.Action1
import timber.log.Timber
import java.util.*

class VenuesAdapter(): RecyclerView.Adapter<VenuesAdapter.ViewHolder>(), Action1<Venue> {

    //TODO: maybe implement Action 1 and pass adapter to call method
    var venues: MutableList<Venue> = ArrayList()

    fun setData(list: MutableList<Venue>) {
        venues = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return venues.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.venue_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val v = venues[position]
        viewHolder.name.text = v.name.toString()
    }

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name) as TextView
    }

    override fun call(v: Venue) {
        venues.add(v)
        setData(venues)
    }

    fun clear() {
        setData(ArrayList())
    }

}