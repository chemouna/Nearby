package com.mounacheikhna.snipschallenge.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.ImageView
import butterknife.bindView
import com.mounacheikhna.snipschallenge.FoursquareApp
import com.mounacheikhna.snipschallenge.R
import com.squareup.picasso.Picasso
import javax.inject.Inject

public class VenueActivity : AppCompatActivity() {

    val mainImage: ImageView by bindView(R.id.main_image)

    @Inject lateinit var picasso: Picasso

    companion object {
        public var EXTRA_VENUE = "extra_venue"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.venue_activity)

        FoursquareApp.appComponent.inject(this)
        var venueResult = intent.getParcelableExtra<VenueResult>(EXTRA_VENUE)

        //TODO: palette
        if (!TextUtils.isEmpty(venueResult.photoUrl)) {
            picasso.load(venueResult.photoUrl)
                .placeholder(R.drawable.ic_city)
                .error(R.drawable.ic_city)
                .fit()
                .into(mainImage/*, Callback.EmptyCallback() {
                    var bitmap = ((BitmapDrawable) viewHolder.image.getDrawable()).getBitmap();

                }*/)
        }

    }

    /* private fun setupToolbar() {
         delegate.setSupportActionBar(toolbar)
         setTitle(R.string.app_name)
     }
 */

}