package com.mounacheikhna.snipschallenge.ui.venues

import android.content.Context
import android.support.percent.PercentRelativeLayout
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.mounacheikhna.snipschallenge.R
import com.mounacheikhna.snipschallenge.photo.RoundedCornersTransformation
import com.mounacheikhna.snipschallenge.ui.VenueResult
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

class VenueItemView : PercentRelativeLayout {

    val venueImage: ImageView by bindView(R.id.venue_image)
    val venueName: TextView by bindView(R.id.venue_name)
    //val venueDescription: TextView by bindView(R.id.venue_description)
    val venueLocation: TextView by bindView(R.id.venue_location)
    val venueRating: TextView by bindView(R.id.venue_rating)
    val venuePrice: TextView by bindView(R.id.venue_price)

    lateinit var photoTransformation: Transformation

    public constructor(context: Context) : super(context) {
        init()
    }

    public constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    public constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,
        attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        var corner = resources.getDimensionPixelSize(R.dimen.venue_photo_radius);
        photoTransformation = RoundedCornersTransformation(corner, 0)
    }

    fun bindTo(item: VenueResult, picasso: Picasso) {
        if (!TextUtils.isEmpty(item.photoUrl)) {
            picasso.load(item.photoUrl)
                .placeholder(R.drawable.ic_city)
                .fit()
                .error(R.drawable.ic_city)
                .transform(photoTransformation)
                .into(venueImage)
        }
        var venue = item.venue
        venueName.text = venue.name
        venueRating.text = if (venue.rating != null) "${venue.rating}" else "0"
        //venuePrice.setText(venue.price)

        if (venue.location.formattedAddress == null) {
            venueLocation.visibility = View.GONE
        } else {
            venueLocation.text = venue.location.formattedAddress?.joinToString(",")
        }
        //venueDescription.text = venue.description
        venueLocation.text = venue.location.address
    }
}