/**
 * Copyright (C), 2016-2020, 杭州致梦科技有限公司
 */
package com.kenning.kcutil.utils.datepicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.DialogInterface
import android.widget.EditText
import android.widget.TextView
import com.kenning.kcutil.R
import com.kenning.kcutil.utils.date.DateExtendUtil
import com.kenning.kcutil.utils.date.Date_Format
import com.kenning.kcutil.utils.math.toInt_
import java.util.*

/**
 * @ClassName: datepickerutil
 * Description:
 * @Author: chenshaohua
 * @Date: 2020/5/21 11:05 AM
 */
object DatePickerUtil {
    /**
     * 弹出控件，并把设置的日期放入到EditText中
     *
     * @param con 在该页面弹出控件
     * @param et  把值显示到该EditText中
     */
    fun <T> artDateDialog(
        con: Context?,
        et: T,
        callBack: OnDateSetListener?
    ) {
        var t = ""
        if (et is TextView) {
            t = et.text.toString()
        } else if (et is EditText) {
            t = et.text.toString()
        } else {
            return
        }

        if (t.length != 10) {
            t = Date_Format.YMD.format(Date())
        }
        val y = t.substring(0, 4).toInt_()
        val m = t.substring(5, 7).toInt_() - 1
        val d = t.substring(8, 10).toInt_()
        // 日历控件
        val dg = DatePickerDialog(con!!, callBack, y, m, d)
        // 手动设置按钮
        dg.setButton(
            DialogInterface.BUTTON_POSITIVE, con!!.getString(R.string.完成)
        ) { dialog, which ->
            // 通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
            val datePicker = dg.datePicker
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth
            val ymd = intArrayOf(year, month, day)
            if (et is TextView) {
                et.text = DateExtendUtil.SETDATE(ymd)
            } else if (et is EditText) {
                et.setText(DateExtendUtil.SETDATE(ymd))
            }
            callBack?.onDateSet(datePicker, year, month, day)
        }
        // 取消按钮，如果不需要直接不设置即可
        dg.setButton(
            DialogInterface.BUTTON_NEGATIVE, con!!.getString(R.string.取消)
        ) { dialog, which -> }
        dg.setCanceledOnTouchOutside(false)
        dg.show()
    }
}