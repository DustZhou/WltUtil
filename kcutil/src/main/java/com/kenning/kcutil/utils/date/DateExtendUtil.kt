package com.kenning.kcutil.utils.date

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.kenning.kcutil.R
import com.kenning.kcutil.utils.date.Date_Format.sdf5
import com.kenning.kcutil.utils.date.Date_Format.sdf8
import com.kenning.kcutil.utils.math.CHENG
import com.kenning.kcutil.utils.math.CHU
import com.kenning.kcutil.utils.math.JIAN
import com.kenning.kcutil.utils.math.toInt_
import com.kenning.kcutil.utils.other.getStringResource
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *Description :日期使用类
 *@author : KenningChen
 *Date : 2022/2/26
 */
object DateExtendUtil {
    const val FIRST_DAY_OF_WEEK = Calendar.MONDAY // 中国周一是一周的第一天

    /**
     * 获取月份差
     *
     * @param currentDate
     * @param beginDate
     * @return
     */
    fun getMonthQty(currentDate: String, beginDate: String): Int {
        val sdf = SimpleDateFormat("yyyy-MM");
        val str1 = beginDate;
        val str2 = currentDate;
        val bef = Calendar.getInstance();
        val aft = Calendar.getInstance();
        try {
            bef.setTime(sdf.parse(str1));
            aft.setTime(sdf.parse(str2));
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        val result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        return result;
    }

    /**
     * 获得获取当前时间的指定时间
     */
    fun getCurrentTimeAfterHour(afterHour: Int): String {
        val c = Calendar.getInstance();
        val day = c.get(Calendar.HOUR);
        c.set(Calendar.HOUR, day + afterHour);
        return sdf5.format(c.getTime());
    }

    /**
     * 获得前几天的日期
     */
    fun GetBeforDate(day: Int): Date {
        val dNow = Date();   //当前时间
        var dBefore = Date();
        val calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -day);  //设置为前一天
        dBefore = calendar.getTime();   //得到前一天的时间
        return dBefore;
    }

    /**
     * 传入年月日，返回日历格式
     *
     * @param ymd 对应1999-10-01
     * @return
     */
    fun SETDATE(ymd: IntArray): String {
        return (ymd[0].toString() + "-" + (if (ymd[1] < 9) "0" + (ymd[1] + 1) else ymd[1] + 1)
                + "-" + if (ymd[2] < 10) "0" + ymd[2] else ymd[2])
    }

    fun setCalendarDate(): IntArray {
        val yearMonthDay = IntArray(3)
        // 时间
        val c = Calendar.getInstance()
        yearMonthDay[0] = c.get(Calendar.YEAR)
        yearMonthDay[1] = c.get(Calendar.MONTH)
        yearMonthDay[2] = c.get(Calendar.DAY_OF_MONTH)
        return yearMonthDay
    }

    /**
     * 时间差  秒
     */
    fun getSecondBetween2(hour1: String, hour2: String): Long {
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            /*小时差*/
            val fromDate2 = simpleFormat.parse(hour1);
            val toDate2 = simpleFormat.parse(hour2);
            val from2 = fromDate2.getTime();
            val to2 = toDate2.getTime();
            return ((to2 - from2) / (1000));
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return 0;
    }

    /**原百度获取时间部分机型获取不到，现在通过后台服务器返回*/
    fun beijingDate3(bjtime: String?, handler: Handler) {
        if (!bjtime.isNullOrEmpty()) {
            val msg = Message();
            msg.what = -11;
            val bd = Bundle();
            bd.putString("bjtime", bjtime);
            msg.setData(bd);
            handler.sendMessage(msg);
        } else {
            handler.sendEmptyMessage(1999);
        }
    }

    fun secondsToTime(seconds: Long): String {
        val hours: Long = TimeUnit.SECONDS.toHours(seconds)
        val minutes: Long = TimeUnit.SECONDS.toMinutes(seconds) % 60
        val secondsRemaining = seconds % 60
        val sBuilder = StringBuilder()
        if (hours > 0) {
            if (hours >= 10) {
                sBuilder.append(hours).append(":")
            } else {
                sBuilder.append(0).append(hours).append(":")
            }
        }
        if (minutes >= 10) {
            sBuilder.append(minutes).append(":")
        } else {
            sBuilder.append(0).append(minutes).append(":")
        }
        if (secondsRemaining >= 10) {
            sBuilder.append(secondsRemaining)
        } else {
            sBuilder.append(0).append(secondsRemaining)
        }
        return sBuilder.toString()
    }

    /**
     * 当天日期
     */
    @JvmOverloads
    fun getCurrentDate(format: SimpleDateFormat = Date_Format.YMD): String {
        // 当天日期
        return Date() formatBy format
    }

