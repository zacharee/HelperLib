package tk.zwander.helperlib

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue

/**
 * Context
 */

fun Context.dpAsPx(dpVal: Number) =
        Math.round(
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal.toFloat(), resources.displayMetrics)
        )

/**
 * Drawable
 */

fun Drawable.toBitmap(): Bitmap? {
    return when (this) {
        is BitmapDrawable -> bitmap
        else -> {
            (if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            } else {
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            })?.apply {
                val canvas = Canvas(this)
                setBounds(0, 0, canvas.width, canvas.height)
                draw(canvas)
            }
        }
    }
}

/**
 * Stolen from HalogenOS
 * https://github.com/halogenOS/android_frameworks_base/blob/XOS-8.1/packages/SystemUI/src/com/android/systemui/tuner/LockscreenFragment.java
 */
fun Drawable.toBitmapDrawable(resources: Resources): BitmapDrawable? {
    if (this is BitmapDrawable) return this

    val canvas = Canvas()
    canvas.drawFilter = PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG, Paint.FILTER_BITMAP_FLAG)

    return try {
        val bmp = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bmp)

        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)

        BitmapDrawable(resources, bmp)
    } catch (e: IllegalArgumentException) {
        null
    }
}