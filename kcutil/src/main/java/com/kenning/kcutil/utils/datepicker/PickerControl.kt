package com.kenning.kcutil.utils.datepicker

import android.view.View
import androidx.fragment.app.Fragment
import cn.carbswang.android.numberpickerview.library.NumberPickerView
import com.kenning.kcutil.R
import com.kenning.kcutil.utils.date.DateExtendUtil
import com.kenning.kcutil.utils.math.toInt_
import com.kenning.kcutil.widget.SwitchImageView


/**
 *Description :
 *author : KenningChen
 *Date : 2021/5/8 3:38 下午
 */
class PickerControl(var fragment: Fragment) {

    enum class ShowLocation {
        TOP, BOTTOM, CENTER
    }

    /**
     * 年份集合
     */
    val years = ArrayList<String>()

    /**
     * 月份集合
     */
    val months = ArrayList<String>()

    /**
     * 各个月份下的天数集合
     */
    val days = ArrayList<String>()

    /**
     * 开始日期的年份
     */
    var currentYear = ""

    /**
     * 开始日期的月份
     */
    var currentMonth = ""

    /**
     * 开始日期的日期(天)
     */
    var currentDay = ""

    /**
     * 结束日期的年份
     */
    var currentYear_end = ""

    /**
     * 结束日期的月份
     */
    var currentMonth_end = ""

    /**
     * 结束日期的日期(天)
     */
    var currentDay_end = ""

    var type = "DD"

    var iDatePickerBase = fragment as IDatePickerBase

    init {

        getDateString()

        var index = 0
        for (year in 1970 until 2101) {
            years.add("${year}年")
            if (year == currentYear.toInt_()) {
//                yearIndex = index//当前日期所在位置，控件显示用
            }
            index++
        }

        index = 0
        for (month in 1 until 13) {
            if (month > 9)
                months.add("${month}月")//如：10月
            else
                months.add("0${month}月")//如：01月
            if (month == currentMonth.toInt_()) {
//                monthIndex = index//当前日期所在位置，控件显示用
            }
            index++
        }
    }

    fun getDateString() {
        if (iDatePickerBase.startdate.isEmpty()) {
            iDatePickerBase.startdate = DateExtendUtil.getCurrentDate()
        }
        if (iDatePickerBase.enddate.isEmpty()) {
            iDatePickerBase.enddate = DateExtendUtil.getCurrentDate()
        }

        currentYear = iDatePickerBase.startdate.substring(0, 4)
        currentMonth = iDatePickerBase.startdate.substring(5, 7)
        currentDay = iDatePickerBase.startdate.substring(8, 10)

        if (iDatePickerBase.enddate.isNotEmpty()) {
            currentYear_end = iDatePickerBase.enddate.substring(0, 4)
            currentMonth_end = iDatePickerBase.enddate.substring(5, 7)
            currentDay_end = iDatePickerBase.enddate.substring(8, 10)
        }
    }

