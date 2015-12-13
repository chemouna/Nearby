package com.mounacheikhna.nearby.ui.details

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.app.SharedElementCallback
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.os.Parcelable
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.transition.Transition
import android.util.AttributeSet
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.bindView
import com.mounacheikhna.nearby.FoursquareApp
import com.mounacheikhna.nearby.R
import com.mounacheikhna.nearby.ui.VenueDetailsActivity
import com.mounacheikhna.nearby.ui.VenueResult
import com.mounacheikhna.nearby.util.Colors
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import javax.inject.Inject

/**
 * A {@link View} to display details of a venue.
 */
public class VenueDetailsView : LinearLayout {

    val toolbar: Toolbar by bindView(R.id.toolbar)
    val mainImage: ImageView by bindView(R.id.main_image)
    val venueFab: ImageView by bindView(R.id.venue_fab)
    val venueDescription: TextView by bindView(R.id.venue_description)
    val containerPhoneNumber: ViewGroup by bindView(R.id.venue_phone_number_container)
    val venuePhoneNumber: TextView by bindView(R.id.venue_phone_number)
    val venueHours: TextView by bindView(R.id.venue_hours)
    val containerHours: ViewGroup by bindView(R.id.venue_hours_container)

    val collapsingTitle: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)
    private val SCRIM_ADJUSTMENT = 0.075f
    lateinit var supportActionBar: ActionBar
    lateinit var window: Window

    @Inject lateinit var picasso: Picasso

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
        LayoutInflater.from(context).inflate(R.layout.venue_details_view, this, true)
        orientation = VERTICAL
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    fun bind(activity: AppCompatActivity) {
        FoursquareApp.appComponent.inject(this)

        activity.delegate.setSupportActionBar(toolbar)
        supportActionBar = activity.supportActionBar
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);

        window = activity.window
        activity.setExitSharedElementCallback(fabSharedElementCallback)
        activity.window.sharedElementReturnTransition.addListener(venueReturnHomeListener)

        val venueResult: VenueResult? = activity.intent.getParcelableExtra<VenueResult>(
            VenueDetailsActivity.EXTRA_VENUE) ?: return
        populateWithVenueItem(activity, venueResult)
    }

    private fun populateWithVenueItem(activity: AppCompatActivity, venueResult: VenueResult?) {
        collapsingTitle.title = venueResult!!.venue.name

        if (!TextUtils.isEmpty(venueResult.bestPhotoUrl)) {
            picasso.load(venueResult.bestPhotoUrl)
                .into(mainImage, PaletteCallback())
        }

        activity.postponeEnterTransition()
        mainImage.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    mainImage.viewTreeObserver.removeOnPreDrawListener(this)
                    enterAnimation()
                    activity.startPostponedEnterTransition()
                    return true
                }
            })

        venueDescription.text = venueResult.venue.description
        if (venueResult.venue.contact != null && !TextUtils.isEmpty(
            venueResult.venue.contact?.formattedPhone)) {
            containerPhoneNumber.visibility = View.VISIBLE
            venuePhoneNumber.text = venueResult.venue.contact?.formattedPhone
        }

        if (venueResult.venue.hours != null) {
            containerHours.visibility = View.VISIBLE
            val openText = if (venueResult.venue.hours?.isOpen ?: false) resources.getString(
                R.string.open) else resources.getString(R.string.closed)
            val status = venueResult.venue.hours?.status;
            val formattedStatus = if(status == null || "null".equals(status))  "" else status
            venueHours.text = "$openText $formattedStatus"
        }
    }

    private fun enterAnimation() {
        val showFab = ObjectAnimator.ofPropertyValuesHolder(venueFab,
            PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f))
        showFab.startDelay = 300L
        showFab.setDuration(300L)
        showFab.interpolator = AnimationUtils.loadInterpolator(context,
            android.R.interpolator.linear_out_slow_in)
        showFab.start()
    }

    inner class PaletteCallback : Callback.EmptyCallback() {
        override fun onSuccess() {
            super.onSuccess()
            val bitmap = (mainImage.drawable as BitmapDrawable).bitmap;

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
                    applyToStatusBar(isDark, palette)
                    applyToBackButton(isDark)
                    applyToToolbar(palette)
                }

            Palette.from(bitmap)
                .clearFilters()
                .generate { palette ->
                    mainImage.foreground = createRipple(palette, 0.3f, 0.6f,
                        ContextCompat.getColor(context, R.color.medium_grey), true)
                }

            mainImage.background = null
        }

        private fun applyToToolbar(palette: Palette) {
            val primaryDark = ContextCompat.getColor(context, R.color.primary_dark);
            val primary = ContextCompat.getColor(context, R.color.primary);
            collapsingTitle.setContentScrimColor(palette.getMutedColor(primary));
            collapsingTitle.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        }

        /**
         * Darkens back button image of the toolbar on images that are primary of light color.
         */
        private fun applyToBackButton(isDark: Boolean) {
            if (!isDark) {
                val colorFilter = PorterDuffColorFilter(R.color.primary_dark_icon,
                    PorterDuff.Mode.MULTIPLY);
                var upArrow = ContextCompat.getDrawable(context,
                    R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                upArrow.colorFilter = colorFilter;
                supportActionBar.setHomeAsUpIndicator(upArrow);
            }
        }

        /**
         * Applies palette to status bar with a light or dark color on M
         * and with status bar icons matching the same color.
         */
        private fun applyToStatusBar(isDark: Boolean, palette: Palette?) {
            var statusBarColor = window.statusBarColor
            val topColor = Colors.getMostPopulousSwatch(palette)
            if (topColor != null && (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                statusBarColor = Colors.scrimify(topColor.rgb, isDark, SCRIM_ADJUSTMENT)
                if (!isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setLightStatusBar(mainImage)
                }
            }

            if (statusBarColor != window.statusBarColor) {
                val statusBarColorAnim = ValueAnimator.ofArgb(window.statusBarColor, statusBarColor)
                statusBarColorAnim.addUpdateListener { animation -> window.statusBarColor = animation.animatedValue as Int }
                statusBarColorAnim.setDuration(1000)
                statusBarColorAnim.interpolator = AnimationUtils.loadInterpolator(
                    context, android.R.interpolator.fast_out_slow_in)
                statusBarColorAnim.start()
            }
        }
    }

    /**
     * Creates a ripple using palette colors.
     */
    fun createRipple(palette: Palette,
                     @FloatRange(from = 0.0, to = 1.0) darkAlpha: Float,
                     @FloatRange(from = 0.0, to = 1.0) lightAlpha: Float,
                     @ColorInt fallbackColor: Int,
                     bounded: Boolean): RippleDrawable {
        var rippleColor = fallbackColor
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
            venueFab.visibility = View.INVISIBLE
            mainImage.elevation = 1f
        }
    }

    open class TransitionListenerAdapter : Transition.TransitionListener {

        override fun onTransitionStart(transition: Transition) {}

        override fun onTransitionEnd(transition: Transition) {}

        override fun onTransitionCancel(transition: Transition) {}

        override fun onTransitionPause(transition: Transition) {}

        override fun onTransitionResume(transition: Transition) {}
    }

}