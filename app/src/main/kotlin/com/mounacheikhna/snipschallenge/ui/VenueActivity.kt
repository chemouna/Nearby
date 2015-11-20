package com.mounacheikhna.snipschallenge.ui

import android.app.SharedElementCallback
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.CollapsingToolbarLayout
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
import com.mounacheikhna.snipschallenge.util.ColorUtil
import com.squareup.picasso.Callback.EmptyCallback
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.annotation.Resource
import javax.inject.Inject


public class VenueActivity : AppCompatActivity() {

    val toolbar: Toolbar by bindView(R.id.toolbar)
    val mainImage: ImageView by bindView(R.id.main_image)
    val collapsingTitle: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)

    @Inject lateinit var picasso: Picasso

    companion object {
        public var EXTRA_VENUE = "extra_venue"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.venue_activity)
        delegate.setSupportActionBar(toolbar)
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);

        FoursquareApp.appComponent.inject(this)
        setExitSharedElementCallback(fabSharedElementCallback)

        var venueResult: VenueResult? = intent.getParcelableExtra<VenueResult>(EXTRA_VENUE) ?: return
        collapsingTitle.title = venueResult!!.venue.name

        //TODO: this in activity -> maybe put it in a view with a presenter
        Timber.d(" TEST url : "+ venueResult.bestPhotoUrl)
        if (!TextUtils.isEmpty(venueResult.bestPhotoUrl)) {
            picasso.load(venueResult.bestPhotoUrl)
                .into(mainImage, PaletteCallback())
        }


    }

    inner class PaletteCallback : EmptyCallback() {
        override fun onSuccess() {
            super.onSuccess()
            var bitmap = (mainImage.drawable as BitmapDrawable).bitmap;

            Palette.from(bitmap)
                .maximumColorCount(3)
                .clearFilters()
                .generate { palette ->
                    val isDark: Boolean
                    val lightness = ColorUtil.isDark(palette)
                    if (lightness == ColorUtil.LIGHTNESS_UNKNOWN) {
                        isDark = ColorUtil.isDark(bitmap, bitmap.width / 2, 0)
                    } else {
                        isDark = lightness == ColorUtil.IS_DARK
                    }

                    if (!isDark) {
                        // darken toolbar's back image on images that have a light color
                        var  colorFilter = PorterDuffColorFilter(R.color.primary_dark_icon, PorterDuff.Mode.MULTIPLY);
                        var upArrow = ContextCompat.getDrawable(baseContext, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                        upArrow.colorFilter = colorFilter;
                        supportActionBar.setHomeAsUpIndicator(upArrow);
                    }


                }
        }
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
     - fix animation with shared elt on both enter/exit
     - add back btn to toolbar
     - add some content in the rest of screen
     - apply rest of palette

     */