    /**
     * @param delay 往后推迟的天数，天
     * @return 推迟后的日期，string
     */
    fun getDateDelay(specifiedDay: String, delay: Int): String {
        val c = Calendar.getInstance();
        try {
            val date = SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
            c.setTime(date);
            val day = c.get(Calendar.DATE);
            c.set(Calendar.DATE, day + delay);
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        val dayAfter = SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }


    /**
     * @param delayMonth 往后推迟的天数，天
     * @return 推迟后的日期，string
     */
    fun getDateDelayByMonth(specifiedDay: String, delayMonth: Int): String {
        var c = Calendar.getInstance();
        var date: Date? = null;
        try {
            date = SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        c.setTime(date);
        val day = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, day + delayMonth);

        val dayAfter = SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }

    /**
     * @param data  初始时间，毫秒
     * @param delay 往后推迟的分钟数，分
     * @return 推迟后的日期，string
     */
    fun getTimeDelay(data: Long, delay: Int): String {
        val temp = data + (60000 * delay);
        val d = Date(temp);
        val format = Date_Format.sdf8.format(d);
        return format;
    }

    fun getHourMint(mint: String): String {
        val h = mint.CHU(60).toInt_()
        val m = mint.JIAN(h.CHENG(60)).toInt_()
        if (h == 0) {
            return mint + getStringResource(R.string.分钟);
        } else
            return "$h 时" + m + getStringResource(R.string.分钟);
    }

    fun getMintBetween2(hour1: String, hour2: String): Long {
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            /*小时差*/
            val fromDate2 = simpleFormat.parse(hour1);
            val toDate2 = simpleFormat.parse(hour2);
            val from2 = fromDate2.getTime();
            val to2 = toDate2.getTime();
            return ((to2 - from2) / (1000 * 60));
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return 0;
    }


    fun getHourMint2(timeStr: String): String {
        try {
            val pushDate = sdf8.parse(timeStr);
            val calendar = Calendar.getInstance();
            calendar.setTime(pushDate);
            return "${calendar.get(Calendar.HOUR_OF_DAY)}:" + calendar.get(Calendar.MINUTE);
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param delayMonth 往后推迟的天数，天
     * @return 推迟后的日期，string
     */
    fun getMonthDelayByMonth(specifiedDay: String, delayMonth: Int): String {
        val c = Calendar.getInstance();
        var date: Date? = null;
        try {
            date = SimpleDateFormat("yy-MM").parse(specifiedDay);
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        c.setTime(date);
        val day = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, day + delayMonth);

        val dayAfter = SimpleDateFormat("yyyy-MM").format(c.getTime());
        return dayAfter;
    }
    /**
     * parseDate
     *
     * @param strDate 时间 如 20210118
     * @param format 日期格式
     * @return Date格式
     */
    /**
     *
     * @param strDate
     * @return
     */
    @JvmOverloads
    fun parseDate(strDate: String, format: SimpleDateFormat = Date_Format.YMD): Date {
        return strDate parseBy format
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    fun getSpecifiedDayAfter(specifiedDay: String): String {
        val c = Calendar.getInstance();
        var date: Date? = null;
        try {
            date = SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        c.time = date;
        val day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        val dayAfter = SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    fun getSpecifiedDayAfter(specifiedDay: Date): String {
        val c = Calendar.getInstance();
        c.setTime(specifiedDay);
        val day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        val dayAfter = SimpleDateFormat("MM月dd日").format(c.getTime());
        return dayAfter;
    }

    fun getHourBetween(hour1: String, hour2: String): Long {
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            /*小时差*/
            val fromDate2 = simpleFormat.parse(hour1);
            val toDate2 = simpleFormat.parse(hour2);
            val from2 = fromDate2.getTime();
            val to2 = toDate2.getTime();
            return ((to2 - from2) / (1000 * 60 * 60));
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    fun getSpecifiedDayBefore(specifiedDay: String): String {
        val c = Calendar.getInstance();
        var date: Date? = null;
        try {
            date = SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        c.setTime(date);
        val day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        val dayBefore = SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }
    // <editor-fold desc="快消 ： 同比 环比 ">
    /**
     * @Description:获取任意天后的时间
     * @Params: time 指定的时间
     * @param:  day 1表示后一天 -1表示前一天
     * @Return
     */
    fun getBeferDay(format: SimpleDateFormat? = null, data: Date, day: Int): String {

        val calendar = Calendar.getInstance()

        calendar.time = data

        calendar.add(Calendar.DATE, day)

        val sdf = format ?: Date_Format.sdf6

        return sdf.format(calendar.time)

    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    fun getSpecifiedDayBefore(specifiedDay: Date): String {
        val c = Calendar.getInstance();
        c.setTime(specifiedDay);
        val day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        val dayBefore = SimpleDateFormat("MM月dd日").format(c.getTime());
        return dayBefore;
    }

    fun getMintBetween(hour1: String, hour2: String): Long {
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            /*分钟差*/
            val fromDate3 = simpleFormat.parse(hour1);
            val toDate3 = simpleFormat.parse(hour2);
            val from3 = fromDate3.getTime();
            val to3 = toDate3.getTime();
            return ((to3 - from3) / (1000 * 60));
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 取得日期：年
     *
     * @param date
     * @return
     */
    fun getYear(date: Date = Date()): Int {
        val c = Calendar.getInstance()
        c.time = date
        return c[Calendar.YEAR]
    }

    /**
     * 取得日期：月
     *
     * @param date
     * @return
     */
    fun getMonth(date: Date): Int {
        val c = Calendar.getInstance()
        c.time = date
        val month = c[Calendar.MONTH]
        return month + 1
    }

    /**
     * 取得日期：天(日)
     *
     * @param date
     * @return
     */
    fun getDay(date: Date?): Int {
        val c = Calendar.getInstance()
        c.time = date
        return c[Calendar.DAY_OF_MONTH]
    }

    /**
     *
     * @param type String 可选 星期 和 周
     * @return String 返回对应的 星期几 和 周几
     */
    fun getWeekDayStr(type: String, date: Date? = Date()): String {
        val cal = Calendar.getInstance()
        if (date == null) {
            cal.time = Date()
        } else {
            cal.time = date
        }
        return when (cal[Calendar.DAY_OF_WEEK]) {
            Calendar.SUNDAY -> "${type}日"
            Calendar.MONDAY -> "${type}一"
            Calendar.TUESDAY -> "${type}二"
            Calendar.WEDNESDAY -> "${type}三"
            Calendar.THURSDAY -> "${type}四"
            Calendar.FRIDAY -> "${type}五"
            Calendar.SATURDAY -> "${type}六"
            else -> ""
        }
    }

    /**
     * 获取日期对应一周中的星期几
     *
     * @param date
     * @return int
     */
    fun getDateOfWeek(date: Date = Date()): Int {
        val c = Calendar.getInstance()
        c.time = date
        val week_of_year = c[Calendar.DAY_OF_WEEK]
        return week_of_year - 1
    }

    /**
     * 获取当天日期是周(星期)几
     * @param type String 可选 星期 和 周
     * @return String 返回对应的 星期几 和 周几
     */
    fun getTodayOfWeekInfo(type: String): String {
        var TodayOfWeek = ""
        val cal = Calendar.getInstance()
        cal.time = Date()
        when (cal[Calendar.DAY_OF_WEEK]) {
            Calendar.SUNDAY -> TodayOfWeek = "${type}日"
            Calendar.MONDAY -> TodayOfWeek = "${type}一"
            Calendar.TUESDAY -> TodayOfWeek = "${type}二"
            Calendar.WEDNESDAY -> TodayOfWeek = "${type}三"
            Calendar.THURSDAY -> TodayOfWeek = "${type}四"
            Calendar.FRIDAY -> TodayOfWeek = "${type}五"
            Calendar.SATURDAY -> TodayOfWeek = "${type}六"
        }
        return TodayOfWeek
    }


    /**
     * 获取当前时间反推一个时间段的开始时间:例如一周前，一月前，三月前，半年前，一年前
     */
    fun getHistoryBeginDate(dateType: DateEnum): String {
        val calendar = Calendar.getInstance() // 得到日历
        calendar.time = Date()
        when (dateType) {
            DateEnum.TODAY -> {}
            DateEnum.YESTERDAY -> calendar.add(Calendar.DATE, -1)
            DateEnum.TWO_DAYS -> calendar.add(Calendar.DATE, -2)
            DateEnum.WEEK -> calendar.add(Calendar.DATE, -6)
            DateEnum.Next7Days -> calendar.add(Calendar.DATE, 6)
            DateEnum.Next30Days -> calendar.add(Calendar.DATE, 29)
            DateEnum.THIRTY_DAYS -> calendar.add(Calendar.DATE, -29)
            DateEnum.DAYS_180 -> calendar.add(Calendar.DATE, -179)
            DateEnum.MONTH -> calendar.add(Calendar.MONTH, -1)
            DateEnum.THREE_MONTHS -> calendar.add(Calendar.MONTH, -3)
            DateEnum.HALF_YEAR -> calendar.add(Calendar.MONTH, -6)
            DateEnum.YEAR -> calendar.add(Calendar.YEAR, -1)
            DateEnum.THISMONTH -> calendar[Calendar.DAY_OF_MONTH] = 1
            DateEnum.THISQUARTER -> {
                val month = getQuarterInMonth(calendar[Calendar.MONTH], true)
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            else -> {}
        }
        return calendar.time formatBy Date_Format.YMD
    }

    /**
     * 获取当前时间反推一个时间段的开始时间:例如一周前，一月前，三月前，半年前，一年前
     */
    fun getHistoryDate(dateType: DateEnum?): Date {
        val calendar = Calendar.getInstance() // 得到日历
        calendar.time = Date()
        when (dateType) {
            DateEnum.TODAY -> {}
            DateEnum.YESTERDAY -> calendar.add(Calendar.DATE, -1)
            DateEnum.TWO_DAYS -> calendar.add(Calendar.DATE, -2)
            DateEnum.WEEK -> calendar.add(Calendar.DATE, -6)
            DateEnum.Next7Days -> calendar.add(Calendar.DATE, 6)
            DateEnum.Next30Days -> calendar.add(Calendar.DATE, 29)
            DateEnum.THIRTY_DAYS -> calendar.add(Calendar.DATE, -29)
            DateEnum.DAYS_180 -> calendar.add(Calendar.DATE, -179)
            DateEnum.MONTH -> calendar.add(Calendar.MONTH, -1)
            DateEnum.THREE_MONTHS -> calendar.add(Calendar.MONTH, -3)
            DateEnum.HALF_YEAR -> calendar.add(Calendar.MONTH, -6)
            DateEnum.YEAR -> calendar.add(Calendar.YEAR, -1)
            DateEnum.THISMONTH -> calendar[Calendar.DAY_OF_MONTH] = 1
            DateEnum.THISQUARTER -> {
                val month = getQuarterInMonth(calendar[Calendar.MONTH], true)
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            else -> {}
        }
        return calendar.time
    }

    /**
     * 按月 获取当前日期(时间)的差额日期(时间)
     * @param baseDate 基准时间点
     * @param format 基准时间点的日期格式(必须匹配)
     * @param balance 偏移时间的长度
     * @param calculateDay 是否计算天数的时间
     * @param type 偏移类型
     * @param offset 二次偏移量 偏移时间单位为天
     * @param resultFormat 计算后的日期格式 默认与 基准点的时间格式一样
     */
    fun getBalanceDateByMonth(
        baseDate: String,
        format: SimpleDateFormat,
        balance: Int,
        calculateDay: Boolean,
        offset: Int,
        resultFormat: SimpleDateFormat = format
    ): String {
        val date = baseDate.parseBy(format)!!

        val calendar = Calendar.getInstance() //得到日历
        calendar.time = date //把当前时间赋给日历

        val days = getDay(date)//基准日期过了所在月份的几天了
        //初始化一个仅有日期没有天数的日历
        val time: String = calendar.time.formatBy(SimpleDateFormat("yyyyMM", Locale.CHINA))
        val calendarMonth = Calendar.getInstance()
        calendarMonth.time = time.parseBy(SimpleDateFormat("yyyyMM", Locale.CHINA))!! //把当前时间赋给日历

        calendarMonth.add(Calendar.MONTH, balance)
        if (calculateDay)
            calendarMonth.add(Calendar.DATE, days - 1)
        //重新赋值给原始日历
        calendar.time = calendarMonth.time
        val os = offset.toInt_()
        if (os != 0 && calculateDay)
            calendar.add(Calendar.DATE, os)
        return calendar.time.formatBy(resultFormat)
    }

    /**按天 获取当前日期(时间)的差额日期(时间)
     * @param baseDate 基准时间点
     * @param format 基准时间点的日期格式(必须匹配)
     * @param balance 偏移时间的长度
     * @param resultFormat 计算后的日期格式 默认与 基准点的时间格式一样
     */
    fun getBalanceDateByDay(
        baseDate: String,
        format: SimpleDateFormat,
        balance: Int,
        resultFormat: SimpleDateFormat = format
    ): String {
        val date = baseDate.parseBy(format)!!

        val calendar = Calendar.getInstance() //得到日历
        calendar.time = date //把当前时间赋给日历

        calendar.add(Calendar.DATE, balance)
        return calendar.time.formatBy(resultFormat)
    }

    // 返回第几个月份，不是几月
    // 季度一年四季， 第一季度：2月-4月， 第二季度：5月-7月， 第三季度：8月-10月， 第四季度：11月-1月
    private fun getQuarterInMonth(month: Int, isQuarterStart: Boolean): Int {
        var months = intArrayOf(1, 4, 7, 10)
        if (!isQuarterStart) {
            months = intArrayOf(3, 6, 9, 12)
        }
        return if (month in 2..4) months[0] else if (month in 5..7) months[1] else if (month in 8..10) months[2] else months[3]
    }

    /**
     * 根据日期获取是该日期是一年的第几周
     *
     * @param date
     * @return
     */
    fun getWeekOfYear(date: Date = Date()): Int {
        val c = Calendar.getInstance()
        c.time = date
        return c[Calendar.WEEK_OF_YEAR]
    }

    /**
     * 根据日期获取日期所在周的周一和周末的日期
     *
     * @param date
     * @param format
     * @return
     */
    fun getWeekBeginAndEndDate(date: Date = Date(), format: SimpleDateFormat = Date_Format.YMD):
            String {
        val monday = getMondayOfWeek(date)
        val sunday = getSundayOfWeek(date)
        return "${monday formatBy format} - ${sunday formatBy format}"
    }

    /**
     * 获得前几天的日期
     */
    fun getBeforDate(day: Int): Date {
        val dNow = Date();   //当前时间
        var dBefore = Date();
        val calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -day);  //设置为前一天
        dBefore = calendar.getTime();   //得到前一天的时间
        return dBefore;
    }

    /**
     * 月的最后一天
     * @param delayMonth 往后推迟的天数，天
     * @return 推迟后的日期，string
     */
    fun getDayDelayByMonth(specifiedDay: String, delayMonth: Int): String {
        val c = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yy-MM").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.time = date
        val day = c[Calendar.MONTH]
        c[Calendar.MONTH] = day + delayMonth
        return Date_Format.YMD.format(getLastDateOfMonth(c.time))
    }

    /**
     * 根据日期取得对应周周一日期
     *
     * @param date
     * @return
     */
    fun getMondayOfWeek(date: Date = Date()): Date {
        val monday = Calendar.getInstance()
        monday.time = date
        if (Build.VERSION.RELEASE.startsWith("4") ||
            Build.VERSION.RELEASE.startsWith("5")
        ) {
            monday.firstDayOfWeek = Calendar.SUNDAY
        } else {
            monday.firstDayOfWeek = FIRST_DAY_OF_WEEK
        }
        monday[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        return monday.time
    }

    /**
     * 根据日期取得对应周周日日期
     *
     * @param date
     * @return
     */
    fun getSundayOfWeek(date: Date = Date()): Date {
        val sunday = Calendar.getInstance()
        sunday.time = date
        if (Build.VERSION.RELEASE.startsWith("4") ||
            Build.VERSION.RELEASE.startsWith("5")
        ) {
            sunday.firstDayOfWeek = Calendar.SUNDAY
            sunday[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            return getNextNDay(6, sunday.time).parseBy(Date_Format.YMD)!!
        } else {
            sunday.firstDayOfWeek = FIRST_DAY_OF_WEEK
            sunday[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        }
        return sunday.time
    }

    /**
     * 取得年第一天
     *
     * @param date
     * @return
     */
    fun getFirstDateOfYear(date: Date = Date()): Date {
        val c = Calendar.getInstance()
        c.time = date
        c[Calendar.DAY_OF_YEAR] = c.getActualMinimum(Calendar.DAY_OF_YEAR)
        return c.time
    }

    /**
     * 取得年最后一天
     *
     * @param date
     * @return
     */
    fun getLastDateOfYear(date: Date = Date()): Date {
        val c = Calendar.getInstance()
        c.time = date
        c[Calendar.DAY_OF_YEAR] = c.getActualMaximum(Calendar.DAY_OF_YEAR)
        return c.time
    }

    /**
     * 取得月的剩余天数
     *
     * @param date
     * @return
     */
    fun getRemainDayOfMonth(date: Date = Date()): Int {
        val dayOfMonth = getTotalDayOfTheMonth(date)
        val day = getPassedDaysOnTheMonth(date)
        return dayOfMonth - day
    }

    /**
     * 取得月已经过的天数
     *
     * @param date
     * @return
     */
    fun getPassedDaysOnTheMonth(date: Date = Date()): Int {
        val c = Calendar.getInstance()
        c.time = date
        return c[Calendar.DAY_OF_MONTH]
    }

    /**
     * 取得月天数
     *
     * @param date
     * @return
     */
    fun getTotalDayOfTheMonth(date: Date = Date()): Int {
        val c = Calendar.getInstance()
        c.time = date
        return c.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    /**
     * 获取日期相差天数
     * @param t1 后面的日期， t2 开始日期
     * @retrue 天数
     */
    fun getCompareDates(t1: String, t2: String): Long {
        var days = 0L
        try {
            val date1 = Date_Format.sdf6.parse(t1);
            val date2 = Date_Format.sdf6.parse(t2);
            days = (date1.getTime() - date2.getTime()) / (1000 * 3600 * 24);
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * day 0为今天  1为明天，-1为一天前  ，-7为七天前
     *
     * @param day
     * @return
     */
    fun getSpecifyDate(day: Int): String {
        val calendar = Calendar.getInstance(); // 得到日历
        calendar.setTime(Date());
        if (day != 0) {
            calendar.add(Calendar.DATE, day);
        }
        return Date_Format.sdf6.format(calendar.getTime());

    }

    /**
     * 环比
     */
    fun getRingComparisonHistory(mStartTime: String, mEndTime: String, dateType: DateEnum?): ArrayList<String> {
        // 获取当前日历实例
        val mStartCalendar = Calendar.getInstance()
        val mEndCalendar = Calendar.getInstance()
        // 设置日历时间
        // 设置日历时间
        mStartCalendar.time = Date_Format.sdf6.parse(mStartTime)!!

        mEndCalendar.time = Date_Format.sdf6.parse(mEndTime)!!

        when (dateType) {
            //当前时间
            DateEnum.TODAY -> {}
            //昨天时间
            DateEnum.YESTERDAY -> {
                mStartCalendar.add(Calendar.DATE, -1)
                mEndCalendar.add(Calendar.DATE, -1)
            }
            //前天
            DateEnum.TWO_DAYS -> {
                mStartCalendar.add(Calendar.DATE, -2)
                mEndCalendar.add(Calendar.DATE, -2)
            }
            //上周
            DateEnum.LAST_WEEK -> {
                mStartCalendar.add(Calendar.DAY_OF_WEEK, -7)
                mEndCalendar.add(Calendar.DAY_OF_WEEK, -7)
            }
            //上月
            DateEnum.LAST_MONTH -> {

                mStartCalendar.add(Calendar.MONTH, -1)
                mEndCalendar.add(Calendar.MONTH, -1)

                mStartCalendar[Calendar.DAY_OF_MONTH] = mStartCalendar.getActualMinimum(Calendar.DAY_OF_MONTH)
                mEndCalendar[Calendar.DAY_OF_MONTH] = mEndCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            }
            //上季度
            DateEnum.LAST_QUARTER -> {

                mStartCalendar.add(Calendar.MONTH, -3)
                mEndCalendar.add(Calendar.MONTH, -3)

                val mStartMonth = getFirstDateOfMonth(getSeasonDate(mStartCalendar.time)[0])
                val mEndMonth = getLastDateOfMonth(getSeasonDate(mEndCalendar.time)[2])

                mStartCalendar.time = mStartMonth
                mEndCalendar.time = mEndMonth
            }
            //上一年
            DateEnum.LAST_YEAR -> {
                mStartCalendar.add(Calendar.YEAR, -1)
                mEndCalendar.add(Calendar.YEAR, -1)
            }

            DateEnum.OTHER -> {

                val time1 = mStartCalendar.timeInMillis

                val time2 = mEndCalendar.timeInMillis

                val mBetweenDays = ((time2 - time1) / (1000 * 3600 * 24)) + 1 //天数差

                mStartCalendar.add(Calendar.DATE, -mBetweenDays.toString().toInt_())

                mEndCalendar.add(Calendar.DATE, -mBetweenDays.toString().toInt_())

            }

            else -> {}
        }

        val dateString = ArrayList<String>()

        dateString.add(Date_Format.sdf6.format(mStartCalendar.time))

        dateString.add(Date_Format.sdf6.format(mEndCalendar.time))

        return dateString
    }

    /**
     * 时间比大小
     */
    fun timeCompare(t1: String, t2: String): Int {
        val formatter = SimpleDateFormat("HH:mm");
        val c1 = Calendar.getInstance();
        val c2 = Calendar.getInstance();
        try {
            c1.setTime(formatter.parse(t1));
            c2.setTime(formatter.parse(t2));
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        val result = c1.compareTo(c2);
        return result;
    }

    /**
     * 同比
     */
    fun getYoYHistory(mStartTime: String, mEndTime: String): ArrayList<String> {
        // 获取当前日历实例
        val mStartCalendar = Calendar.getInstance()

        val mEndCalendar = Calendar.getInstance()
        // 设置日历时间
        mStartCalendar.time = Date_Format.sdf6.parse(mStartTime)!!

        mEndCalendar.time = Date_Format.sdf6.parse(mEndTime)!!

        mStartCalendar.add(Calendar.YEAR, -1)

        mEndCalendar.add(Calendar.YEAR, -1)

        val dateString = ArrayList<String>()

        dateString.add(Date_Format.sdf6.format(mStartCalendar.time))

        dateString.add(Date_Format.sdf6.format(mEndCalendar.time))

        return dateString
    }

    /**
     * 获取日期相差天数
     * @param day1 开始日期  day2 后面的日期
     * @retrue 天数
     */
    fun getBetweenDays(day1: String, day2: String): Long {
        /*天数差*/
        try {
            val date1 = Date_Format.sdf6.parse(day1);
            val date2 = Date_Format.sdf6.parse(day2);
            return ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 取得月第一天
     *
     * @param date
     * @return
     */
    fun getFirstDateOfMonth(date: Date = Date()): Date {
        val c = Calendar.getInstance()
        c.time = date
        c[Calendar.DAY_OF_MONTH] = c.getActualMinimum(Calendar.DAY_OF_MONTH)
        return c.time
    }

    /**
     * 取得月最后一天
     *
     * @param date
     * @return
     */
    fun getLastDateOfMonth(date: Date = Date()): Date {
        val c = Calendar.getInstance()
        c.time = date
        c[Calendar.DAY_OF_MONTH] = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        return c.time
    }

    /**
     * 取得季度第一天
     *
     * @param date
     * @return
     */
    fun getFirstDateOfSeason(date: Date = Date()): Date {
        return getFirstDateOfMonth(getSeasonDate(date)[0])
    }

    /**
     * 取得季度最后一天
     *
     * @param date
     * @return
     */
    fun getLastDateOfSeason(date: Date = Date()): Date {
        return getLastDateOfMonth(getSeasonDate(date)[2])
    }

    /**
     * 取得季度天数
     *
     * @param date
     * @return
     */
    fun getDayOfSeason(date: Date): Int {
        var day = 0
        val seasonDates = getSeasonDate(date)
        for (date2 in seasonDates) {
            day += getTotalDayOfTheMonth(date2)
        }
        return day
    }

    /**
     * 取得季度剩余天数
     *
     * @param date
     * @return
     */
    fun getRemainDayOfSeason(date: Date): Int {
        return getDayOfSeason(date) - getPassDayOfSeason(date)
    }

    /**
     * 取得季度已过天数
     *
     * @param date
     * @return
     */
    fun getPassDayOfSeason(date: Date): Int {
        var day = 0
        val seasonDates = getSeasonDate(date)
        val c = Calendar.getInstance()
        c.time = date
        val month = c[Calendar.MONTH]
        if (month == Calendar.JANUARY || month == Calendar.APRIL || month == Calendar.JULY || month == Calendar.OCTOBER) { // 季度第一个月
            day = getPassedDaysOnTheMonth(seasonDates[0])
        } else if (month == Calendar.FEBRUARY || month == Calendar.MAY || month == Calendar.AUGUST || month == Calendar.NOVEMBER) { // 季度第二个月
            day = (getTotalDayOfTheMonth(seasonDates[0])
                    + getPassedDaysOnTheMonth(seasonDates[1]))
        } else if (month == Calendar.MARCH || month == Calendar.JUNE || month == Calendar.SEPTEMBER || month == Calendar.DECEMBER) { // 季度第三个月
            day = (getTotalDayOfTheMonth(seasonDates[0]) + getTotalDayOfTheMonth(seasonDates[1])
                    + getPassedDaysOnTheMonth(seasonDates[2]))
        }
        return day
    }

    /**
     * 取得季度月
     *
     * @param date
     * @return
     */
    fun getSeasonDate(date: Date = Date()): Array<Date> {
        val season = arrayOf(Date(), Date(), Date())
        val c = Calendar.getInstance()
        c.time = date
        //        c.set(Calendar.DAY_OF_MONTH, 1);
        val nSeason = getSeason(date)
        if (nSeason == 1) { // 第一季度
            c[Calendar.MONTH] = Calendar.JANUARY
            season[0] = c.time
            c[Calendar.MONTH] = Calendar.FEBRUARY
            season[1] = c.time
            c[Calendar.MONTH] = Calendar.MARCH
            season[2] = c.time
        } else if (nSeason == 2) { // 第二季度
            c[Calendar.MONTH] = Calendar.APRIL
            season[0] = c.time
            c[Calendar.MONTH] = Calendar.MAY
            season[1] = c.time
            c[Calendar.MONTH] = Calendar.JUNE
            season[2] = c.time
        } else if (nSeason == 3) { // 第三季度
            c[Calendar.MONTH] = Calendar.JULY
            season[0] = c.time
            c[Calendar.MONTH] = Calendar.AUGUST
            season[1] = c.time
            c[Calendar.MONTH] = Calendar.SEPTEMBER
            season[2] = c.time
        } else /*if (nSeason == 4)*/ { // 第四季度
            c[Calendar.MONTH] = Calendar.OCTOBER
            season[0] = c.time
            c[Calendar.MONTH] = Calendar.NOVEMBER
            season[1] = c.time
            c[Calendar.MONTH] = Calendar.DECEMBER
            season[2] = c.time
        }
        return season
    }

    /**
     *
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @param date
     * @return
     */
    fun getSeason(date: Date = Date()): Int {
        var season = 0
        val c = Calendar.getInstance()
        c.time = date
        val month = c[Calendar.MONTH]
        when (month) {
            Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH -> season = 1
            Calendar.APRIL, Calendar.MAY, Calendar.JUNE -> season = 2
            Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER -> season = 3
            Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER -> season = 4
            else -> {
            }
        }
        return season
    }

    /**获取当天的日期 */
    fun getTodayDateStr(format: SimpleDateFormat = Date_Format.YMD): String {
        return format.format(Date())
    }

    /**获取昨天的日期 */
    @JvmOverloads
    fun getYestodayDateStr(format: SimpleDateFormat = Date_Format.YMD): String {
        val calendar = Calendar.getInstance() // 得到日历
        calendar.time = Date()
        calendar.add(Calendar.DATE, -1)
        return format.format(calendar.time)
    }

    /**获取近7天对应的日期*/
    fun getNear7DaysDAteStr(format: SimpleDateFormat = Date_Format.YMD): String {
        val calendar = Calendar.getInstance() // 得到日历
        calendar.time = Date()
        calendar.add(Calendar.DATE, -6)
        return format.format(calendar.time)
    }

    /**获取近30天对应的日期*/
    fun getNear30DaysDAteStr(format: SimpleDateFormat = Date_Format.YMD): String {
        val calendar = Calendar.getInstance() // 得到日历
        calendar.time = Date()
        calendar.add(Calendar.DATE, -29)
        return format.format(calendar.time)
    }

    fun getNextNDay(N: Int, date: Date = Date(), format: SimpleDateFormat = Date_Format.YMD): String {
        val calendar = Calendar.getInstance() // 得到日历
        calendar.time = date
        calendar.add(Calendar.DATE, N)
        return format.format(calendar.time)
    }
//
//    fun getDateByDate(dif:Int,date:String):String{
//        val cal = Calendar.getInstance()
//        cal.time = Date_Format.YMD.parse(date)
//        cal.add(Calendar.DATE, dif)
//        return Date_Format.YMD.format(cal.time)
//    }

    /**
     * 获取时间差
     * @param format SimpleDateFormat lastTime startTime 的日期格式
     * @param type DifTimeType [DifTimeType]
     * @param lastTime String
     * @param startTime String
     * @return Int
     */
    fun getBetweenTime(
        format: SimpleDateFormat,
        type: DifTimeType,
        lastTime: String,
        startTime: String
    ): Int {
        return try {
            val cal = Calendar.getInstance()
            cal.time = format.parse(startTime)
            val time1 = cal.timeInMillis
            cal.time = format.parse(lastTime)
            val time2 = cal.timeInMillis
            val between_days = when (type) {
                DifTimeType.DAY -> (time2 - time1) / (1000 * 3600 * 24)//天数差
                DifTimeType.SECOND -> (time2 - time1) / 1000//秒差
                DifTimeType.MINUTES -> (time2 - time1) / (1000 * 60)// 分钟差
            }
            between_days.toString().toInt()
        } catch (e: java.lang.Exception) {
            -1
        }
    }


    /**
     * format date
     *
     * @param date
     * @return
     */
    fun formatDate(date: Date?): String {
        return formatDate(date, null)
    }

    /**
     * format date
     *
     * @param date
     * @param pattern
     * @return
     */
    fun formatDate(date: Date?, pattern: String?): String {
        var pattern = pattern
        var strDate = ""
        try {
            if (pattern == null) {
                pattern = "yyyy-MM-dd"
            }
            val format = SimpleDateFormat(pattern)
            strDate = format.format(date)
        } catch (e: Exception) {
//            logger.error("formatDate error:", e);
        }
        return strDate
    }

    /**时差类型*/
    enum class DifTimeType {
        /**天数差*/
        DAY,

        /**秒差*/
        SECOND,

        /**分钟差*/
        MINUTES
    }
}


fun DataOfCustomeBillDateType(CustomeBillDateType: String, callBack: (String, String) -> Unit) {
    var BeginDate = ""
    var EndDate = ""

    when (CustomeBillDateType) {

        DateEnum.TODAY.name -> {
            BeginDate = DateExtendUtil.getCurrentDate()
            EndDate = DateExtendUtil.getCurrentDate()
        }

        DateEnum.YESTERDAY.name -> {
            BeginDate = DateExtendUtil.getHistoryBeginDate(DateEnum.YESTERDAY)
            EndDate = BeginDate
        }

        DateEnum.WEEK.name -> {

            BeginDate = DateExtendUtil.formatDate(DateExtendUtil.getMondayOfWeek(Date()))
            EndDate = DateExtendUtil.formatDate(DateExtendUtil.getSundayOfWeek(Date()))
        }

        DateEnum.LAST_WEEK.name -> {

            BeginDate = DateExtendUtil.formatDate(
                DateExtendUtil.getMondayOfWeek(
                    DateExtendUtil.getHistoryDate(DateEnum.WEEK)
                )
            )
            EndDate = DateExtendUtil.formatDate(
                DateExtendUtil.getSundayOfWeek(
                    DateExtendUtil.getHistoryDate(DateEnum.WEEK)
                )
            )
        }

        DateEnum.MONTH.name -> {

            BeginDate = DateExtendUtil.formatDate(DateExtendUtil.getFirstDateOfMonth(Date()))
            EndDate = DateExtendUtil.formatDate(DateExtendUtil.getLastDateOfMonth(Date()))
        }

        DateEnum.LAST_MONTH.name -> {

            BeginDate = DateExtendUtil.formatDate(
                DateExtendUtil.getFirstDateOfMonth(
                    DateExtendUtil.getHistoryDate(DateEnum.MONTH)
                )
            )
            EndDate = DateExtendUtil.formatDate(
                DateExtendUtil.getFirstDateOfMonth(
                    DateExtendUtil.getHistoryDate(DateEnum.MONTH)
                )
            )
        }

        DateEnum.THISQUARTER.name -> {
            BeginDate = DateExtendUtil.formatDate(DateExtendUtil.getFirstDateOfSeason(Date()))
            EndDate = DateExtendUtil.formatDate(DateExtendUtil.getLastDateOfSeason(Date()))

        }

        DateEnum.LAST_QUARTER.name -> {

            BeginDate = DateExtendUtil.formatDate(
                DateExtendUtil.getFirstDateOfSeason(
                    DateExtendUtil.getHistoryDate(DateEnum.THREE_MONTHS)
                )
            )
            EndDate = DateExtendUtil.formatDate(
                DateExtendUtil.getLastDateOfSeason(
                    DateExtendUtil.getHistoryDate(DateEnum.THREE_MONTHS)
                )
            )

        }

        DateEnum.YEAR.name -> {

            BeginDate = "${DateExtendUtil.getYear(Date())}-01-01"
            EndDate = "${DateExtendUtil.getYear(Date())}-12-31"

        }

        getStringResource(R.string.无) -> {
            BeginDate = ""
            EndDate = ""

        }

        else -> {

        }
    }
    if (CustomeBillDateType != DateEnum.OTHER.name){
        callBack(BeginDate, EndDate)
    }
}

fun getDataName(CustomeBillDateType: String): String {
    return when (CustomeBillDateType) {
        //今天
        DateEnum.TODAY.name -> {
            getStringResource(R.string.今日)
        }
        //昨天
        DateEnum.YESTERDAY.name -> {
            getStringResource(R.string.昨日)
        }
        //本周
        DateEnum.WEEK.name -> {
            getStringResource(R.string.本周)
        }
        //上周
        DateEnum.LAST_WEEK.name -> {
            getStringResource(R.string.上周)
        }
        //本季度
        DateEnum.THISQUARTER.name -> {
            getStringResource(R.string.本季度)
        }
        //上季度
        DateEnum.LAST_QUARTER.name -> {
            getStringResource(R.string.上季度)
        }
        //本年
        DateEnum.YEAR.name -> {
            getStringResource(R.string.本年)
        }
        //上月
        DateEnum.LAST_MONTH.name -> {
            getStringResource(R.string.上月)
        }
        //本月
        else -> {
            getStringResource(R.string.本月)
        }
    }
}
