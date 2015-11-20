package com.mounacheikhna.snipschallenge.ui

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
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
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.transition.Transition
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.mounacheikhna.snipschallenge.FoursquareApp
import com.mounacheikhna.snipschallenge.R
import com.mounacheikhna.snipschallenge.util.Colors
import com.squareup.picasso.Callback.EmptyCallback
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.inject.Inject


public class VenueActivity : AppCompatActivity() {

    val toolbar: Toolbar by bindView(R.id.toolbar)
    val mainImage: ImageView by bindView(R.id.main_image)
    val venueFab: ImageView by bindView(R.id.venue_fab)
    val venueDescription: TextView by bindView(R.id.venue_description)
    val containerPhoneNumber: TextView by bindView(R.id.venue_phone_number_container)
    val venuePhoneNumber: TextView by bindView(R.id.venue_phone_number)
    val venueHours: TextView by bindView(R.id.venue_hours)
    val containerHours: TextView by bindView(R.id.venue_hours_container)

    /*val evaluateButton: TextView by bindView(R.id.evaluate)
    val checkInButton: TextView by bindView(R.id.check_in)
    val shareButton: TextView by bindView(R.id.share)*/

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
        window.sharedElementReturnTransition.addListener(venueReturnHomeListener)

        var venueResult: VenueResult? = intent.getParcelableExtra<VenueResult>(
            EXTRA_VENUE) ?: return
        collapsingTitle.title = venueResult!!.venue.name

        if (!TextUtils.isEmpty(venueResult.bestPhotoUrl)) {
            picasso.load(venueResult.bestPhotoUrl)
                .into(mainImage, PaletteCallback())
        }

        postponeEnterTransition()
        mainImage.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                mainImage.viewTreeObserver.removeOnPreDrawListener(this)
                enterAnimation()
                startPostponedEnterTransition()
                return true
            }
        })

        venueDescription.text = venueResult.venue.description
        if(! TextUtils.isEmpty(venueResult.venue.description)) containerPhoneNumber.visibility = View.VISIBLE

        var openText = if (venueResult.venue.hours?.isOpen ?: false) resources.getString(
                            R.string.open) else resources.getString(R.string.closed)
        venueHours.text = "$openText ${venueResult.venue.hours?.status}"
        if(venueResult.venue.hours != null) containerHours.visibility = View.VISIBLE

    }


    private fun enterAnimation() {
        val showFab = ObjectAnimator.ofPropertyValuesHolder(venueFab,
            PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f))
        showFab.startDelay = 300L
        showFab.setDuration(300L)
        showFab.interpolator = AnimationUtils.loadInterpolator(this,
            android.R.interpolator.linear_out_slow_in)
        showFab.start()
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
                    applyToBackButton(isDark)
                    applyToStatusBar(isDark, palette)
                    applyToToolbar(palette)
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

        private fun applyToToolbar(palette: Palette) {
            var primaryDark = resources.getColor(R.color.primary_dark);
            var primary = resources.getColor(R.color.primary);
            collapsingTitle.setContentScrimColor(palette.getMutedColor(primary));
            collapsingTitle.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        }

        private fun applyToBackButton(isDark: Boolean) {
            if (!isDark) {
                // darken toolbar back image on images that have a light color
                var colorFilter = PorterDuffColorFilter(R.color.primary_dark_icon,
                    PorterDuff.Mode.MULTIPLY);
                var upArrow = ContextCompat.getDrawable(baseContext,
                    R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                upArrow.colorFilter = colorFilter;
                supportActionBar.setHomeAsUpIndicator(upArrow);
            }
        }

        private fun applyToStatusBar(isDark: Boolean, palette: Palette?) {
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

            if (statusBarColor != window.statusBarColor) {
                //mainImage.setScrimColor(statusBarColor)
                val statusBarColorAnim = ValueAnimator.ofArgb(window.statusBarColor,
                    statusBarColor)
                statusBarColorAnim.addUpdateListener { animation -> window.statusBarColor = animation.animatedValue as Int }
                statusBarColorAnim.setDuration(1000)
                statusBarColorAnim.interpolator = AnimationUtils.loadInterpolator(
                    baseContext, android.R.interpolator.fast_out_slow_in)
                statusBarColorAnim.start()
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

    private val venueReturnHomeListener = object : TransitionListenerAdapter() {
        override fun onTransitionStart(transition: Transition) {
            super.onTransitionStart(transition)
            // hide the fab as for some reason it jumps position??  TODO work out why
            venueFab.setVisibility(View.INVISIBLE)
            // fade out the "toolbar" & list as we don't want them to be visible during return
            // animation
           /* back.animate().alpha(0f).setDuration(100).setInterpolator(
                AnimationUtils.loadInterpolator(this@DribbbleShot,
                    android.R.interpolator.linear_out_slow_in))*/
            mainImage.elevation = 1f
            //back.setElevation(0f)
            /*commentsList.animate().alpha(0f).setDuration(50).setInterpolator(
                AnimationUtils.loadInterpolator(this@DribbbleShot,
                    android.R.interpolator.linear_out_slow_in))*/
        }
    }

    open class TransitionListenerAdapter : Transition.TransitionListener {

        override fun onTransitionStart(transition: Transition) {

        }

        override fun onTransitionEnd(transition: Transition) {

        }

        override fun onTransitionCancel(transition: Transition) {

        }

        override fun onTransitionPause(transition: Transition) {

        }

        override fun onTransitionResume(transition: Transition) {

        }
    }

}


