package com.mounacheikhna.snipschallenge.ui

import android.animation.ValueAnimator
import android.app.SharedElementCallback
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.ColorUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.Toolbar
import android.support.v7.widget.ViewUtils
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import butterknife.bindView
import com.mounacheikhna.snipschallenge.FoursquareApp
import com.mounacheikhna.snipschallenge.R
import com.mounacheikhna.snipschallenge.util.Colors
import com.squareup.picasso.Callback.EmptyCallback
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.annotation.Resource
import javax.inject.Inject


public class VenueActivity : AppCompatActivity() {

    val toolbar: Toolbar by bindView(R.id.toolbar)
    val mainImage: ImageView by bindView(R.id.main_image)
    val collapsingTitle: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)
    private val SCRIM_ADJUSTMENT = 0.075f

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
                    val lightness = Colors.isDark(palette)
                    if (lightness == Colors.LIGHTNESS_UNKNOWN) {
                        isDark = Colors.isDark(bitmap, bitmap.width / 2, 0)
                    } else {
                        isDark = lightness == Colors.IS_DARK
                    }

                    if (!isDark) {
                        // darken toolbar back image on images that have a light color
                        var  colorFilter = PorterDuffColorFilter(R.color.primary_dark_icon, PorterDuff.Mode.MULTIPLY);
                        var upArrow = ContextCompat.getDrawable(baseContext, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                        upArrow.colorFilter = colorFilter;
                        supportActionBar.setHomeAsUpIndicator(upArrow);
                    }


                    // color the status bar. Set a complementary dark color on L,
                    // light or dark color on M (with matching status bar icons)
                    var statusBarColor = window.statusBarColor
                    val topColor = Colors.getMostPopulousSwatch(palette)
                    if (topColor != null && (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                        statusBarColor = Colors.scrimify(topColor!!.rgb, isDark, SCRIM_ADJUSTMENT)
                        // set a light status bar on M+
                        if (!isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            setLightStatusBar(mainImage)
                        }
                    }
                    //toolbar.setBackgroundColor(topColor.rgb); //temp //TODO: use a lighter color for it

                    if (statusBarColor != window.statusBarColor) {
                        //mainImage.setScrimColor(statusBarColor)
                        val statusBarColorAnim = ValueAnimator.ofArgb(window.statusBarColor,
                            statusBarColor)
                        statusBarColorAnim.addUpdateListener { animation -> window.statusBarColor = animation.animatedValue as Int }
                        statusBarColorAnim.setDuration(1000)
                        statusBarColorAnim.interpolator = AnimationUtils.loadInterpolator(baseContext, android.R.interpolator.fast_out_slow_in)
                        statusBarColorAnim.start()
                    }
                }

            Palette.from(bitmap)
                .clearFilters()
                .generate { palette ->
                    // slightly more opaque ripple on the pinned image to compensate
                    // for the scrim
                    mainImage.foreground = createRipple(palette, 0.3f, 0.6f,
                        ContextCompat.getColor(baseContext, R.color.medium_grey), true)
                }

        }
    }

    fun createRipple(palette: Palette,
                     @FloatRange(from = 0.0, to = 1.0) darkAlpha: Float,
                     @FloatRange(from = 0.0, to = 1.0) lightAlpha: Float,
                     @ColorInt fallbackColor: Int,
                     bounded: Boolean): RippleDrawable {
        var rippleColor = fallbackColor
        // try the named swatches in preference order
        if (palette.vibrantSwatch != null) {
            rippleColor = Colors.modifyAlpha(palette.vibrantSwatch!!.rgb, darkAlpha)
        } else if (palette.lightVibrantSwatch != null) {
            rippleColor = Colors.modifyAlpha(palette.lightVibrantSwatch!!.rgb,
                lightAlpha)
        } else if (palette.darkVibrantSwatch != null) {
            rippleColor = Colors.modifyAlpha(palette.darkVibrantSwatch!!.rgb,
                darkAlpha)
        } else if (palette.mutedSwatch != null) {
            rippleColor = Colors.modifyAlpha(palette.mutedSwatch!!.rgb, darkAlpha)
        } else if (palette.lightMutedSwatch != null) {
            rippleColor = Colors.modifyAlpha(palette.lightMutedSwatch!!.rgb,
                lightAlpha)
        } else if (palette.darkMutedSwatch != null) {
            rippleColor = Colors.modifyAlpha(palette.darkMutedSwatch!!.rgb, darkAlpha)
        }
        return RippleDrawable(ColorStateList.valueOf(rippleColor), null,
            if (bounded) ColorDrawable(Color.WHITE) else null)
    }

    fun setLightStatusBar(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
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

