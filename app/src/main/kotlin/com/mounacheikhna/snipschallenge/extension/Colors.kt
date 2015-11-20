package com.mounacheikhna.snipschallenge.extension

import android.graphics.Bitmap
import android.support.annotation.ColorInt
import android.support.v7.graphics.Palette

/*
val IS_LIGHT = 0
val IS_DARK = 1
val LIGHTNESS_UNKNOWN = 2

*/
/**
 * Checks if the most populous color in the given palette is dark
 *//*

fun isDark(palette: Palette): Int {
    val mostPopulous = getMostPopulousSwatch(palette) ?: return LIGHTNESS_UNKNOWN
    return if (isDark(mostPopulous.hsl)) IS_DARK else IS_LIGHT
}

*/
/**
 * Check that the lightness value (0–1)
 *//*

fun isDark(hsl: FloatArray): Boolean {
    // @Size(3)
    return hsl[2] < 0.5f
}

fun getMostPopulousSwatch(palette: Palette?): Palette.Swatch? {
    var mostPopulous: Palette.Swatch? = null
    if (palette != null) {
        for (swatch in palette.swatches) {
            if (mostPopulous == null || swatch.population > mostPopulous.population) {
                mostPopulous = swatch
            }
        }
    }
    return mostPopulous
}

*/
/**
 * Determines if a given bitmap is dark. This extracts a palette inline so should not be called
 * with a large image!! If palette fails then check the color of the specified pixel
 *//*

fun isDark(bitmap: Bitmap, backupPixelX: Int, backupPixelY: Int): Boolean {
    // first try palette with a small color quant size
    val palette = Palette.from(bitmap).maximumColorCount(3).generate()
    if (palette != null && palette.swatches.size > 0) {
        return isDark(palette) == IS_DARK
    } else {
        // if palette failed, then check the color of the specified pixel
        return isDark(bitmap.getPixel(backupPixelX, backupPixelY))
    }
}

*/
/**
 * Convert to HSL & check that the lightness value
 *//*

fun isDark(@ColorInt color: Int): Boolean {
    val hsl = FloatArray(3)
    android.support.v4.graphics.ColorUtils.colorToHSL(color, hsl)
    return isDark(hsl)
}
*/
