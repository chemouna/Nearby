package com.mounacheikhna.nearby.ui.venues

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.support.percent.PercentRelativeLayout
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.mounacheikhna.nearby.R
import com.mounacheikhna.nearby.photo.RoundedCornersTransformation
import com.mounacheikhna.nearby.ui.VenueDetailsActivity
import com.mounacheikhna.nearby.ui.VenueResult
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

/**
 * View to display a {@link VenueResult} in venues list.
 */
class VenueItemView : PercentRelativeLayout {

    val venueImage: ImageView by bindView(R.id.venue_image)
    val venueName: TextView by bindView(R.id.venue_name)
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
        if (!TextUtils.isEmpty(item.bestPhotoUrl)) {
            picasso.load(item.bestPhotoUrl)
                .placeholder(R.drawable.ic_city)
                .error(R.drawable.ic_city)
                .transform(photoTransformation)
                .fit()
                .into(venueImage)
        }
        venueImage.setOnClickListener({ view ->
            venueImage.transitionName = resources.getString(R.string.transition_venue)
            venueImage.setBackgroundColor(ContextCompat.getColor(context, R.color.background_light))
            val intent = Intent()
            intent.setClass(context, VenueDetailsActivity::class.java)
            intent.putExtra(VenueDetailsActivity.EXTRA_VENUE, item)
            val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                android.util.Pair.create<View, String>(venueImage,
                    context.getString(R.string.transition_venue)),
                android.util.Pair.create<View, String>(venueImage,
                    context.resources.getString(R.string.transition_venue_background)))
            context.startActivity(intent, options.toBundle())
        })

        var venue = item.venue
        venueName.text = venue.name
        venueRating.text = if (venue.rating != null) "${venue.rating}" else "0"

        if (venue.price == null || venue.price?.tier == null) {
            venuePrice.visibility = View.GONE
        } else {
            venuePrice.text = venue.price?.tier.toString()
        }

        if (venue.location.formattedAddress == null ) {
            venueLocation.visibility = View.GONE
        } else {
            venueLocation.text = venue.location.formattedAddress?.joinToString(",")
        }
        venueLocation.text = venue.location.address
    }
}