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
import android.util.DisplayMetrics
import android.util.TypedValue

class DensityUtil private constructor() {
    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {

        /**
         * see [DensityUtil.applyDimension]
         */
        @Deprecated("")
        @JvmStatic fun dip2px(context: Context, dipValue: Int): Int {
            val scale = context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }

        /**
         * see [DensityUtil.applyDimension]
         */
        @Deprecated("")
        @JvmStatic fun px2dip(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

        /**
         * see [DensityUtil.applyDimension]
         */
        @Deprecated("")
        @JvmStatic fun sp2px(context: Context, spValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }

        /**
         * see [DensityUtil.applyDimension]
         */
        @Deprecated("")
        @JvmStatic fun px2sp(context: Context, pxValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }

        /**
         * Conversion in dp、dip、sp、pt、px、mm、in according to the device

         * @param context    Context
         * *
         * @param srcUnit    src unit,see [TypedValue]
         * *
         * @param srcValue   src value
         * *
         * @param targetUnit target unit,see [TypedValue]
         * *
         * @return target value with targetUnit
         */
        @JvmStatic fun applyDimension(context: Context, srcUnit: Int, srcValue: Float, targetUnit: Int): Float {
            var targetValue = 0f

            val metrics = context.resources.displayMetrics

            val pxvalue = TypedValue.applyDimension(srcUnit, srcValue, metrics)

            when (targetUnit) {
                TypedValue.COMPLEX_UNIT_DIP -> targetValue = pxvalue / metrics.density
                TypedValue.COMPLEX_UNIT_SP -> targetValue = pxvalue / metrics.scaledDensity
                TypedValue.COMPLEX_UNIT_PX -> targetValue = pxvalue
                TypedValue.COMPLEX_UNIT_MM -> targetValue = pxvalue * 25.4f / metrics.xdpi
                TypedValue.COMPLEX_UNIT_IN -> targetValue = pxvalue / metrics.xdpi
                TypedValue.COMPLEX_UNIT_PT -> targetValue = pxvalue * 72 / metrics.xdpi
                else -> targetValue = pxvalue
            }

            return targetValue
        }

        /**
         * Convert dp、dip、sp、pt、px、mm、in to px according to the device.
         * It is the same as: [TypedValue.applyDimension(int, float, DisplayMetrics)][TypedValue.applyDimension]

         * @param context    Context
         * *
         * @param srcUnit    src unit,see [TypedValue]
         * *
         * @param srcValue   src value
         * *
         * @return target value  px
         */
        @JvmStatic fun applyDimension(context: Context, srcUnit: Int, srcValue: Float): Float {
            val metrics = context.resources.displayMetrics

            return TypedValue.applyDimension(srcUnit, srcValue, metrics)
        }

        /**
         * Convert dp、dip、sp、pt、px、mm、in to px according to the device.
         * It is the same as: [TypedValue.applyDimension(int, float, DisplayMetrics)][TypedValue.applyDimension]

         * @param context    Context
         * *
         * @param srcUnit    src unit,see [TypedValue]
         * *
         * @param srcValue   src value
         * *
         * @return target value  px
         */
        @JvmStatic fun applyDimensionOffset(context: Context, srcUnit: Int, srcValue: Float): Int {
            val metrics = context.resources.displayMetrics

            return TypedValue.applyDimension(srcUnit, srcValue, metrics).toInt()
        }
    }
}
