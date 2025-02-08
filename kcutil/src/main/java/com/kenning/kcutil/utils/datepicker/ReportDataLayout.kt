package com.kenning.kcutil.utils.datepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.kenning.kcutil.R
import com.kenning.kcutil.databinding.PopBillListTimeBinding
import com.kenning.kcutil.databinding.ReportdatelayoutBinding
import com.kenning.kcutil.utils.date.DateEnum
import com.kenning.kcutil.utils.date.DateExtendUtil
import com.kenning.kcutil.utils.date.Date_Format
import com.kenning.kcutil.utils.other.ToastUtil
import com.kenning.kcutil.utils.other.getColorResource
import com.kenning.kcutil.utils.other.getStringResource
import com.kenning.kcutil.utils.other.outOf
import com.zyyoona7.popup.EasyPopup
import java.util.Date
import kotlin.math.abs

/**
 * Created by ZHT
 * Date 2024-08-23 15:13
 * FileName ReportDataLayout.kt
 * 描述：报表时间选择器
 */
class ReportDataLayout : LinearLayout, IPickerListener, TwoDatePickInterface {

    private lateinit var viewBinding: ReportdatelayoutBinding

    var showDateView = MutableLiveData(false)

    var BeginDate = ""

    var EndDate = ""

    var CustomeBillDateType = ""

    var CustomeBillDateFormat = ""

    /**时间窗口*/
    lateinit var mTimeEasyPop: EasyPopup

    lateinit var mTimeBinding: PopBillListTimeBinding

    constructor(context: Context?) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    fun setFrameLayout(mFrameLayout: ViewGroup): ReportDataLayout {
        mTimeEasyPop.setDimView(mFrameLayout)
        return this
    }

    @SuppressLint("ResourceType")
    fun initView(context: Context?) {
        viewBinding = ReportdatelayoutBinding.inflate(LayoutInflater.from(context), this, true)
        viewBinding.tvOther.setTextColor(Color.parseColor("#7C7F8E"))
        val colorStateList = ContextCompat.getColorStateList(context!!, R.color.color_7C7F8E)
        ImageViewCompat.setImageTintList(viewBinding.ivOther, colorStateList)
        viewBinding.rbOther.setOnCheckedChangeListener(null)
        if (context != null) {
            initDateView(context)
        }
    }


    var lastTimeType: LinearLayout? = null

