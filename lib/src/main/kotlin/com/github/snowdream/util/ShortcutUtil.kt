package com.github.snowdream.util


import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.ShortcutIconResource
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Created by hui.yang on 2015/11/19.
 * see: http://blog.zanlabs.com/2015/03/14/android-shortcut-summary/
 * see: https://gist.github.com/waylife/437a3d98a84f245b9582
 */
class ShortcutUtil private constructor() {

    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {
        private val TAG = "ShortcutUtil"

        /**
         * @param context
         * *            执行者。
         * *
         * @params pkg 待添加快捷方式的应用包名，其值不可为null。
         * *
         * @return 返回true为正常执行完毕，
         * *         返回false为pkg值为null或者找不到该应用或者该应用无用于Launch的MainActivity 。
         * *
         * @author sodino
         * *
         */
        @JvmStatic fun addShortcutByPackageName(context: Context, pkg: String): Boolean {
            // 快捷方式名
            var title = "unknown"
            // MainActivity完整名
            var mainAct: String? = null
            // 应用图标标识
            var iconIdentifier = 0
            // 根据包名寻找MainActivity
            val pkgMag = context.packageManager
            val queryIntent = Intent(Intent.ACTION_MAIN, null)
            queryIntent.addCategory(Intent.CATEGORY_LAUNCHER)// 重要，添加后可以进入直接已经打开的页面
            queryIntent.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            queryIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)

            val list = pkgMag.queryIntentActivities(queryIntent,
                    PackageManager.GET_ACTIVITIES)
            for (i in list.indices) {
                val info = list[i]
                if (info.activityInfo.packageName == pkg) {
                    title = info.loadLabel(pkgMag).toString()
                    mainAct = info.activityInfo.name
                    iconIdentifier = info.activityInfo.applicationInfo.icon
                    break
                }
            }
            if (mainAct == null) {
                // 没有启动类
                return false
            }
            val shortcut = Intent(
                    "com.android.launcher.action.INSTALL_SHORTCUT")
            // 快捷方式的名称
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title)
            // 不允许重复创建
            shortcut.putExtra("duplicate", false)
            val comp = ComponentName(pkg, mainAct)
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                    queryIntent.setComponent(comp))
            // 快捷方式的图标
            var pkgContext: Context? = null
            if (context.packageName == pkg) {
                pkgContext = context
            } else {
                // 创建第三方应用的上下文环境，为的是能够根据该应用的图标标识符寻找到图标文件。
                try {
                    pkgContext = context.createPackageContext(pkg,
                            Context.CONTEXT_IGNORE_SECURITY or Context.CONTEXT_INCLUDE_CODE)
                } catch (e: NameNotFoundException) {
                    e.printStackTrace()
                }

            }
            if (pkgContext != null) {
                val iconRes = ShortcutIconResource.fromContext(pkgContext, iconIdentifier)
                shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes)
            }
            // 发送广播，让接收者创建快捷方式
            // 需权限<uses-permission
            // android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
            context.sendBroadcast(shortcut)
            return true
        }

        /**
         * 新建快捷方式到桌面
         * 请在对应Intent的Activity中添加
         *  intent-filter action
         * android:name="android.intent.action.MAIN"

         * @param shortcutName
         * *            快捷方式名
         * *
         * @param actionIntent
         * *            快捷方式操作
         * *
         * @param icon
         * *            快捷方式图标
         * *
         * @param allowRepeat
         * *            是否允许重复生成
         */
        @JvmStatic fun addShortcut(context: Context, shortcutName: String,
                        actionIntent: Intent, icon: ShortcutIconResource, allowRepeat: Boolean) {
            val shortcutIntent = Intent(
                    "com.android.launcher.action.INSTALL_SHORTCUT")
            shortcutIntent.putExtra("duplicate", allowRepeat)// 是否允许重复创建
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName)// 快捷方式的标题
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon)// 快捷方式的图标
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent)// 快捷方式的动作
            context.sendBroadcast(shortcutIntent)// 发送广播
        }

        /**
         * 新建快捷方式到桌面
         * 请在对应Intent的Activity中添加
         *  intent-filter action
         * android:name="android.intent.action.MAIN"

         * @param shortcutName
         * *            快捷方式名
         * *
         * @param actionIntent
         * *            快捷方式操作
         * *
         * @param icon
         * *            快捷方式图标
         * *
         * @param allowRepeat
         * *            是否允许重复生成
         */
        @JvmStatic fun addShortcut(context: Context, shortcutName: String,
                        actionIntent: Intent, icon: Bitmap, allowRepeat: Boolean) {
            val shortcutIntent = Intent(
                    "com.android.launcher.action.INSTALL_SHORTCUT")
            shortcutIntent.putExtra("duplicate", allowRepeat)// 是否允许重复创建
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName)// 快捷方式的标题
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon)// 快捷方式的图标
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent)// 快捷方式的动作
            context.sendBroadcast(shortcutIntent)// 发送广播
        }

        /**
         * 删除桌面快捷方式

         * @param shortcutName
         * *            快捷方式名
         * *
         * @param actionIntent
         * *            快捷方式操作
         * *
         * @param isDuplicate
         * *            为true时循环删除快捷方式（即存在很多相同的快捷方式）
         */
        @JvmStatic fun deleteShortcut(context: Context, shortcutName: String,
                           actionIntent: Intent, isDuplicate: Boolean) {
            val shortcutIntent = Intent(
                    "com.android.launcher.action.UNINSTALL_SHORTCUT")
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName)// 快捷方式的标题
            shortcutIntent.putExtra("duplicate", isDuplicate)// 是否循环删除，比如有很多一样的快捷方式
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent)// 快捷方式的动作
            context.sendBroadcast(shortcutIntent)// 发送广播
        }

        /**
         * 检查快捷方式是否存在
         * 注意： 有些手机无法判断是否已经创建过快捷方式
         * 因此，在创建快捷方式时，请添加
         * shortcutIntent.putExtra("duplicate", false);// 不允许重复创建
         * 最好使用[.isShortCutExist]
         * 进行判断，因为可能有些应用生成的快捷方式名称是一样的的
         * 此处需要在AndroidManifest.xml中配置相关的桌面权限信息
         * 错误信息已捕获
         */
        @JvmStatic fun isShortCutExist(context: Context, title: String): Boolean {
            var result = false
            try {
                val cr = context.contentResolver
                val uriStr = StringBuilder()
                var authority: String? = LauncherUtil.getAuthorityFromPermissionDefault(context)
                if (authority == null || authority.trim { it <= ' ' } == "") {
                    authority = LauncherUtil.getAuthorityFromPermission(context, LauncherUtil.getCurrentLauncherPackageName(context) + ".permission.READ_SETTINGS")
                }
                uriStr.append("content://")
                if (TextUtils.isEmpty(authority)) {
                    val sdkInt = android.os.Build.VERSION.SDK_INT
                    if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                        uriStr.append("com.android.launcher.settings")
                    } else if (sdkInt < 19) {// Android 4.4以下
                        uriStr.append("com.android.launcher2.settings")
                    } else {// 4.4以及以上
                        uriStr.append("com.android.launcher3.settings")
                    }
                } else {
                    uriStr.append(authority)
                }
                uriStr.append("/favorites?notify=true")
                val uri = Uri.parse(uriStr.toString())
                val c = cr.query(uri, arrayOf("title"),
                        "title=? ",
                        arrayOf(title), null)
                if (c != null && c.count > 0) {
                    result = true
                }
                if (c != null && !c.isClosed) {
                    c.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                result = false
            }

            return result
        }

        /**
         * 不一定所有的手机都有效，因为国内大部分手机的桌面不是系统原生的
         * 更多请参考[.isShortCutExist]
         * 桌面有两种，系统桌面(ROM自带)与第三方桌面，一般只考虑系统自带
         * 第三方桌面如果没有实现系统响应的方法是无法判断的，比如GO桌面
         * 此处需要在AndroidManifest.xml中配置相关的桌面权限信息
         * 错误信息已捕获
         */
        @JvmStatic fun isShortCutExist(context: Context, title: String, intent: Intent): Boolean {
            var result = false
            try {
                val cr = context.contentResolver
                val uriStr = StringBuilder()
                var authority: String? = LauncherUtil.getAuthorityFromPermissionDefault(context)
                if (authority == null || authority.trim { it <= ' ' } == "") {
                    authority = LauncherUtil.getAuthorityFromPermission(context, LauncherUtil.getCurrentLauncherPackageName(context) + ".permission.READ_SETTINGS")
                }
                uriStr.append("content://")
                if (TextUtils.isEmpty(authority)) {
                    val sdkInt = android.os.Build.VERSION.SDK_INT
                    if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                        uriStr.append("com.android.launcher.settings")
                    } else if (sdkInt < 19) {// Android 4.4以下
                        uriStr.append("com.android.launcher2.settings")
                    } else {// 4.4以及以上
                        uriStr.append("com.android.launcher3.settings")
                    }
                } else {
                    uriStr.append(authority)
                }
                uriStr.append("/favorites?notify=true")
                val uri = Uri.parse(uriStr.toString())
                val c = cr.query(uri, arrayOf("title", "intent"),
                        "title=?  and intent=?",
                        arrayOf(title, intent.toUri(0)), null)
                if (c != null && c.count > 0) {
                    result = true
                }
                if (c != null && !c.isClosed) {
                    c.close()
                }
            } catch (ex: Exception) {
                result = false
                ex.printStackTrace()
            }

            return result
        }

        /**
         * 更新桌面快捷方式图标，不一定所有图标都有效
         * 如果快捷方式不存在，则不更新.
         */
        @JvmStatic fun updateShortcutIcon(context: Context, title: String, intent: Intent, bitmap: Bitmap) {
            if (bitmap == null) {
                Log.i(TAG, "update shortcut icon,bitmap empty")
                return
            }
            try {
                val cr = context.contentResolver
                val uriStr = StringBuilder()
                var urlTemp = ""
                var authority: String? = LauncherUtil.getAuthorityFromPermissionDefault(context)
                if (authority == null || authority.trim { it <= ' ' } == "") {
                    authority = LauncherUtil.getAuthorityFromPermission(context, LauncherUtil.getCurrentLauncherPackageName(context) + ".permission.READ_SETTINGS")
                }
                uriStr.append("content://")
                if (TextUtils.isEmpty(authority)) {
                    val sdkInt = android.os.Build.VERSION.SDK_INT
                    if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                        uriStr.append("com.android.launcher.settings")
                    } else if (sdkInt < 19) {// Android 4.4以下
                        uriStr.append("com.android.launcher2.settings")
                    } else {// 4.4以及以上
                        uriStr.append("com.android.launcher3.settings")
                    }
                } else {
                    uriStr.append(authority)
                }
                urlTemp = uriStr.toString()
                uriStr.append("/favorites?notify=true")
                val uri = Uri.parse(uriStr.toString())
                val c = cr.query(uri, arrayOf("_id", "title", "intent"),
                        "title=?  and intent=? ",
                        arrayOf(title, intent.toUri(0)), null)
                var index = -1
                if (c != null && c.count > 0) {
                    c.moveToFirst()
                    index = c.getInt(0)//获得图标索引
                    val cv = ContentValues()
                    cv.put("icon", flattenBitmap(bitmap))
                    val uri2 = Uri.parse("$urlTemp/favorites/$index?notify=true")
                    val i = context.contentResolver.update(uri2, cv, null, null)
                    context.contentResolver.notifyChange(uri, null)//此处不能用uri2，是个坑
                    Log.i(TAG, "update ok: affected $i rows,index is$index")
                } else {
                    Log.i(TAG, "update result failed")
                }
                if (c != null && !c.isClosed) {
                    c.close()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }


        @JvmStatic private fun flattenBitmap(bitmap: Bitmap): ByteArray? {
            // Try go guesstimate how much space the icon will take when serialized
            // to avoid unnecessary allocations/copies during the write.
            val size = bitmap.width * bitmap.height * 4
            val out = ByteArrayOutputStream(size)
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
                return out.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }
    }
}
