package jp.shiita.gamesample

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun FragmentManager.add(@IdRes id: Int, fragment: Fragment) = beginTransaction()
    .add(id, fragment)
    .commit()

fun FragmentManager.replace(@IdRes id: Int, fragment: Fragment, tag: String? = null) =
    beginTransaction()
        .replace(id, fragment)
        .addToBackStack(tag)
        .commit()

fun <T, R> List<T>.scanL(initial: R, operation: (R, T) -> R): List<R> =
    scanL(listOf(initial), operation)

private tailrec fun <T, R> List<T>.scanL(scanned: List<R>, operation: (R, T) -> R): List<R> =
    if (isEmpty()) scanned
    else drop(1).scanL(scanned + listOf(operation(scanned.last(), first())), operation)

fun Drawable.getBitmap(): Bitmap = when(this) {
    is BitmapDrawable -> bitmap
    else -> {
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        bitmap
    }
}
