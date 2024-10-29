package com.kenning.kcutil.utils.math

import android.text.Editable
import android.widget.EditText
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*
import java.util.regex.Pattern

/**
 * description：数值工具类
 * author: csh
 * date: ON 2021/1/18
 */
private object MathUtils {
    //BigDecimal.ROUND_HALF_UP  四舍五入
    //BigDecimal.ROUND_HALF_DOWN  五舍六入
    //BigDecimal.ROUND_UP   舍入远离零的舍入模式 此舍入模式始终不会减少计算值的大小
    //BigDecimal.ROUND_DOWN   接近零的舍入模式 此舍入模式始终不会增加计算值的大小
    //BigDecimal.ROUND_CEILING  接近正无穷大的舍入模式 如果 BigDecimal 为正，则舍入行为与 ROUND_UP 相同  如果为负，则舍入行为与 ROUND_DOWN 相同  此舍入模式始终不会减少计算值
    //BigDecimal.ROUND_FLOOR    接近负无穷大的舍入模式

    private val default_DecimalDigit = 20


    private fun formatNumber_Outside(str:String):String{
        // 西班牙用逗号做小数
        val nf: NumberFormat = NumberFormat.getInstance(Locale.forLanguageTag("es"))
        val parsedNumber: Number? = nf.parse(str)
        return if (parsedNumber == null) "0"
        else parsedNumber.toString()
    }

    private fun isNumeric_Outside(str: Any?): Boolean{
        val lan = Locale.getDefault().language
        if (lan != "es"){
            return false
        }
        try {
            if (str == null) return false
            var reg="^([+-]?)\\d*\\,?\\d+$"// number
            var reg_zz="^([+|-]?\\d+(,{0}|,\\d+))[Ee]{1}([+|-]?\\d+)$"//
            return Pattern.compile(reg).matcher(str.toString()).matches() ||
                    Pattern.compile(reg_zz).matcher(str.toString()).matches()
        } catch (e: Exception) {
            return false
        }
    }

    /**判断是否为数字*/
    fun isNumeric(str: Any?): Boolean {
        try {
            if (str == null) return false
            var reg="^([+-]?)\\d*\\.?\\d+$"// number
            var reg_zz="^([+|-]?\\d+(.{0}|.\\d+))[Ee]{1}([+|-]?\\d+)$"//
            return Pattern.compile(reg).matcher(str.toString()).matches() ||
                    Pattern.compile(reg_zz).matcher(str.toString()).matches()
        } catch (e: Exception) {
            return false
        }
    }

    fun ObjectToDouble(o: Any?): Double {
        return try {
            if (o == null)  0.0
            if (isNumeric_Outside(o)){
                formatNumber_Outside(o.toString()).toDouble()
            }else if (isNumeric(o)) {
                o.toString().toDouble()
            }else{
                0.0
            }
        } catch (e: java.lang.Exception) {
            0.0
        }
    }

    fun ObjectToFloat(o: Any?): Float {
        return try {
            if (o == null)  0f
            if (isNumeric_Outside(o)){
                formatNumber_Outside(o.toString()).toFloat()
            }else if (isNumeric(o)) {
                o.toString().toFloat()
            }else 0f
        } catch (e: java.lang.Exception) {
            0f
        }
    }

    fun ObjectToInt(o: Any?): Int {
        return try {
            if (o == null)  0
            if (isNumeric_Outside(o)){
                var result = formatNumber_Outside(o.toString()).toDouble().toInt()
                if (result == null) 0
                else result
            }else if (isNumeric(o)) {
                var result = o.toString().toDouble().toInt()
                if (result == null) 0
                else result
            }else 0
        } catch (e: java.lang.Exception) {
            0
        }
    }

    fun ObjectToLong(o: Any?): Long {
        return try {
            if (o == null)  0
            if (isNumeric_Outside(o)){
                formatNumber_Outside(o.toString()).toDouble().toLong()
            }else if (isNumeric(o)) {
                o.toString().toDouble().toLong()
            }else 0
        } catch (e: java.lang.Exception) {
            0
        }
    }

