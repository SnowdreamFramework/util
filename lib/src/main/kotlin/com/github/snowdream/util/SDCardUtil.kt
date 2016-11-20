package com.github.snowdream.util

import android.content.Context
import android.os.Environment
import android.os.storage.StorageManager
import android.text.TextUtils
import java.io.File
import java.lang.reflect.Array
import java.lang.reflect.InvocationTargetException

/**
 * Created by yanghui.yangh on 2016-07-11.
 */
class SDCardUtil private constructor() {
    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {

        @JvmStatic fun getAvailableStorageDirectory(mContext: Context): File? {
            val externalPath = getStoragePath(mContext, true)
            if (!TextUtils.isEmpty(externalPath)) {
                return File(externalPath!!)
            }

            val internalPath = getStoragePath(mContext, false)
            if (!TextUtils.isEmpty(internalPath)) {
                return File(internalPath!!)
            }

            return null
        }

        @JvmStatic fun getExternalStorageDirectory(mContext: Context): File? {
            val path = getStoragePath(mContext, true)
            if (TextUtils.isEmpty(path)) return null

            return File(path!!)
        }

        @JvmStatic fun getInternalStorageDirectory(mContext: Context): File? {
            val path = getStoragePath(mContext, false)
            if (TextUtils.isEmpty(path)) return null

            return File(path!!)
        }

        private fun getStoragePath(mContext: Context, is_removale: Boolean): String? {

            val mStorageManager = mContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            var storageVolumeClazz: Class<*>? = null
            try {
                storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
                val getVolumeList = mStorageManager.javaClass.getMethod("getVolumeList")
                val getVolumeState = StorageManager::class.java.getMethod(
                        "getVolumeState", String::class.java)
                val getPath = storageVolumeClazz!!.getMethod("getPath")
                val isRemovable = storageVolumeClazz.getMethod("isRemovable")
                val result = getVolumeList.invoke(mStorageManager)
                val length = Array.getLength(result)
                for (i in 0..length - 1) {
                    val storageVolumeElement = Array.get(result, i)
                    val path = getPath.invoke(storageVolumeElement) as String
                    val state = getVolumeState.invoke(mStorageManager,
                            getPath.invoke(storageVolumeElement)) as String

                    if (!state.equals(Environment.MEDIA_MOUNTED, ignoreCase = true)) continue

                    val removable = isRemovable.invoke(storageVolumeElement) as Boolean
                    if (is_removale == removable) {
                        return path
                    }
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            return null
        }
    }

}