    @SuppressLint("SetTextI18n")
    fun initDateView(context: Context) {

        mTimeBinding = PopBillListTimeBinding.inflate(LayoutInflater.from(context), this, false)

        mTimeBinding.llToday.visibility = View.GONE
        mTimeBinding.llYesterday.visibility = View.GONE
        mTimeBinding.llWeek.visibility = View.GONE
        mTimeBinding.llMonth.visibility = View.GONE
        mTimeBinding.viewToday.visibility = View.GONE
        mTimeBinding.viewYesterday.visibility = View.GONE
        mTimeBinding.viewWeek.visibility = View.GONE
        mTimeBinding.viewMonth.visibility = View.GONE

        mTimeBinding.tvToday.text = context!!.getString(R.string.今日) + "(${(DateExtendUtil.getCurrentDate().substring(5)).replace("-", "/")})"
        mTimeBinding.tvYesterday.text =context!!.getString(R.string.昨日)  + "(${
            (DateExtendUtil.getHistoryBeginDate(DateEnum.YESTERDAY).substring(5)).replace(
                "-",
                "/"
            )
        })"

        mTimeBinding.tvWeek.text = context!!.getString(R.string.本周) + "(${
            (DateExtendUtil.formatDate(DateExtendUtil.getMondayOfWeek(Date()))
                .substring(5) + context!!.getString(R.string.至) + DateExtendUtil.formatDate(DateExtendUtil.getSundayOfWeek(Date()))
                .substring(5)).replace("-", "/")
        })"
        mTimeBinding.tvMonth.text = context!!.getString(R.string.本月) + "(${
            (
                    DateExtendUtil.formatDate(DateExtendUtil.getFirstDateOfMonth(Date()))
                        .substring(0, 7)).replace("-", "/")
        })"
        mTimeBinding.tvLastWeek.text = context!!.getString(R.string.上周)  + "(${
            (
                    DateExtendUtil.formatDate(
                        DateExtendUtil.getMondayOfWeek(DateExtendUtil.getHistoryDate(DateEnum.WEEK))
                    ).substring(5) +
                            context!!.getString(R.string.至) + DateExtendUtil.formatDate(
                        DateExtendUtil.getSundayOfWeek(
                            DateExtendUtil.getHistoryDate(
                                DateEnum.WEEK
                            )
                        )
                    ).substring(5)
                    ).replace("-", "/")
        })"


        mTimeBinding.tvLastMonth.text = context!!.getString(R.string.上月) + "(${
            (
                    DateExtendUtil.formatDate(
                        DateExtendUtil.getFirstDateOfMonth(
                            DateExtendUtil.getHistoryDate(DateEnum.MONTH)
                        )
                    ).substring(0, 7)).replace("-", "/")
        })"

        mTimeBinding.tvSeason.text = context!!.getString(R.string.本季度) + "(${
            (
                    DateExtendUtil.formatDate(DateExtendUtil.getFirstDateOfSeason(Date()))
                        .substring(0, 7) + context!!.getString(R.string.至) + DateExtendUtil.formatDate(DateExtendUtil.getLastDateOfSeason(Date()))
                        .substring(0, 7)
                    ).replace("-", "/")
        })"

        mTimeBinding.tvLastSeason.text = context!!.getString(R.string.上季度) + "(${
            (
                    DateExtendUtil.formatDate(
                        DateExtendUtil.getFirstDateOfSeason(
                            DateExtendUtil.getHistoryDate(DateEnum.THREE_MONTHS)
                        )
                    ).substring(0, 7) + context!!.getString(R.string.至) + DateExtendUtil.formatDate(
                        DateExtendUtil.getLastDateOfSeason(
                            DateExtendUtil.getHistoryDate(DateEnum.THREE_MONTHS)
                        )
                    ).substring(0, 7)
                    ).replace("-", "/")
        })"

        mTimeBinding.tvYear.text =
            context!!.getString(R.string.本年) + "(${DateExtendUtil.getYear(Date()).toString() + "/01" + context!!.getString(R.string.至) + DateExtendUtil.getYear(Date()).toString() + "/12"})"

        mTimeEasyPop = EasyPopup(context)
            .setContentView(mTimeBinding.root)
            //是否允许点击PopupWindow之外的地方消失
            .setFocusAndOutsideEnable(true)
            //允许背景变暗
            .setBackgroundDimEnable(true)
            .setAnchorView(viewBinding.rgDatePicker)
            //变暗的透明度(0-1)，0为完全透明
            .setWidth((context as Activity).windowManager.defaultDisplay.width)
            .setDimValue(0.5f)
            .setOnDismissListener {
                showDateView.value = false
                viewBinding.tvOther.setTextColor(Color.parseColor("#7C7F8E"))
                val colorStateList = ContextCompat.getColorStateList(context, R.color.color_7C7F8E)
                ImageViewCompat.setImageTintList(viewBinding.ivOther, colorStateList)
            }.apply()


    }

    /**
     * @Description:自定义字段 （默认是隐藏今日、昨天、本周、本月）
     * 当使用本方法时，会按传的枚举进行隐藏和显示，并隐藏右边的其他按钮
     * @author: create by zyl on 2024/11/19
     */
    fun setDisplayColumn(showLists: ArrayList<DateEnum>): ReportDataLayout {
//        //自定义的都默认本月了。如后期要改，要再建个方法来设置默认值
//        viewBinding.tvOther.text = context!!.getString(R.string.本月)
//        changeTimeState(mTimeBinding.llMonth, lastTimeType)

        viewBinding.tvOther.visibility = View.VISIBLE
        viewBinding.ivOther.visibility = View.VISIBLE
        viewBinding.rbOther.visibility = View.GONE
        viewBinding.tvOther.setOnClickListener {
            viewBinding.rbOther.performClick()
        }
        viewBinding.ivOther.setOnClickListener {
            viewBinding.rbOther.performClick()
        }
        viewBinding.rgDatePicker.visibility = View.GONE
        if (DateEnum.TODAY outOf showLists) {
            mTimeBinding.llToday.visibility = View.GONE
            mTimeBinding.viewToday.visibility = View.GONE
        } else {
            mTimeBinding.llToday.visibility = View.VISIBLE
            mTimeBinding.viewToday.visibility = View.VISIBLE
        }
        if (DateEnum.YESTERDAY outOf showLists) {
            mTimeBinding.llYesterday.visibility = View.GONE
            mTimeBinding.viewYesterday.visibility = View.GONE
        } else {
            mTimeBinding.llYesterday.visibility = View.VISIBLE
            mTimeBinding.viewYesterday.visibility = View.VISIBLE
        }
        if (DateEnum.WEEK outOf showLists) {
            mTimeBinding.llWeek.visibility = View.GONE
            mTimeBinding.viewWeek.visibility = View.GONE
        } else {
            mTimeBinding.llWeek.visibility = View.VISIBLE
            mTimeBinding.viewWeek.visibility = View.VISIBLE
        }
        if (DateEnum.LAST_WEEK outOf showLists) {
            mTimeBinding.llLastWeek.visibility = View.GONE
            mTimeBinding.viewLastWeek.visibility = View.GONE
        } else {
            mTimeBinding.llLastWeek.visibility = View.VISIBLE
            mTimeBinding.viewLastWeek.visibility = View.VISIBLE
        }
        if (DateEnum.MONTH outOf showLists) {
            mTimeBinding.llMonth.visibility = View.GONE
            mTimeBinding.viewMonth.visibility = View.GONE
        } else {
            mTimeBinding.llMonth.visibility = View.VISIBLE
            mTimeBinding.viewMonth.visibility = View.VISIBLE
        }
        if (DateEnum.LAST_MONTH outOf showLists) {
            mTimeBinding.llLastMonth.visibility = View.GONE
            mTimeBinding.viewLastMonth.visibility = View.GONE
        } else {
            mTimeBinding.llLastMonth.visibility = View.VISIBLE
            mTimeBinding.viewLastMonth.visibility = View.VISIBLE
        }
        if (DateEnum.THISQUARTER outOf showLists) {
            mTimeBinding.llSeason.visibility = View.GONE
            mTimeBinding.viewSeason.visibility = View.GONE
        } else {
            mTimeBinding.llSeason.visibility = View.VISIBLE
            mTimeBinding.viewSeason.visibility = View.VISIBLE
        }
        if (DateEnum.LAST_QUARTER outOf showLists) {
            mTimeBinding.llLastSeason.visibility = View.GONE
            mTimeBinding.viewLastSeason.visibility = View.GONE
        } else {
            mTimeBinding.llLastSeason.visibility = View.VISIBLE
            mTimeBinding.viewLastSeason.visibility = View.VISIBLE
        }
        if (DateEnum.YEAR outOf showLists) {
            mTimeBinding.llYear.visibility = View.GONE
            mTimeBinding.viewYear.visibility = View.GONE
        } else {
            mTimeBinding.llYear.visibility = View.VISIBLE
            mTimeBinding.viewYear.visibility = View.VISIBLE
        }
        if (DateEnum.OTHER outOf showLists) {
            mTimeBinding.llCustom.visibility = View.GONE
        } else {
            mTimeBinding.llCustom.visibility = View.VISIBLE
        }
        return this
    }

    var isDiy = false

    @SuppressLint("SetTextI18n")
    fun setDefaultShow(BeginDate: String, EndDate: String, CustomeBillDateType: String, CustomeBillDateFormat: String = "DD", isD9Diy: Boolean = false): ReportDataLayout {
        this.BeginDate = BeginDate
        this.EndDate = EndDate
        this.CustomeBillDateType = CustomeBillDateType
        this.CustomeBillDateFormat = CustomeBillDateFormat

        when (CustomeBillDateType) {

            //今天
            DateEnum.TODAY.name -> {
                viewBinding.rbToDay.isChecked = true
                viewBinding.tvOther.text = context!!.getString(R.string.今日)
                viewBinding.rbOther.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(context, R.drawable.ic_new_unfold_gray),
                    null
                )
                viewBinding.rbOther.text = context!!.getString(R.string.时间筛选)
                mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                if (isD9Diy){
                    changeTimeState(mTimeBinding.llToday, lastTimeType)
                }else{
                    changeTimeState(mTimeBinding.llCustom1, lastTimeType)
                }

            }
            //昨天
            DateEnum.YESTERDAY.name -> {
                viewBinding.rbYesterDay.isChecked = true
                viewBinding.tvOther.text = context!!.getString(R.string.昨日)
                viewBinding.rbOther.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(context, R.drawable.ic_new_unfold_gray),
                    null
                )
                viewBinding.rbOther.text = context!!.getString(R.string.时间筛选)
                mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                if (isD9Diy){
                    changeTimeState(mTimeBinding.llYesterday, lastTimeType)
                }else{
                    changeTimeState(mTimeBinding.llCustom1, lastTimeType)
                }            }
            //本周
            DateEnum.WEEK.name -> {
                viewBinding.rbWeek.isChecked = true
                viewBinding.tvOther.text = context!!.getString(R.string.本周)
                viewBinding.rbOther.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(context, R.drawable.ic_new_unfold_gray),
                    null
                )
                viewBinding.rbOther.text = context!!.getString(R.string.时间筛选)
                mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                if (isD9Diy){
                    changeTimeState(mTimeBinding.llWeek, lastTimeType)
                }else{
                    changeTimeState(mTimeBinding.llCustom1, lastTimeType)
                }            }

            //本月
            DateEnum.MONTH.name -> {
                viewBinding.rbMonth.isChecked = true
                viewBinding.tvOther.text = context!!.getString(R.string.本月)
                viewBinding.rbOther.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(context, R.drawable.ic_new_unfold_gray),
                    null
                )
                viewBinding.rbOther.text = context!!.getString(R.string.时间筛选)
                mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                if (isD9Diy){
                    changeTimeState(mTimeBinding.llMonth, lastTimeType)
                }else{
                    changeTimeState(mTimeBinding.llCustom1, lastTimeType)
                }            }

            getStringResource(R.string.无) -> {
                viewBinding.rbDiy.isChecked = true
                viewBinding.rbOther.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(context, R.drawable.ic_new_unfold_gray),
                    null
                )
                viewBinding.rbOther.text = context!!.getString(R.string.时间筛选)
                mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                if (isD9Diy){
                    changeTimeState(mTimeBinding.llCustom, lastTimeType)
                }else{
                    changeTimeState(mTimeBinding.llCustom1, lastTimeType)
                }            }

            else -> {

                viewBinding.rbDiy.isChecked = true
                viewBinding.rbOther.isChecked = true
                isDiy = true
                viewBinding.rbOther.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(context, R.drawable.ic_new_unfold),
                    null
                )
                this.callback?.invoke(this.BeginDate, this.EndDate, this.CustomeBillDateType)
                changeTimeState(
                    when (CustomeBillDateType) {
                        //其他

                        DateEnum.OTHER.name -> {

                            if (CustomeBillDateFormat == "MM") {
                                viewBinding.rbOther.text = "${BeginDate.substring(0, 7)}\n${EndDate.substring(0, 7)}"
                                viewBinding.tvOther.text = "${BeginDate.substring(0, 7)}\n${EndDate.substring(0, 7)}"

                                mTimeBinding.tvCustom.text =
                                    context!!.getString(R.string.自定义) + "(${BeginDate.substring(0, 7)}~${EndDate.substring(0, 7)})"
                            } else {
                                viewBinding.rbOther.text = "${BeginDate.replace("-", "/")}\n${
                                    EndDate.replace(
                                        "-",
                                        "/"
                                    )
                                }"
                                viewBinding.tvOther.text = "${BeginDate.replace("-", "/")}\n${
                                    EndDate.replace(
                                        "-",
                                        "/"
                                    )
                                }"
                                mTimeBinding.tvCustom.text =
                                    context!!.getString(R.string.自定义) + "(${BeginDate.replace("-", "/")}${context!!.getString(R.string.至)}${
                                        EndDate.replace(
                                            "-",
                                            "/"
                                        )
                                    })"
                            }


                            mTimeBinding.llCustom
                        }

                        //上周
                        DateEnum.LAST_WEEK.name -> {
                            viewBinding.rbOther.text = context!!.getString(R.string.上周)
                            viewBinding.tvOther.text = context!!.getString(R.string.上周)
                            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                            mTimeBinding.llLastWeek
                        }
                        //本季度
                        DateEnum.THISQUARTER.name -> {
                            viewBinding.rbOther.text = context!!.getString(R.string.本季度)
                            viewBinding.tvOther.text = context!!.getString(R.string.本季度)
                            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                            mTimeBinding.llSeason
                        }
                        //上季度
                        DateEnum.LAST_QUARTER.name -> {
                            viewBinding.rbOther.text = context!!.getString(R.string.上季度)
                            viewBinding.tvOther.text = context!!.getString(R.string.上季度)
                            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                            mTimeBinding.llLastSeason
                        }
                        //本年
                        DateEnum.YEAR.name -> {
                            viewBinding.rbOther.text = context!!.getString(R.string.本年)
                            viewBinding.tvOther.text = context!!.getString(R.string.本年)
                            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                            mTimeBinding.llYear
                        }
                        //上月
                        else -> {
                            viewBinding.rbOther.text = context!!.getString(R.string.上月)
                            viewBinding.tvOther.text = context!!.getString(R.string.上月)
                            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                            mTimeBinding.llLastMonth
                        }
                    }, lastTimeType
                )
            }
        }
        return this
    }

    var callback: ((String, String, String) -> Unit?)? = null

    /**RadioGroup监听事件 */
    fun setRadioChangListenner(block: (startdate: String, enddate: String, dateEnumName: String) -> Unit): ReportDataLayout {

        viewBinding.rgDatePicker.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                viewBinding.rbToDay.id -> {
                    //今天
                    CustomeBillDateType = DateEnum.TODAY.name
                    BeginDate = DateExtendUtil.getCurrentDate()
                    EndDate = DateExtendUtil.getCurrentDate()
                    changeTimeState(mTimeBinding.llCustom1, lastTimeType)

                    block(BeginDate, EndDate, CustomeBillDateType)

                }

                viewBinding.rbYesterDay.id -> {
                    //昨天
                    CustomeBillDateType = DateEnum.YESTERDAY.name
                    BeginDate = DateExtendUtil.getHistoryBeginDate(DateEnum.YESTERDAY)
                    EndDate = BeginDate
                    changeTimeState(mTimeBinding.llCustom1, lastTimeType)
                    block(BeginDate, EndDate, CustomeBillDateType)

                }

                viewBinding.rbWeek.id -> {
                    //本周
                    CustomeBillDateType = DateEnum.WEEK.name
                    BeginDate = DateExtendUtil.formatDate(DateExtendUtil.getMondayOfWeek(Date()))
                    EndDate = DateExtendUtil.formatDate(DateExtendUtil.getSundayOfWeek(Date()))
                    changeTimeState(mTimeBinding.llCustom1, lastTimeType)
                    block(BeginDate, EndDate, CustomeBillDateType)

                }

                viewBinding.rbMonth.id -> {
                    //本月
                    CustomeBillDateType = DateEnum.MONTH.name
                    BeginDate = DateExtendUtil.formatDate(DateExtendUtil.getFirstDateOfMonth(Date()))
                    EndDate = DateExtendUtil.formatDate(DateExtendUtil.getLastDateOfMonth(Date()))
                    changeTimeState(mTimeBinding.llCustom1, lastTimeType)
                    block(BeginDate, EndDate, CustomeBillDateType)
                }

                else -> {

                }
            }
            if (viewBinding.rbToDay.isChecked ||
                viewBinding.rbYesterDay.isChecked ||
                viewBinding.rbWeek.isChecked ||
                viewBinding.rbMonth.isChecked
            ) {
                viewBinding.rbOther.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(context, R.drawable.ic_new_unfold_gray),
                    null
                )
                viewBinding.rbOther.text = context!!.getString(R.string.时间筛选)
                mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                viewBinding.rbOther.isChecked = false
                isDiy = false
            }
        }

        viewBinding.rbOther.setOnClickListener {

            if (isDiy) {
                viewBinding.rbOther.isChecked = isDiy
            } else {
                viewBinding.rbOther.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(context, R.drawable.ic_new_unfold_gray),
                    null
                )
                viewBinding.rbOther.text = context!!.getString(R.string.时间筛选)
                mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
                viewBinding.rbOther.isChecked = isDiy
            }
            viewBinding.tvOther.setTextColor(Color.parseColor("#2F71FD"))
            val colorStateList = ContextCompat.getColorStateList(context!!, R.color.color_2F71FD)
            ImageViewCompat.setImageTintList(viewBinding.ivOther, colorStateList)
            mTimeEasyPop.showAsDropDown(viewBinding.llTimeFiltrate)
        }

        return this
    }

    fun setDateEvent(
        activity: FragmentActivity,
        block: (startdate: String, enddate: String, dateEnumName: String) -> Unit,
    ): ReportDataLayout {
        this.callback = block
        //今天
        mTimeBinding.llToday.setOnClickListener {
            CustomeBillDateType = DateEnum.TODAY.name
            BeginDate = DateExtendUtil.getCurrentDate()
            EndDate = DateExtendUtil.getCurrentDate()
            viewBinding.rbOther.text = context!!.getString(R.string.今日)
            viewBinding.tvOther.text = context!!.getString(R.string.今日)
            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
            switchDateType(activity, (it as LinearLayout), lastTimeType)
            block.invoke(BeginDate, EndDate, CustomeBillDateType)
        }
        //昨天
        mTimeBinding.llYesterday.setOnClickListener {
            CustomeBillDateType = DateEnum.YESTERDAY.name
            BeginDate = DateExtendUtil.getHistoryBeginDate(DateEnum.YESTERDAY)
            EndDate = BeginDate
            viewBinding.rbOther.text = context!!.getString(R.string.昨日)
            viewBinding.tvOther.text = context!!.getString(R.string.昨日)
            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
            switchDateType(activity, (it as LinearLayout), lastTimeType)
            block.invoke(BeginDate, EndDate, CustomeBillDateType)
        }
        //本周
        mTimeBinding.llWeek.setOnClickListener {
            CustomeBillDateType = DateEnum.WEEK.name
            BeginDate = DateExtendUtil.formatDate(DateExtendUtil.getMondayOfWeek(Date()))
            EndDate = DateExtendUtil.formatDate(DateExtendUtil.getSundayOfWeek(Date()))
            viewBinding.rbOther.text = context!!.getString(R.string.本周)
            viewBinding.tvOther.text = context!!.getString(R.string.本周)
            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
            switchDateType(activity, (it as LinearLayout), lastTimeType)
            block.invoke(BeginDate, EndDate, CustomeBillDateType)
        }
        //上周
        mTimeBinding.llLastWeek.setOnClickListener {
            CustomeBillDateType = DateEnum.LAST_WEEK.name
            BeginDate =
                DateExtendUtil.formatDate(DateExtendUtil.getMondayOfWeek(DateExtendUtil.getHistoryDate(DateEnum.WEEK)))
            EndDate =
                DateExtendUtil.formatDate(DateExtendUtil.getSundayOfWeek(DateExtendUtil.getHistoryDate(DateEnum.WEEK)))
            viewBinding.rbOther.text = context!!.getString(R.string.上周)
            viewBinding.tvOther.text = context!!.getString(R.string.上周)
            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
            switchDateType(activity, (it as LinearLayout), lastTimeType)
            block.invoke(BeginDate, EndDate, CustomeBillDateType)
        }
        //上月
        mTimeBinding.llLastMonth.setOnClickListener {
            CustomeBillDateType = DateEnum.LAST_MONTH.name
            BeginDate = DateExtendUtil.formatDate(
                DateExtendUtil.getFirstDateOfMonth(
                    DateExtendUtil.getHistoryDate(DateEnum.MONTH)
                )
            )
            EndDate = DateExtendUtil.formatDate(
                DateExtendUtil.getLastDateOfMonth(
                    DateExtendUtil.getHistoryDate(DateEnum.MONTH)
                )
            )
            viewBinding.rbOther.text = context!!.getString(R.string.上月)
            viewBinding.tvOther.text = context!!.getString(R.string.上月)
            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
            switchDateType(activity, (it as LinearLayout), lastTimeType)
            block.invoke(BeginDate, EndDate, CustomeBillDateType)
        }
        //本月
        mTimeBinding.llMonth.setOnClickListener {
            CustomeBillDateType = DateEnum.MONTH.name
            BeginDate = DateExtendUtil.formatDate(DateExtendUtil.getFirstDateOfMonth(Date()))
            EndDate = DateExtendUtil.formatDate(DateExtendUtil.getLastDateOfMonth(Date()))
            viewBinding.rbOther.text = context!!.getString(R.string.本月)
            viewBinding.tvOther.text = context!!.getString(R.string.本月)
            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
            switchDateType(activity, (it as LinearLayout), lastTimeType)
            block.invoke(BeginDate, EndDate, CustomeBillDateType)
        }
        //本季度
        mTimeBinding.llSeason.setOnClickListener {
            CustomeBillDateType = DateEnum.THISQUARTER.name
            BeginDate = DateExtendUtil.formatDate(DateExtendUtil.getFirstDateOfSeason(Date()))
            EndDate = DateExtendUtil.formatDate(DateExtendUtil.getLastDateOfSeason(Date()))
            viewBinding.rbOther.text = context!!.getString(R.string.本季度)
            viewBinding.tvOther.text = context!!.getString(R.string.本季度)
            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
            switchDateType(activity, (it as LinearLayout), lastTimeType)
            block.invoke(BeginDate, EndDate, CustomeBillDateType)
        }
        //上季度
        mTimeBinding.llLastSeason.setOnClickListener {
            CustomeBillDateType = DateEnum.THISQUARTER.name
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
            viewBinding.rbOther.text = context!!.getString(R.string.上季度)
            viewBinding.tvOther.text = context!!.getString(R.string.上季度)
            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
            switchDateType(activity, (it as LinearLayout), lastTimeType)
            block.invoke(BeginDate, EndDate, CustomeBillDateType)
        }
        //本年
        mTimeBinding.llYear.setOnClickListener {
            CustomeBillDateType = DateEnum.YEAR.name
            BeginDate = "${DateExtendUtil.getYear(Date())}-01-01"
            EndDate = "${DateExtendUtil.getYear(Date())}-12-31"
            viewBinding.rbOther.text = context!!.getString(R.string.本年)
            viewBinding.tvOther.text = context!!.getString(R.string.本年)
            mTimeBinding.tvCustom.text = context!!.getString(R.string.自定义)
            switchDateType(activity, (it as LinearLayout), lastTimeType)
            block.invoke(BeginDate, EndDate, CustomeBillDateType)
        }
        //自定义
        mTimeBinding.llCustom.setOnClickListener {
            switchDateType(activity, (it as LinearLayout), lastTimeType)
        }

        return this

    }

    fun setCustomeBillDateFormat(block: (CustomeBillDateFormat: String) -> Unit): ReportDataLayout {
        this.DateFormatcallback = block
        return this
    }


    @SuppressLint("SetTextI18n")
    private fun switchDateType(
        activity: FragmentActivity,
        current: LinearLayout,
        last: LinearLayout?,
    ) {
        if (current == mTimeBinding.llCustom) {
            mTimeEasyPop.dismiss()
            TwoDatePickerBuilder(activity, this)
                .setBeginDate(BeginDate)
                .setEndDate(EndDate)
                .setSingle(false)
                .setCanceledOnTouchOutside(false)
                .setDateFormat(CustomeBillDateFormat)
                .setRequestCode(abs(mTimeBinding.llCustom.hashCode()))
                .setLoaction(PickerControl.ShowLocation.BOTTOM)
                .start()
            return
        } else {

            viewBinding.rbDiy.isChecked = true

            viewBinding.rbOther.isChecked = true
            isDiy = true
            viewBinding.rbOther.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                ContextCompat.getDrawable(context, R.drawable.ic_new_unfold),
                null
            )
        }
        changeTimeState(current, last)
        mTimeEasyPop.dismiss()
    }

    //
    fun changeTimeState(current: LinearLayout, last: LinearLayout?) {
        if (current != last) {
            if (last != null) {
                (last.getChildAt(0) as TextView).setTextColor(getColorResource(R.color.color_333333))
                last.getChildAt(1).visibility = View.INVISIBLE
            }
            (current.getChildAt(0) as TextView).setTextColor(getColorResource(R.color.color_3875f6))
            current.getChildAt(1).visibility = View.VISIBLE
            lastTimeType = current
        }
    }

    override fun onDateChange(requestcode: Int, start: String, end: String): Boolean {
        if (DateExtendUtil.getBetweenTime(
                Date_Format.YMD,
                DateExtendUtil.DifTimeType.DAY,
                start,
                end
            ) > 0
        ) {
            ToastUtil.show(context!!.getString(R.string.不允许结束日期早于开始日期))
            return false
        }

        viewBinding.rbDiy.isChecked = true

        viewBinding.rbOther.isChecked = true
        isDiy = true
        viewBinding.rbOther.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            ContextCompat.getDrawable(context, R.drawable.ic_new_unfold),
            null
        )

        if (CustomeBillDateFormat == "MM") {
            viewBinding.rbOther.text = "${start.substring(0, 7)}\n${end.substring(0, 7)}"
            viewBinding.tvOther.text = "${start.substring(0, 7)}\n${end.substring(0, 7)}"
        } else {
            viewBinding.rbOther.text = "${start.replace("-", "/")}\n${
                end.replace(
                    "-",
                    "/"
                )
            }"
            viewBinding.tvOther.text = "${start.replace("-", "/")}\n${
                end.replace(
                    "-",
                    "/"
                )
            }"
        }


        setDefaultShow(
            start,
            end,
            DateEnum.OTHER.name,
            CustomeBillDateFormat
        )

        return true
    }

    override fun onDismissPicker() {}

    override fun onDateFormat(type: String) {
        this.CustomeBillDateFormat = type
        DateFormatcallback?.invoke(type)
    }

    var DateFormatcallback: ((String) -> Unit?)? = null
}