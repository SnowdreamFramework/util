package com.github.snowdream.util

import android.content.Context
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.lang.ref.SoftReference

/**
 * Created by yanghui.yangh on 2016-07-14.
 */
class SimUtil private constructor() {
    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {
        private var reference: SoftReference<DualSimInfo>? = null

        /**
         * Whether the phone use DualSim

         * @param context
         * *
         * @return
         */
        @JvmStatic fun isDualSim(context: Context): Boolean {
            return getMtkDualSimInfo(context, true) != null || getQualcommDualSimInfo(context, true) != null
        }


        /**
         * Get DualSimInfo,if any.

         * @param context
         * *
         * @return
         */
        @JvmStatic fun getDualSimInfo(context: Context): DualSimInfo? {
            if (isDualSim(context)) {
                return dualSimInfoFromCache
            }

            return null
        }


        /**
         * Whether the phone use MTK DualSim

         * @param context
         * *
         * @param isCache
         * *
         * @return
         */
        @JvmStatic fun getMtkDualSimInfo(context: Context, isCache: Boolean): DualSimInfo? {
            if (isCache && dualSimInfoFromCache != null) {
                return dualSimInfoFromCache
            }

            var dualSimInfo: DualSimInfo? = DualSimInfo()
            try {
                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val c = Class.forName("com.android.internal.telephony.Phone")
                val fields1 = c.getField("GEMINI_SIM_1")
                fields1.isAccessible = true
                dualSimInfo!!.simId_1 = fields1.get(null) as Int
                val fields2 = c.getField("GEMINI_SIM_2")
                fields2.isAccessible = true
                dualSimInfo.simId_2 = fields2.get(null) as Int
                val m = TelephonyManager::class.java.getDeclaredMethod(
                        "getSubscriberIdGemini", Integer.TYPE)
                dualSimInfo.imsi_1 = m.invoke(tm,
                        dualSimInfo.simId_1) as String
                dualSimInfo.imsi_2 = m.invoke(tm,
                        dualSimInfo.simId_2) as String

                val m1 = TelephonyManager::class.java.getDeclaredMethod(
                        "getDeviceIdGemini", Integer.TYPE)
                dualSimInfo.imei_1 = m1.invoke(tm,
                        dualSimInfo.simId_1) as String
                dualSimInfo.imei_2 = m1.invoke(tm,
                        dualSimInfo.simId_2) as String

                val mx = TelephonyManager::class.java.getDeclaredMethod(
                        "getPhoneTypeGemini", Integer.TYPE)
                dualSimInfo.phoneType_1 = mx.invoke(tm,
                        dualSimInfo.simId_1) as Int
                dualSimInfo.phoneType_2 = mx.invoke(tm,
                        dualSimInfo.simId_2) as Int
                dualSimInfo.platformType = DualSimInfo.PLATORM_MTK

                if (TextUtils.isEmpty(dualSimInfo.imsi_1) && !TextUtils.isEmpty(dualSimInfo.imsi_2)) {
                    dualSimInfo.defaultImsi = dualSimInfo.imsi_2
                }
                if (TextUtils.isEmpty(dualSimInfo.imsi_2) && !TextUtils.isEmpty(dualSimInfo.imsi_1)) {
                    dualSimInfo.defaultImsi = dualSimInfo.imsi_1
                }

                if (isCache) {
                    reference = SoftReference<DualSimInfo>(dualSimInfo)
                }
            } catch (e: Exception) {
                dualSimInfo = null
            }

            return dualSimInfo
        }

        /**
         * Whether the phone use Qualcomm DualSim

         * @param context
         * *
         * @param isCache
         * *
         * @return
         */
        @JvmStatic fun getQualcommDualSimInfo(context: Context, isCache: Boolean): DualSimInfo? {
            if (isCache && dualSimInfoFromCache != null) {
                return dualSimInfoFromCache
            }

            var dualSimInfo: DualSimInfo? = DualSimInfo()
            dualSimInfo!!.simId_1 = 0
            dualSimInfo.simId_2 = 1
            try {
                val cx = Class.forName("android.telephony.MSimTelephonyManager")
                val obj = context.getSystemService("phone_msim")

                val md = cx.getMethod("getDeviceId", Integer.TYPE)
                val ms = cx.getMethod("getSubscriberId", Integer.TYPE)

                dualSimInfo.imei_1 = md.invoke(obj,
                        dualSimInfo.simId_1) as String
                dualSimInfo.imei_2 = md.invoke(obj,
                        dualSimInfo.simId_2) as String
                dualSimInfo.imsi_1 = ms.invoke(obj,
                        dualSimInfo.simId_1) as String
                dualSimInfo.imsi_2 = ms.invoke(obj,
                        dualSimInfo.simId_2) as String
                dualSimInfo.platformType = DualSimInfo.PLATORM_QUALCOMM


                if (TextUtils.isEmpty(dualSimInfo.imsi_1) && !TextUtils.isEmpty(dualSimInfo.imsi_2)) {
                    dualSimInfo.defaultImsi = dualSimInfo.imsi_2
                }
                if (TextUtils.isEmpty(dualSimInfo.imsi_2) && !TextUtils.isEmpty(dualSimInfo.imsi_1)) {
                    dualSimInfo.defaultImsi = dualSimInfo.imsi_1
                }

                if (isCache) {
                    reference = SoftReference<DualSimInfo>(dualSimInfo)
                }
            } catch (e: Exception) {
                dualSimInfo = null
            }

            return dualSimInfo
        }

        private val dualSimInfoFromCache: DualSimInfo?
            get() {
                if (reference != null && reference!!.get() != null) {
                    return reference!!.get()
                }

                return null
            }
    }
}
