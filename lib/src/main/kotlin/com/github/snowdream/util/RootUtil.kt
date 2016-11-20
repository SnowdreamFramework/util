package com.github.snowdream.util

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * Created by hui.yang on 2015/11/16.
 * see: http://stackoverflow.com/a/8097801
 */
class RootUtil private constructor() {
    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {

        /**
         * Check whether the root is rooted.

         * @return
         */
        @JvmStatic fun isDeviceRooted(): Boolean = checkRootMethod1() || checkRootMethod2() || checkRootMethod3()

        private fun checkRootMethod1(): Boolean {
            val buildTags = android.os.Build.TAGS
            return buildTags != null && buildTags.contains("test-keys")
        }

        private fun checkRootMethod2(): Boolean {
            val paths = arrayOf("/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su")
            for (path in paths) {
                if (File(path).exists()) return true
            }
            return false
        }

        private fun checkRootMethod3(): Boolean {
            var process: Process? = null
            try {
                process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
                val `in` = BufferedReader(InputStreamReader(process!!.inputStream))
                if (`in`.readLine() != null) return true
                return false
            } catch (t: Throwable) {
                return false
            } finally {
                if (process != null) process.destroy()
            }
        }
    }
}
