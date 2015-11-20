package com.mounacheikhna.snipschallenge.photo

import android.graphics.*
import com.squareup.picasso.Transformation;

/**
 * A picasso transformation to add small round corners to images.
 */
class RoundedCornersTransformation(val radius: Int, val margin: Int) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        var paint = Paint()
        paint.isAntiAlias = true
        paint.setShader(BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))

        var output = Bitmap.createBitmap(source.width, source.height,
            Bitmap.Config.ARGB_8888)
        var canvas = Canvas(output)
        canvas.drawRoundRect(
            RectF(margin.toFloat(), margin.toFloat(), source.width.toFloat() - margin,
                source.height.toFloat() - margin), radius.toFloat(), radius.toFloat(), paint)

        if (source != output) {
            source.recycle()
        }

        return output;
    }

    override fun key(): String? {
        return "rounded(radius=$radius, margin=$margin)";
    }

}