    /**
     * @return true 闰年
     *
     * false 非闰年
     * */
    private fun isLeayYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0
    }

    /**获取该月份下的天数*/
    fun getDaysArrayOnMonth(year: Int, month: Int) {
        days.clear()
        var index = 0
        var range: IntRange
        when (month) {
            1, 3, 5, 7, 8, 10, 12 -> range = 1 until 32
            2 -> range = if (isLeayYear(year)) 1 until 30 else 1 until 29
            else -> range = 1 until 31
        }

        for (day in range) {
            if (day > 9)
                days.add("${day}日")//如：10月
            else
                days.add("0${day}日")//如：01月
            index++
        }
    }

    fun getDaysArrayOnMonth_EveryTime(year: Int, month: Int): ArrayList<String> {
        val days = ArrayList<String>()
        var index = 0
        var range: IntRange
        when (month) {
            1, 3, 5, 7, 8, 10, 12 -> range = 1 until 32
            2 -> range = if (isLeayYear(year)) 1 until 30 else 1 until 29
            else -> range = 1 until 31
        }

        for (day in range) {
            if (day > 9)
                days.add("${day}日")//如：10月
            else
                days.add("0${day}日")//如：01月
            index++
        }
        return days
    }

    /**设置当前日期*/
    fun setCurrentDate(isRefrsh: Boolean = false) {

        with(iDatePickerBase.mView) {
            //多日期开始开始时间
            if (iDatePickerBase.isSingleDate) {
                getDaysArrayOnMonth(currentYear.toInt(), currentMonth.toInt())//设置 days 数组

                findViewById<NumberPickerView>(R.id.yearonly).displayedValues = years.toTypedArray()
                findViewById<NumberPickerView>(R.id.yearonly).maxValue = years.size - 1
                findViewById<NumberPickerView>(R.id.yearonly).minValue = 0
                findViewById<NumberPickerView>(R.id.yearonly).value =
                    years.indexOf("${currentYear}年")

                findViewById<NumberPickerView>(R.id.monthonly).displayedValues =
                    months.toTypedArray()
                findViewById<NumberPickerView>(R.id.monthonly).maxValue = months.size - 1
                findViewById<NumberPickerView>(R.id.monthonly).minValue = 0
                findViewById<NumberPickerView>(R.id.monthonly).value =
                    months.indexOf("${currentMonth}月")

                findViewById<NumberPickerView>(R.id.dayonly).displayedValues = days.toTypedArray()
                findViewById<NumberPickerView>(R.id.dayonly).maxValue = days.size - 1
                findViewById<NumberPickerView>(R.id.dayonly).minValue = 0
                findViewById<NumberPickerView>(R.id.dayonly).value = days.indexOf("${currentDay}日")
            } else {

                kotlin.run {
                    findViewById<NumberPickerView>(R.id.year).displayedValues =
                        years.toTypedArray()
                    findViewById<NumberPickerView>(R.id.year).maxValue = years.size - 1
                    findViewById<NumberPickerView>(R.id.year).minValue = 0
                    findViewById<NumberPickerView>(R.id.year).value =
                        years.indexOf("${currentYear}年")

                    findViewById<NumberPickerView>(R.id.month).displayedValues =
                        months.toTypedArray()
                    findViewById<NumberPickerView>(R.id.month).maxValue = months.size - 1
                    findViewById<NumberPickerView>(R.id.month).minValue = 0
                    findViewById<NumberPickerView>(R.id.month).value =
                        months.indexOf("${currentMonth}月")

                    if (!isRefrsh) {
                        getDaysArrayOnMonth(currentYear.toInt(), currentMonth.toInt())//设置 days 数组
                        findViewById<NumberPickerView>(R.id.day).displayedValues =
                            days.toTypedArray()
                        findViewById<NumberPickerView>(R.id.day).maxValue = days.size - 1
                        findViewById<NumberPickerView>(R.id.day).minValue = 0
                        findViewById<NumberPickerView>(R.id.day).value =
                            days.indexOf("${currentDay}日")
                    } else {
                        var olddaysize = days.size
                        getDaysArrayOnMonth(currentYear.toInt(), currentMonth.toInt())//设置 days 数组
                        var newdaysize = days.size
                        if (newdaysize > olddaysize) {
                            findViewById<NumberPickerView>(R.id.day).displayedValues =
                                days.toTypedArray()
                            findViewById<NumberPickerView>(R.id.day).maxValue = days.size - 1
                        } else {
                            findViewById<NumberPickerView>(R.id.day).maxValue = days.size - 1
                            findViewById<NumberPickerView>(R.id.day).displayedValues =
                                days.toTypedArray()
                        }
                    }
                    findViewById<NumberPickerView>(R.id.day).value =
                        days.indexOf("${currentDay}日")
                }


                //多日期结束时间
                kotlin.run {
                    findViewById<NumberPickerView>(R.id.year_end).displayedValues =
                        years.toTypedArray()
                    findViewById<NumberPickerView>(R.id.year_end).maxValue = years.size - 1
                    findViewById<NumberPickerView>(R.id.year_end).minValue = 0
                    findViewById<NumberPickerView>(R.id.year_end).value =
                        years.indexOf("${currentYear_end}年")

                    findViewById<NumberPickerView>(R.id.month_end).displayedValues =
                        months.toTypedArray()
                    findViewById<NumberPickerView>(R.id.month_end).maxValue = months.size - 1
                    findViewById<NumberPickerView>(R.id.month_end).minValue = 0
                    findViewById<NumberPickerView>(R.id.month_end).value =
                        months.indexOf("${currentMonth_end}月")

                    if (!isRefrsh) {
                        getDaysArrayOnMonth(
                            currentYear_end.toInt(),
                            currentMonth_end.toInt()
                        )//设置 days 数组
                        findViewById<NumberPickerView>(R.id.day_end).displayedValues =
                            days.toTypedArray()
                        findViewById<NumberPickerView>(R.id.day_end).maxValue = days.size - 1
                        findViewById<NumberPickerView>(R.id.day_end).minValue = 0
                        findViewById<NumberPickerView>(R.id.day_end).value =
                            days.indexOf("${currentDay_end}日")
                    } else {
                        var olddaysize = days.size
                        getDaysArrayOnMonth(
                            currentYear_end.toInt(),
                            currentMonth_end.toInt()
                        )//设置 days 数组
                        var newdaysize = days.size
                        if (newdaysize > olddaysize) {
                            findViewById<NumberPickerView>(R.id.day_end).displayedValues =
                                days.toTypedArray()
                            findViewById<NumberPickerView>(R.id.day_end).maxValue = days.size - 1
                        } else {
                            findViewById<NumberPickerView>(R.id.day_end).maxValue = days.size - 1
                            findViewById<NumberPickerView>(R.id.day_end).displayedValues =
                                days.toTypedArray()
                        }
                    }
                    findViewById<NumberPickerView>(R.id.day_end).value =
                        days.indexOf("${currentDay_end}日")
                }
            }
        }
    }

    fun bindClick() {

        with(iDatePickerBase.mView) {
            findViewById<SwitchImageView>(R.id.switchType)?.setOnSwitchListener {
                if (it) {
                    type = "DD"
                    findViewById<View>(R.id.dayonly).visibility = View.VISIBLE
                } else {
                    type = "MM"
                    findViewById<View>(R.id.dayonly).visibility = View.GONE
                }
            }

            if (type == "MM") {
                findViewById<SwitchImageView>(R.id.switchType)?.performClick()
            }

            //单日期
            kotlin.run {
                findViewById<NumberPickerView>(R.id.yearonly)?.setOnValueChangedListener { picker, oldVal, newVal ->
                    currentYear = years[newVal].replace("年", "")
                    if (/*isLeayYear(currentYear.toInt_()) && */currentMonth.toInt_() == 2) {
                        var oldsize = days.size
                        getDaysArrayOnMonth(currentYear.toInt_(), currentMonth.toInt_())
                        var newsize = days.size
                        if (newsize != oldsize) {
                            findViewById<NumberPickerView>(R.id.dayonly).apply {
                                if (displayedValues.size > days.toTypedArray().size){
                                    maxValue = days.size - 1
                                    displayedValues = days.toTypedArray()
                                }else{
                                    displayedValues = days.toTypedArray()
                                    maxValue = days.size - 1
                                }
                            }
                        }

                        if (days.indexOf("${currentDay}日") == -1) {
                            findViewById<NumberPickerView>(R.id.dayonly).value = days.size - 1
                            currentDay = days.last().replace("日", "")
                        } else {
                            findViewById<NumberPickerView>(R.id.dayonly).value =
                                days.indexOf("${currentDay}日")
                        }
                    }

                    //显示到日期控件上
                    iDatePickerBase.putDateToView(
                        currentYear,
                        currentMonth,
                        currentDay,
                        type = 1,
                        isSignal = true
                    )
                }

                findViewById<NumberPickerView>(R.id.monthonly)?.setOnValueChangedListener { picker, oldVal, newVal ->
                    currentMonth = months[newVal].replace("月", "")

                    var oldsize = days.size
                    getDaysArrayOnMonth(currentYear.toInt_(), currentMonth.toInt_())
                    var newsize = days.size
                    if (newsize != oldsize){
                        findViewById<NumberPickerView>(R.id.dayonly).apply {
                            if (displayedValues.size > days.toTypedArray().size){
                                maxValue = days.size - 1
                                displayedValues = days.toTypedArray()
                            }else{
                                displayedValues = days.toTypedArray()
                                maxValue = days.size - 1
                            }
                        }
                    }
                    if (days.indexOf("${currentDay}日") == -1) {
                        findViewById<NumberPickerView>(R.id.dayonly).value = days.size - 1
                        currentDay = days.last().replace("日", "")
                    } else {
                        findViewById<NumberPickerView>(R.id.dayonly).value =
                            days.indexOf("${currentDay}日")
                    }

                    //显示到日期控件上
                    iDatePickerBase.putDateToView(
                        currentYear,
                        currentMonth,
                        currentDay,
                        type = 1,
                        isSignal = true
                    )
                }

                findViewById<NumberPickerView>(R.id.dayonly)?.setOnValueChangedListener { picker,
                                                                                          oldVal, newVal ->
                    currentDay = days[newVal].replace("日", "")

                    //显示到日期控件上
                    iDatePickerBase.putDateToView(
                        currentYear,
                        currentMonth,
                        currentDay,
                        type = 1,
                        isSignal = true
                    )
                }
            }

            //多日期的开始
            kotlin.run {
                var days =
                    getDaysArrayOnMonth_EveryTime(currentYear.toInt_(), currentMonth.toInt_())
                findViewById<NumberPickerView>(R.id.year)?.setOnValueChangedListener { picker, oldVal, newVal ->
                    currentYear = years[newVal].replace("年", "")
                    if (/*isLeayYear(currentYear.toInt_()) && */currentMonth.toInt_() == 2) {
                        var oldsize = days.size
                        days = getDaysArrayOnMonth_EveryTime(
                            currentYear.toInt_(),
                            currentMonth.toInt_()
                        )
                        var newsize = days.size
                        if (newsize != oldsize){
                            findViewById<NumberPickerView>(R.id.day).apply {
                                if (displayedValues.size > days.toTypedArray().size){
                                    maxValue = days.size - 1
                                    displayedValues = days.toTypedArray()
                                }else{
                                    displayedValues = days.toTypedArray()
                                    maxValue = days.size - 1
                                }
                            }
                        }
                        if (days.indexOf("${currentDay}日") == -1) {
                            findViewById<NumberPickerView>(R.id.day).value = days.size - 1
                            currentDay = days.last().replace("日", "")
                        } else {
                            findViewById<NumberPickerView>(R.id.day).value =
                                days.indexOf("${currentDay}日")
                        }
                    }

                    //显示到日期控件上
                    iDatePickerBase.putDateToView(
                        currentYear,
                        currentMonth,
                        currentDay,
                        type = 0,
                        isSignal = false
                    )
                }

                findViewById<NumberPickerView>(R.id.month)?.setOnValueChangedListener { picker, oldVal, newVal ->
                    currentMonth = months[newVal].replace("月", "")
                    var oldsize = days.size
                    days =
                        getDaysArrayOnMonth_EveryTime(currentYear.toInt_(), currentMonth.toInt_())
                    var newsize = days.size
                    if (newsize != oldsize){
                        findViewById<NumberPickerView>(R.id.day).apply {
                            if (displayedValues.size > days.toTypedArray().size){
                                maxValue = days.size - 1
                                displayedValues = days.toTypedArray()
                            }else{
                                displayedValues = days.toTypedArray()
                                maxValue = days.size - 1
                            }
                        }
                    }

                    if (days.indexOf("${currentDay}日") == -1) {
                        findViewById<NumberPickerView>(R.id.day).value = days.size - 1
                        currentDay = days.last().replace("日", "")
                    } else {
                        findViewById<NumberPickerView>(R.id.day).value =
                            days.indexOf("${currentDay}日")
                    }

                    //显示到日期控件上
                    iDatePickerBase.putDateToView(
                        currentYear,
                        currentMonth,
                        currentDay,
                        type = 0,
                        isSignal = false
                    )
                }

                findViewById<NumberPickerView>(R.id.day)?.setOnValueChangedListener { picker,
                                                                                      oldVal, newVal ->
                    currentDay = days[newVal].replace("日", "")

                    //显示到日期控件上
                    iDatePickerBase.putDateToView(
                        currentYear,
                        currentMonth,
                        currentDay,
                        type = 0,
                        isSignal = false
                    )
                }
            }

            //多日期的结束
            kotlin.run {
                var days = getDaysArrayOnMonth_EveryTime(
                    currentYear_end.toInt_(),
                    currentMonth_end.toInt_()
                )
                findViewById<NumberPickerView>(R.id.year_end)?.setOnValueChangedListener { picker, oldVal, newVal ->
                    currentYear_end = years[newVal].replace("年", "")
                    if (/*isLeayYear(currentYear_end.toInt_()) && */currentMonth_end.toInt_() == 2) {
                        var oldsize = days.size
                        days = getDaysArrayOnMonth_EveryTime(
                            currentYear_end.toInt_(),
                            currentMonth_end.toInt_()
                        )
                        var newsize = days.size
                        if (newsize != oldsize){
                            findViewById<NumberPickerView>(R.id.day_end).apply {
                                if (displayedValues.size > days.toTypedArray().size){
                                    maxValue = days.size - 1
                                    displayedValues = days.toTypedArray()
                                }else{
                                    displayedValues = days.toTypedArray()
                                    maxValue = days.size - 1
                                }
                            }
                        }
                        if (days.indexOf("${currentDay_end}日") == -1) {
                            findViewById<NumberPickerView>(R.id.day_end).value = days.size - 1
                            currentDay_end = days.last().replace("日", "")
                        } else {
                            findViewById<NumberPickerView>(R.id.day_end).value =
                                days.indexOf("${currentDay_end}日")
                        }
                    }

                    //显示到日期控件上
                    iDatePickerBase.putDateToView(
                        currentYear_end,
                        currentMonth_end,
                        currentDay_end,
                        type = 1,
                        isSignal = false
                    )
                }

                findViewById<NumberPickerView>(R.id.month_end)?.setOnValueChangedListener { picker, oldVal, newVal ->
                    currentMonth_end = months[newVal].replace("月", "")
                    var oldsize = days.size
                    days = getDaysArrayOnMonth_EveryTime(
                        currentYear_end.toInt_(),
                        currentMonth_end.toInt_()
                    )
                    var newsize = days.size
                    if (newsize != oldsize){
                        findViewById<NumberPickerView>(R.id.day_end).apply {
                            if (displayedValues.size > days.toTypedArray().size){
                                maxValue = days.size - 1
                                displayedValues = days.toTypedArray()
                            }else{
                                displayedValues = days.toTypedArray()
                                maxValue = days.size - 1
                            }
                        }
                    }
                    if (days.indexOf("${currentDay_end}日") == -1) {
                        findViewById<NumberPickerView>(R.id.day_end).value = days.size - 1
                        currentDay_end = days.last().replace("日", "")
                    } else {
                        findViewById<NumberPickerView>(R.id.day_end).value =
                            days.indexOf("${currentDay_end}日")
                    }

                    //显示到日期控件上
                    iDatePickerBase.putDateToView(
                        currentYear_end,
                        currentMonth_end,
                        currentDay_end,
                        type = 1,
                        isSignal = false
                    )
                }

                findViewById<NumberPickerView>(R.id.day_end)?.setOnValueChangedListener { picker,
                                                                                          oldVal, newVal ->
                    currentDay_end = days[newVal].replace("日", "")

                    //显示到日期控件上
                    iDatePickerBase.putDateToView(
                        currentYear_end,
                        currentMonth_end,
                        currentDay_end,
                        type = 1,
                        isSignal = false
                    )
                }
            }
        }
    }

}