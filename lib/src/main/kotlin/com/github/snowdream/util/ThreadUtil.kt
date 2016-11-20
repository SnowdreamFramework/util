package com.github.snowdream.util

import android.os.Looper

/**
 * Created by hui.yang on 2015/10/21.
 * see: http://stackoverflow.com/a/8714032
 */
class ThreadUtil private constructor() {
    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {
        @JvmStatic fun isOnUIThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

        @JvmStatic fun isOnNonUIThread(): Boolean = Looper.myLooper() != Looper.getMainLooper()
    }
}
