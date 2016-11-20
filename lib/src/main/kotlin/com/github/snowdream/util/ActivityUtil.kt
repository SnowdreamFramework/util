package com.github.snowdream.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build

class ActivityUtil private constructor() {
    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {

        /**
         * Restart the Activity
         *
         * @param activity
         */
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @JvmStatic fun restartActivity(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                activity.recreate()
            } else {
                val intent = activity.intent
                activity.overridePendingTransition(0, 0)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                activity.finish()

                activity.overridePendingTransition(0, 0)
                activity.startActivity(intent)
            }
        }
    }
}
