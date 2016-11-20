/*
 * Copyright (C) 2014 Snowdream Mobile <yanghui1986527@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.snowdream.util

import android.content.Context
import android.text.TextUtils
import android.util.Log

/**
 * Created by hui.yang on 2014/11/9.
 */
class BuildConfigUtil private constructor() {

    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {
        private val DEBUG = "DEBUG"
        private val APPLICATION_ID = "APPLICATION_ID"
        private val BUILD_TYPE = "BUILD_TYPE"
        private val FLAVOR = "FLAVOR"
        private val VERSION_CODE = "VERSION_CODE"
        private val VERSION_NAME = "VERSION_NAME"


        @Deprecated("")
        //@Deprecated("Use {@link #APPLICATION_ID}")
        private val PACKAGE_NAME = "PACKAGE_NAME"

        private val TAG = "BuildConfigUtil"

        /**
         * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
         * are used at the project level to set custom fields.

         * @param context   Used to find the correct file
         * *
         * @param fieldName The name of the field-to-access
         * *
         * @return The value of the field, or `null` if the field is not found.
         */
        @JvmStatic fun <T> getBuildConfigValue(context: Context, fieldName: String?): T? {
            if (TextUtils.isEmpty(fieldName)) {
                Log.w(TAG,"The fileName is null or empty.")
                return null
            }

            try {
                val clazz = Class.forName(context.packageName + ".BuildConfig")
                val field = clazz.getField(fieldName)
                return field.get(null) as T
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            return null
        }

        /**
         * BuildConfig.Debug

         * @param context
         * *
         * @return
         */
        @JvmStatic fun isDebug(context: Context): Boolean? {
            return getBuildConfigValue<Boolean>(context, DEBUG)
        }

        /**
         * BuildConfig.APPLICATION_ID

         * @param context
         * *
         * @return
         */
        @JvmStatic fun getApplicationId(context: Context): String? {
            return getBuildConfigValue(context, APPLICATION_ID)
        }

        /**
         * BuildConfig.PACKAGE_NAME

         * @param context
         * *
         * @return
         */
        @Deprecated("")
        @JvmStatic fun getPackageName(context: Context): String? {
            return getBuildConfigValue(context, PACKAGE_NAME)
        }

        /**
         * BuildConfig.BUILD_TYPE

         * @param context
         * *
         * @return
         */
        @JvmStatic fun getBuildType(context: Context): String? {
            return getBuildConfigValue(context, BUILD_TYPE)
        }

        /**
         * BuildConfig.FLAVOR

         * @param context
         * *
         * @return
         */
        @JvmStatic fun getFlavor(context: Context): String? {
            return getBuildConfigValue(context, FLAVOR)
        }

        /**
         * BuildConfig.VersionName

         * @param context
         * *
         * @return
         */
        @JvmStatic fun getVersionName(context: Context): String? {
            return getBuildConfigValue(context, VERSION_NAME)
        }

        /**
         * BuildConfig.VERSION_CODE

         * @param context
         * *
         * @return
         */
        @JvmStatic fun getVersionCode(context: Context): Int? {
            return getBuildConfigValue<Int>(context, VERSION_CODE)
        }
    }
}
