package com.github.snowdream.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils

/**
 * Created by hui.yang on 2015/11/19.
 * see: http://blog.zanlabs.com/2015/03/14/android-shortcut-summary/
 * see: https://gist.github.com/waylife/437a3d98a84f245b9582
 */
class LauncherUtil private constructor() {

    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {
        private var mBufferedValue: String = ""

        /**
         * get the current Launcher's Package Name
         */
        @JvmStatic fun getCurrentLauncherPackageName(context: Context): String {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            val res = context.packageManager.resolveActivity(intent, 0)
            if (res == null || res.activityInfo == null) {
                // should not happen. A home is always installed, isn't it?
                return ""
            }
            if (res.activityInfo.packageName == "android") {
                return ""
            } else {
                return res.activityInfo.packageName
            }
        }

        /**
         * default permission is "com.android.launcher.permission.READ_SETTINGS"
         * [.getAuthorityFromPermission]

         * @param context
         */
        @JvmStatic fun getAuthorityFromPermissionDefault(context: Context): String {
            if (TextUtils.isEmpty(mBufferedValue)) {
                //we get value buffered
                mBufferedValue = getAuthorityFromPermission(context, "com.android.launcher.permission.READ_SETTINGS")
            }

            return mBufferedValue
        }

        /**
         * be cautious to use this, it will cost about 500ms 此函数为费时函数，大概占用500ms左右的时间
         * android系统桌面的基本信息由一个launcher.db的Sqlite数据库管理，里面有三张表
         * 其中一张表就是favorites。这个db文件一般放在data/data/com.android.launcher(launcher2)文件的databases下
         * 但是对于不同的rom会放在不同的地方
         * 例如MIUI放在data/data/com.miui.home/databases下面
         * htc放在data/data/com.htc.launcher/databases下面/

         * @param context
         * *
         * @param permission 读取设置的权限  READ_SETTINGS_PERMISSION
         * *
         * @return
         */
        @JvmStatic fun getAuthorityFromPermission(context: Context, permission: String): String {
            if (TextUtils.isEmpty(permission)) {
                return ""
            }

            try {
                val packs = context.packageManager.getInstalledPackages(PackageManager.GET_PROVIDERS) ?: return ""
                for (pack in packs) {
                    val providers = pack.providers
                    if (providers != null) {
                        for (provider in providers) {
                            if (permission == provider.readPermission || permission == provider.writePermission) {
                                return provider.authority
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }
    }
}
