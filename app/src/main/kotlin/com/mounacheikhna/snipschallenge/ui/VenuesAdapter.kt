package com.mounacheikhna.snipschallenge.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mounacheikhna.snipschallenge.api.Venue
import com.mounacheikhna.snipschallenge.R;
import rx.functions.Action1
import java.util.*

class VenuesAdapter(): RecyclerView.Adapter<VenuesAdapter.ViewHolder>(), Action1<List<Venue>> {

    //TODO: maybe implement Action 1 and pass adapter to call method
    var venues: List<Venue> = listOf()

    fun setData(list: List<Venue>) {
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
        val v = venues.get(position)
        viewHolder.name.text = v.venueName.toString()
    }

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name) as TextView
    }

    override fun call(venues: List<Venue>?) {
        setData(ArrayList<Venue>(venues))
    }

}