package com.mounacheikhna.snipschallenge.util;

import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ColorUtil {

  public static final int IS_LIGHT = 0;
  public static final int IS_DARK = 1;
  public static final int LIGHTNESS_UNKNOWN = 2;


  private ColorUtil() {}

  /**
   * Checks if the most populous color in the given palette is dark
   * <p/>
   * Annoyingly we have to return this Lightness 'enum' rather than a boolean as palette isn't
   * guaranteed to find the most populous color.
   */
  public static @Lightness int isDark(Palette palette) {
    Palette.Swatch mostPopulous = getMostPopulousSwatch(palette);
    if (mostPopulous == null) return LIGHTNESS_UNKNOWN;
    return isDark(mostPopulous.getHsl()) ? IS_DARK : IS_LIGHT;
  }

  public static @Nullable
  Palette.Swatch getMostPopulousSwatch(Palette palette) {
    Palette.Swatch mostPopulous = null;
    if (palette != null) {
      for (Palette.Swatch swatch : palette.getSwatches()) {
        if (mostPopulous == null || swatch.getPopulation() > mostPopulous.getPopulation()) {
          mostPopulous = swatch;
        }
      }
    }
    return mostPopulous;
  }

  /**
   * Determines if a given bitmap is dark. This extracts a palette inline so should not be called
   * with a large image!!
   * <p/>
   * Note: If palette fails then check the color of the central pixel
   */
  public static boolean isDark(@NonNull Bitmap bitmap) {
    return isDark(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
  }

  /**
   * Determines if a given bitmap is dark. This extracts a palette inline so should not be called
   * with a large image!! If palette fails then check the color of the specified pixel
   */
  public static boolean isDark(@NonNull Bitmap bitmap, int backupPixelX, int backupPixelY) {
    // first try palette with a small color quant size
    Palette palette = Palette.from(bitmap).maximumColorCount(3).generate();
    if (palette != null && palette.getSwatches().size() > 0) {
      return isDark(palette) == IS_DARK;
    } else {
      // if palette failed, then check the color of the specified pixel
      return isDark(bitmap.getPixel(backupPixelX, backupPixelY));
    }
  }

  /**
   * Check that the lightness value (0–1)
   */
  public static boolean isDark(float[] hsl) { // @Size(3)
    return hsl[2] < 0.5f;
  }

  /**
   * Convert to HSL & check that the lightness value
   */
  public static boolean isDark(@ColorInt int color) {
    float[] hsl = new float[3];
    android.support.v4.graphics.ColorUtils.colorToHSL(color, hsl);
    return isDark(hsl);
  }

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({IS_LIGHT, IS_DARK, LIGHTNESS_UNKNOWN})
  public @interface Lightness {
  }

}
