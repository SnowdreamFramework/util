package com.github.snowdream.util

import android.os.Build

/**
 * Created by snowdream on 16/11/26.
 */
class APIUtil private constructor() {
    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {
        /**
         * June 2010: Android 2.2
         */
        @JvmStatic fun hasFroyo(): Boolean {
            // Can use static final constants like FROYO, declared in later versions
            // of the OS since they are inlined at compile time. This is guaranteed behavior.
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
        }

        /**
         * November 2010: Android 2.3
         */
        @JvmStatic fun hasGingerbread(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
        }

        /**
         * February 2011: Android 3.0
         */
        @JvmStatic fun hasHoneycomb(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
        }

        /**
         * May 2011: Android 3.1
         */
        @JvmStatic fun hasHoneycombMR1(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
        }

        /**
         * June 2012: Android 4.1
         */
        @JvmStatic fun hasJellyBean(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
        }

        /**
         * October 2013: Android 4.4
         */
        @JvmStatic fun hasKitKat(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        }

        /**
         * November 2014: Android 5.0
         */
        @JvmStatic fun hasLollipop(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        }


        /**
         * October 2015: Android 6.0
         */
        @JvmStatic fun hasMarshmallow(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        }

        /**
         * August 2016: Android 7.0
         */
        @JvmStatic fun hasNougat(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        }
    }

}