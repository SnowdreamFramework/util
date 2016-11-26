package com.github.snowdream.util

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build

/**
 * Created by snowdream on 16/11/26.
 */
class BitmapUtil private constructor() {
    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {
        /**
         * Get the size in bytes of a bitmap in a BitmapDrawable. Note that from Android 4.4 (KitKat)
         * onward this returns the allocated memory size of the bitmap which can be larger than the
         * actual bitmap data byte count (in the case it was re-used).

         * @param bitmapdrawable
         * *
         * @return size in bytes
         */
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @JvmStatic fun getBitmapSize(bitmapdrawable: BitmapDrawable): Int {
            val bitmap = bitmapdrawable.getBitmap()

            return getBitmapSize(bitmap)
        }

        /**
         * Get the size in bytes of a bitmap in a BitmapDrawable. Note that from Android 4.4 (KitKat)
         * onward this returns the allocated memory size of the bitmap which can be larger than the
         * actual bitmap data byte count (in the case it was re-used).

         * @param bitmapdrawable
         * *
         * @return size in bytes
         */
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @JvmStatic fun getBitmapSize(bitmap: Bitmap): Int {

            // From KitKat onward use getAllocationByteCount() as allocated bytes can potentially be
            // larger than bitmap byte count.
            if (APIUtil.hasKitKat()) {
                return bitmap.getAllocationByteCount()
            }

            if (APIUtil.hasHoneycombMR1()) {
                return bitmap.getByteCount()
            }

            // Pre HC-MR1
            return bitmap.getRowBytes() * bitmap.getHeight()
        }

    }


}