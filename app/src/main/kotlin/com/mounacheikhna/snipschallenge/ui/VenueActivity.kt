package com.mounacheikhna.snipschallenge.ui

import android.app.SharedElementCallback
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.ColorUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import butterknife.bindView
import com.mounacheikhna.snipschallenge.FoursquareApp
import com.mounacheikhna.snipschallenge.R

import com.squareup.picasso.Callback.EmptyCallback
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.inject.Inject


public class VenueActivity : AppCompatActivity() {

    val toolbar: Toolbar by bindView(R.id.toolbar)

    val mainImage: ImageView by bindView(R.id.main_image)

    @Inject lateinit var picasso: Picasso

    companion object {
        public var EXTRA_VENUE = "extra_venue"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.venue_activity)

        FoursquareApp.appComponent.inject(this)
        setupToolbar()
        setExitSharedElementCallback(fabSharedElementCallback)

        var venueResult = intent.getParcelableExtra<VenueResult>(EXTRA_VENUE)

        //TODO: this in activity -> maybe put it in a view with a presenter
        Timber.d(" TEST url : "+ venueResult.photoUrl)
        if (!TextUtils.isEmpty(venueResult.photoUrl)) {
            picasso.load(venueResult.photoUrl)
                //.placeholder(R.drawable.ic_city)
                //.error(R.drawable.ic_city)
                .into(mainImage/*, PaletteCallback()*/)
        }

    }

    /*inner class PaletteCallback : EmptyCallback() {
        override fun onSuccess() {
            super.onSuccess()
            var bitmap = (mainImage.drawable as BitmapDrawable).bitmap;

            Palette.from(bitmap)
                .maximumColorCount(3)
                .clearFilters()
                .generate { palette ->
                    val isDark: Boolean
                    val lightness = isDark(palette)
                    if (lightness == LIGHTNESS_UNKNOWN) {
                        isDark = isDark(bitmap, bitmap.width / 2, 0)
                    } else {
                        isDark = lightness == IS_DARK
                    }

                    if (!isDark) {
                        // make back icon dark on light images
                        *//*toolbar.setColorFilter(ContextCompat.getColor(
                            this@DribbbleShot, R.color.dark_icon))*//*
                    }
                }
        }
    }*/

    private fun setupToolbar() {
         delegate.setSupportActionBar(toolbar)
         setTitle(R.string.app_name)
     }

    private val fabSharedElementCallback = object : SharedElementCallback() {
        override fun onCaptureSharedElementSnapshot(sharedElement: View,
                                                    viewToGlobalMatrix: Matrix,
                                                    screenBounds: RectF): Parcelable {
            // store a snapshot of the fab to fade out when morphing to the login dialog
            val bitmapWidth = Math.round(screenBounds.width())
            val bitmapHeight = Math.round(screenBounds.height())
            var bitmap: Bitmap? = null
            if (bitmapWidth > 0 && bitmapHeight > 0) {
                bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
                sharedElement.draw(Canvas(bitmap))
            }
            return bitmap as Parcelable
        }
    }

}

/*
    TODO:
     - fix image display
     - display fab with right icon
     - fix animation with shared elt on both enter/exit
     - add back btn to toolbar
     - add some content in the rest of screen
     - apply rest of palette

     */

