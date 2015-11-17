package com.mounacheikhna.snipschallenge.ui

import android.content.Context
import android.support.percent.PercentRelativeLayout
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.mounacheikhna.snipschallenge.R
import com.mounacheikhna.snipschallenge.api.Venue
import com.squareup.picasso.Picasso
import timber.log.Timber

class VenueView: PercentRelativeLayout {

    val venueImage: ImageView by bindView(R.id.venue_image)
    val venueName: TextView by bindView(R.id.venue_name)
    val venueDescription: TextView by bindView(R.id.venue_description)
    val venueLocation: TextView by bindView(R.id.venue_location)
    val venueRating: TextView by bindView(R.id.venue_rating)
    val venuePrice: TextView by bindView(R.id.venue_price)

    public constructor(context: Context) : super(context) {
        init(context);
    }

    public constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context);
    }

    public constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,
        attrs, defStyleAttr) {
        init(context);
    }

    private fun init(context: Context) {
        //orientation = VERTICAL
    }

    fun bindTo(item: VenueResult, picasso: Picasso) {
        val photos = item.getAllPhotos()
        if(photos != null && photos.size > 0) {
            val url = "${photos[0].prefix}300x500${photos[0].suffix}"
            Timber.d(" TEST - load url for img : "+ url)
            picasso.load(url)
                .placeholder(R.drawable.ic_city)
                .fit()
                .error(R.drawable.ic_city)
                //.transform(avatarTransformation)
                .into(venueImage)
        }
        var venue = item.venue
        venueName.text = venue.name
        venueRating.text = if(venue.rating != null) "${venue.rating}" else "0"
        //venuePrice.setText(venue.price)

        if(venue.location.formattedAddress == null) {
            venueLocation.visibility = View.GONE
        }
        else {
            venueLocation.text = venue.location.formattedAddress?.joinToString(",")
        }
        venueDescription.text = venue.description
        venueLocation.text = venue.location.address
    }
}