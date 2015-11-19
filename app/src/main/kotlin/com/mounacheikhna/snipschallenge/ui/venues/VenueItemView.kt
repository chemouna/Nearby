package com.mounacheikhna.snipschallenge.ui.venues

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.support.percent.PercentRelativeLayout
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Pair
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.mounacheikhna.snipschallenge.R
import com.mounacheikhna.snipschallenge.photo.RoundedCornersTransformation
import com.mounacheikhna.snipschallenge.ui.VenueActivity
import com.mounacheikhna.snipschallenge.ui.VenueResult
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

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
        if (!TextUtils.isEmpty(item.photoUrl)) {
            picasso.load(item.photoUrl)
                .placeholder(R.drawable.ic_city)
                .error(R.drawable.ic_city)
                .transform(photoTransformation)
                .fit()
                .into(venueImage)
        }
       /* venueImage.setOnClickListener({ view ->
            venueImage.setTransitionName(venueImage.getResources().getString(R.string.transition_shot))
            venueImage.setBackgroundColor(
                ContextCompat.getColor(host, R.color.background_light))
            val intent = Intent()
            intent.setClass(context, VenueActivity::class.java)
            intent.putExtra(VenueActivity.EXTRA_SHOT, shot)

            //TODO: find a way to pass activity here without bearking mvp
            val options = ActivityOptions.makeSceneTransitionAnimation(activity,
                Pair.create<View, String>(view, host.getString(R.string.transition_shot)),
                Pair.create<View, String>(view,
                    resources.getString(R.string.transition_shot_background)))
            context.startActivity(intent, options.toBundle())
        })*/

        var venue = item.venue
        venueName.text = venue.name
        venueRating.text = if (venue.rating != null) "${venue.rating}" else "0"

        if (venue.price == null || venue.price?.tier == null) {
            venuePrice.visibility = View.GONE
        }
        else {
            venuePrice.text =  venue.price?.tier.toString()
        }

        if (venue.location.formattedAddress == null ) {
            venueLocation.visibility = View.GONE
        } else {
            venueLocation.text = venue.location.formattedAddress?.joinToString(",")
        }
        venueLocation.text = venue.location.address
    }
}