package com.github.snowdream.util

/**
 * Created by yanghui.yangh on 2016-07-14.
 */
class DualSimInfo {

    var simId_1: Int? = null
    var simId_2: Int? = null
    var imsi_1: String? = null
    var imsi_2: String? = null
    var imei_1: String? = null
    var imei_2: String? = null
    var phoneType_1: Int? = null
    var phoneType_2: Int? = null
    var defaultImsi: String? = null
    var platformType: Int? = null

    companion object {
        val PLATORM_QUALCOMM = 0
        val PLATORM_MTK = 1
    }
}
