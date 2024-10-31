package com.kenning.kcutil.utils.date

import android.annotation.SuppressLint
import com.kenning.kcutil.utils.date.Date_Format
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期格式处理类
 *
 * @author Wesley
 */
//@SuppressLint("SimpleDateFormat")
object Date_Format {
    /**
     * 日期格式3：dd
     */

    val sdf0 = SimpleDateFormat("dd")

    /**
     * 日期格式3：MM-dd HH:mm
     */
    val sdf1 = SimpleDateFormat("MM-dd HH:mm")

    /**
     * 日期格式2：HH:mm
     */
    val sdf2 = SimpleDateFormat("HH:mm")

    /**
     * 日期格式3：yyyy-MM-dd HH:mm:ss
     */
    val sdf3 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val sdf8 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期格式4:yyyy-MM-dd_HH:mm:ss
     */
    val sdf4 = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")

    /**
     * 日期格式5：yyyy-MM-dd HH:mm
     */
    val sdf5 = SimpleDateFormat("yyyy-MM-dd HH:mm")

    /**
     * 日期格式6：yyyy-MM-dd
     */
    val sdf6 = SimpleDateFormat("yyyy-MM-dd")

    /**
     * 日期格式6：yyyy-MM-dd
     */
    val YMD = SimpleDateFormat("yyyy-MM-dd")

    /**
     * 日期格式7：yyyyMMdd
     */
    val sdf7 = SimpleDateFormat("yyyyMMdd")

    /**
     * 日期格式8：yyyy-MM
     */
    val YM = SimpleDateFormat("yyyy-MM")

    /**
     * 日期格式9：yyyy年MM月dd日
     */
    val sdf9 = SimpleDateFormat("yyyy年MM月dd日")

    /**
     * 日期格式10：yyyy-MM-dd HH:mm:ss:SSS
     */
    val sdf10 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")

    /**
     * 日期格式9：yyyy年MM月dd日
     */
    val sdf11 = SimpleDateFormat("MM月dd日")

    /**
     * 日期格式12：yyyyMMddHHmmss
     */
    val sdf12 = SimpleDateFormat("yyyyMMddHHmmss")

    /**
     * 日期格式13：yyyyMMddHHmmssSSS
     */
    val sdf13 = SimpleDateFormat("yyyyMMddHHmmssSSS")

    /**
     * 日期格式14：yyyy-MM-dd HH:mm:ss.SSS
     */
    val sdf14 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

    /**
     * 日期格式15：HH:mm:ss
     */
    val sdf15 = SimpleDateFormat("HH:mm:ss")
}

infix fun Date.formatBy(format: SimpleDateFormat): String {
    return format.format(this)
}

fun Date.format2String(dateFormat: SimpleDateFormat = Date_Format.sdf6): String {
    return dateFormat.format(this)
}

infix fun String.parseBy(format: SimpleDateFormat): Date {
    return format.parse(this)
}