    /**
     * 数值字符串进行格式的方法，解决出现科学计数法的问题
     *
     * @param s
     * @param scale
     * @return
     */
    private fun NumberFormat(str: String?, scale: Int): String {
        var s = str
        if (isNumeric_Outside(s)){
            val nf = NumberFormat.getInstance(Locale.forLanguageTag("es"))
            nf.isGroupingUsed = false
            nf.maximumFractionDigits = scale
            return nf.format(ObjectToDouble(s)).replace(",", ".")
        }else if (isNumeric(s)) {
            val nf = NumberFormat.getInstance(Locale.CHINA)
            nf.isGroupingUsed = false
            nf.maximumFractionDigits = scale
            return nf.format(ObjectToDouble(s))
        } else {
            return "0"
        }
    }

    /**
     * 字符串Null值处理
     * @param ifnullString  字符串'null' 是否需要处理为""（空字符串）  默认处理
     */
    fun CleanNull(str: Any?, ifnullString: Boolean = true): String {
        var str = str
        if (str == null || ("null" == str && ifnullString)) {
            str = ""
        }
        return str.toString()
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    fun StringCleanZero(str: String?): String {
        var s = str
        s = CleanNull(s)
        s = s.replace("^(0+)", "")
        if (s.indexOf(".") > 0) {
            s = s.replace("0+?$".toRegex(), "") // 去掉多余的0
            s = s.replace("[.]$".toRegex(), "") // 如最后一位是.则去掉
        }
        s = s.replace("^(0+)".toRegex(), "")
        //数值字符串进行格式的方法，解决出现科学计数法的问题
        s = NumberFormat(s, default_DecimalDigit)

        return s
    }

    /**
     * 提供精确的小数位四舍五入处理(round_mode)
     *
     * @param s          需要四舍五入的数字
     * @param scale      小数点后保留几位
     * @param round_mode 指定的舍入模式
     * @return 四舍五入后的结果，以字符串格式返回
     */
    private fun round(s: String?, scale: Int, round_mode: Int): String {
        require(scale >= 0) { "The scale must be a positive integer or zero" }
        val b = BigDecimal(s)
        return b.setScale(scale, round_mode).toString()
    }

    /**
     * 提供精确的小数位四舍五入处理,舍入模式采用（四舍五入）
     * 举例，1.25保留1位小数：1.3
     *
     * @param s     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果，以字符串格式返回
     */
    private fun round_Half_Up(str: String?, scale: Int): String { //保留小数位数（四舍五入）
        var s = str
        s = CleanNull(s)
        s = round(s, scale, BigDecimal.ROUND_HALF_UP)
        //数值字符串进行格式的方法，解决出现科学计数法的问题
        s = NumberFormat(s, scale)
        return s
    }

    /**
     * 四舍五入保留固定小数位数后，并且去除后面的0
     *
     * @param s
     * @param scale
     * @return
     */
    fun BaoliuCleanZero(str: Any?, scale: Int): String {
        var s = str
        s = CleanNull(s)
        if (isNumeric_Outside(s)) {
            s = round_Half_Up(formatNumber_Outside(s), scale)
            s = StringCleanZero(s)
        }else if (isNumeric(s)) {
            s = round_Half_Up(s, scale)
            s = StringCleanZero(s)
        } else {
            s = "0"
        }
        return s
    }

    /**加法运算*/
    fun JIA(values: Any?, vararg os: Any?): String {
        var bd = BigDecimal.valueOf(ObjectToDouble(values))
        val s = os[0] as Array<Any>
        for (i in s.indices) {
            val d = ObjectToDouble(s[i])
            bd = bd.add(BigDecimal.valueOf(d))
        }
        return bd.toString()
    }

    /**减法*/
    fun JIAN(values: Any?, vararg os: Any?): String {
        var bd = BigDecimal.valueOf(ObjectToDouble(values))
        val s = os[0] as Array<Any>
        for (i in s.indices) {
            val d = ObjectToDouble(s[i])
            bd = bd.subtract(BigDecimal.valueOf(d))
        }
        return bd.toString()
    }

    /**乘法*/
    fun CHENG(valuse: Any?, vararg os: Any?): String {
        var bd = BigDecimal.valueOf(ObjectToDouble(valuse))
        val s = os[0] as Array<Any>
        for (i in 0 until s.size ) {
            val d = ObjectToDouble(s[i])
            if (d == 0.0) {
                return "0"
            }
            bd = bd.multiply(BigDecimal.valueOf(d))
        }
        return bd.toString()
    }

    /**double的除法*/
    fun CHU(values: Any?, vararg os: Any?): String {
        var bd = BigDecimal.valueOf(ObjectToDouble(values))
        val s = os[0] as Array<Any>
        for (i in s.indices) {
            val d = ObjectToDouble(s[i])
            if (d == 0.0) {
                return "0"
            }
            bd = bd.divide(
                    BigDecimal.valueOf(d),
                    default_DecimalDigit,
                    BigDecimal.ROUND_HALF_UP
            )
        }
        return bd.toString()
    }

}

//string
fun String?.clearZero():String = MathUtils.StringCleanZero(this)
fun Any?.clearZero():String = MathUtils.StringCleanZero("$this")
/**数值类型的字符串 保留小数位数*/
fun Any?.keepPoint(point:Int):String = MathUtils.BaoliuCleanZero(this, point)
//
/**
 * @param ifnullString  字符串'null' 是否需要处理为""（空字符串）  默认处理
 * */
fun Any?.ClearNull(ifnullString:Boolean = true):String = MathUtils.CleanNull(this, ifnullString)
fun Any?.isNumeric():Boolean = MathUtils.isNumeric(this)
fun Any?.toDouble_():Double = MathUtils.ObjectToDouble(this)
fun Any?.toLong_():Long = MathUtils.ObjectToLong(this)
fun Any?.toInt_():Int = MathUtils.ObjectToInt(this)
fun Any?.toFloat_():Float = MathUtils.ObjectToFloat(this)

fun Any?.JIA(vararg strs:Any?,point:Int):String = MathUtils.JIA(this, strs).toString().keepPoint(point)
fun Any?.JIA(vararg strs:Any?):String = MathUtils.JIA(this, strs).toString().clearZero()

fun Any?.JIAN(vararg strs:Any?,point:Int):String = MathUtils.JIAN(this, strs).toString().keepPoint(point)
fun Any?.JIAN(vararg strs:Any?):String = MathUtils.JIAN(this, strs).toString().clearZero()

fun Any?.CHU(vararg strs:Any?,point:Int):String = MathUtils.CHU(this, strs).toString().keepPoint(point)
fun Any?.CHU(vararg strs:Any?):String = MathUtils.CHU(this, strs).toString().clearZero()

fun Any?.CHENG(vararg strs:Any?,point:Int):String = MathUtils.CHENG(this, strs).toString().keepPoint(point)
fun Any?.CHENG(vararg strs:Any?):String = MathUtils.CHENG(this, strs).toString().clearZero()
/**edittext 处理小数位数的控制*/
fun Editable?.ROUND_UP(point:Int):Unit{
    if (this.toString().startsWith(".")) {
        this?.clear()
        this?.append("0.")
    }
    var str = this.toString()
    if (str.contains(".")) {
        if (str.substring(str.indexOf(".") + 1, str.length).length > point) {
            this?.clear()
            this?.append(str.substring(0, str.indexOf(".") + 1 + point))
        }
    }
}

fun EditText?.ROUND_UP(point:Int):Unit{
    if (this.toString().startsWith(".")) {
        this?.setText("0.")
        this?.setSelection(this.text.toString().length)
    }
    var str = this?.text.toString()
    if (str.contains(".")) {
        if (str.substring(str.indexOf(".") + 1, str.length).length > point) {
            this?.setText(str.substring(0, str.indexOf(".") + 1 + point))
            this?.setSelection(this.text.toString().length)
        }
    }
}

/**edittext 现有文本中的小数位数 是否 超出*/
fun  Editable?.isOutPoint(point:Int):Boolean{
    var str = this.toString()
    if (str.contains(".")) {
        if (str.substring(str.indexOf(".") + 1, str.length).length > point) {
            return true
        }
    }
    return false
}