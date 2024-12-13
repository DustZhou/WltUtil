package com.kenning.kcutil.utils.datepicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.kenning.kcutil.databinding.PopBillListTimeBinding
import com.kenning.kcutil.databinding.QueryreportdatelayoutBinding
import com.kenning.kcutil.utils.date.DateEnum
import com.kenning.kcutil.utils.date.DateExtendUtil
import com.kenning.kcutil.utils.date.Date_Format
import com.kenning.kcutil.utils.other.ToastUtil
import com.kenning.kcutil.utils.other.outOf
import com.kenning.kcutil.widget.basicview.BackGroundTextView
//import com.zyyoona7.popup.EasyPopup
import java.util.Date
import kotlin.math.abs

/**
 * Created by ZHT
 * Date 2024-08-23 15:13
 * FileName ReportDataLayout.kt
 * 描述：报表时间选择器
 */
class QueryReportDataLayout : LinearLayout, IPickerListener, TwoDatePickInterface {

    private lateinit var viewBinding: QueryreportdatelayoutBinding

    var BeginDate = ""

    var EndDate = ""

    var CustomeBillDateType = ""

    var CustomeBillDateFormat = ""

    var LimitDays = 0

    var _mActivity: FragmentActivity? = null

//    /**时间窗口*/
//    lateinit var mTimeEasyPop: EasyPopup
//
//    lateinit var mTimeBinding: PopBillListTimeBinding

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

    fun setTitle(mTitle: String): QueryReportDataLayout {
        viewBinding.tvTitle.text = mTitle
        return this
    }

    fun setTitleVisibility(mVisibility : Boolean): QueryReportDataLayout {
        if (mVisibility){
            viewBinding.tvTitle.visibility = View.VISIBLE
        }else{
            viewBinding.tvTitle.visibility = View.GONE
        }
        return this
    }

    @SuppressLint("ResourceType")
    fun initView(context: Context?) {
        viewBinding = QueryreportdatelayoutBinding.inflate(LayoutInflater.from(context), this, true)

    }


    @SuppressLint("SetTextI18n")
    fun setDefaultShow(
        activity: FragmentActivity,
        BeginDate: String,
        EndDate: String,
        CustomeBillDateType: String, CustomeBillDateFormat: String = "DD",
        block: (startdate: String, enddate: String, dateEnumName: String) -> Unit
    ): QueryReportDataLayout {
        this._mActivity = activity
        this.CustomeBillDateFormat = CustomeBillDateFormat

        if (CustomeBillDateType == "无") {
            this.BeginDate = BeginDate
            this.EndDate = EndDate
            this.CustomeBillDateType = "无"
            setSaleMainTypeView(activity, CustomeBillDateType, true)

            block(this.BeginDate, this.EndDate, this.CustomeBillDateType)

        } else {
            this.BeginDate = BeginDate
            this.EndDate = EndDate
            this.CustomeBillDateType = if (CustomeBillDateType == "") DateEnum.WEEK.name else CustomeBillDateType

            if (this.CustomeBillDateType == DateEnum.OTHER.name) {
                if (CustomeBillDateFormat == "MM") {
                    viewBinding.tvOther.text = BeginDate.substring(0, 7) + "至" + EndDate.substring(0, 7)
                } else {
                    viewBinding.tvOther.text = BeginDate + "至" + EndDate
                }
            } else {
                viewBinding.tvOther.text = "自定义"
            }

            setSaleMainTypeView(activity, this.CustomeBillDateType, true)

            block(this.BeginDate, this.EndDate, this.CustomeBillDateType)
        }
        return this
    }


    fun setDateEvent(
        activity: FragmentActivity,
        requestCode: Int = 0,
        block: (startdate: String, enddate: String, dateEnumName: String) -> Unit,
    ): QueryReportDataLayout {

        this.callback = block

        viewBinding.tvToDay.setOnClickListener {
            setSaleMainTypeView(activity, DateEnum.TODAY.name, false)
            block(this.BeginDate, this.EndDate, this.CustomeBillDateType)
        }
        viewBinding.tvYesterDay.setOnClickListener {
            setSaleMainTypeView(activity, DateEnum.YESTERDAY.name, false)
            block(this.BeginDate, this.EndDate, this.CustomeBillDateType)

        }
        viewBinding.tvWeek.setOnClickListener {
            setSaleMainTypeView(activity, DateEnum.WEEK.name, false)
            block(this.BeginDate, this.EndDate, this.CustomeBillDateType)

        }
        viewBinding.tvLastWeek.setOnClickListener {
            setSaleMainTypeView(activity, DateEnum.LAST_WEEK.name, false)
            block(this.BeginDate, this.EndDate, this.CustomeBillDateType)

        }
        viewBinding.tvMonth.setOnClickListener {
            setSaleMainTypeView(activity, DateEnum.MONTH.name, false)
            block(this.BeginDate, this.EndDate, this.CustomeBillDateType)

        }
        viewBinding.tvLastMonth.setOnClickListener {
            setSaleMainTypeView(activity, DateEnum.LAST_MONTH.name, false)
            block(this.BeginDate, this.EndDate, this.CustomeBillDateType)

        }
        viewBinding.tvLastSeason.setOnClickListener {
            setSaleMainTypeView(activity, DateEnum.LAST_QUARTER.name, false)
            block(this.BeginDate, this.EndDate, this.CustomeBillDateType)

        }
        viewBinding.tvSeason.setOnClickListener {
            setSaleMainTypeView(activity, DateEnum.THISQUARTER.name, false)
            block(this.BeginDate, this.EndDate, this.CustomeBillDateType)

        }
        viewBinding.tvYear.setOnClickListener {
            setSaleMainTypeView(activity, DateEnum.YEAR.name, false)
            block(this.BeginDate, this.EndDate, this.CustomeBillDateType)
        }
        viewBinding.tvOther.setOnClickListener {
            if (requestCode != 0) {
                setSaleMainTypeView(activity, DateEnum.OTHER.name, false, requestCode)
            } else {
                setSaleMainTypeView(activity, DateEnum.OTHER.name, false)
            }
        }
        return this
    }

    fun isDayStatistics(isDayStatistics: Boolean): QueryReportDataLayout {
        if (isDayStatistics) {

            viewBinding.tvLastWeek.visibility = View.GONE
            viewBinding.tvLastMonth.visibility = View.GONE
            viewBinding.tvSeason.visibility = View.GONE
            viewBinding.tvLastSeason.visibility = View.GONE
            viewBinding.tvYear.visibility = View.GONE
        }
        return this
    }

    fun setDisplayColumn(showLists: ArrayList<DateEnum>): QueryReportDataLayout {
        if (DateEnum.TODAY outOf showLists) {
            viewBinding.tvToDay.visibility = View.GONE
        }
        if (DateEnum.YESTERDAY outOf showLists) {
            viewBinding.tvYesterDay.visibility = View.GONE
        }
        if (DateEnum.WEEK outOf showLists) {
            viewBinding.tvWeek.visibility = View.GONE
        }
        if (DateEnum.LAST_WEEK outOf showLists) {
            viewBinding.tvLastWeek.visibility = View.GONE
        }
        if (DateEnum.MONTH outOf showLists) {
            viewBinding.tvMonth.visibility = View.GONE
        }
        if (DateEnum.LAST_MONTH outOf showLists) {
            viewBinding.tvLastMonth.visibility = View.GONE
        }
        if (DateEnum.THISQUARTER outOf showLists) {
            viewBinding.tvSeason.visibility = View.GONE
        }
        if (DateEnum.LAST_QUARTER outOf showLists) {
            viewBinding.tvLastSeason.visibility = View.GONE
        }
        if (DateEnum.YEAR outOf showLists) {
            viewBinding.tvYear.visibility = View.GONE
        }
        if (DateEnum.OTHER outOf showLists) {
            viewBinding.tvOther.visibility = View.GONE
        }
        return this
    }

    fun isLimitDays(mLimitDays: Int): QueryReportDataLayout {
        this.LimitDays = mLimitDays
        return this
    }


    fun setSaleMainTypeView(
        activity: FragmentActivity,
        CustomeBillDateType: String,
        isShow: Boolean,
        requestCode: Int = 0
    ) {
        //选项 view
        arrayListOf(
            viewBinding.tvToDay,
            viewBinding.tvYesterDay,
            viewBinding.tvWeek,
            viewBinding.tvLastWeek,
            viewBinding.tvMonth,
            viewBinding.tvLastMonth,
            viewBinding.tvSeason,
            viewBinding.tvLastSeason,
            viewBinding.tvYear,
            viewBinding.tvOther
        ).forEach { oldView ->
            oldView.setStrokeColor(Color.parseColor("#F8F8F8"))
            oldView.setNormalBackgroundColor(Color.parseColor("#F8F8F8"))
            oldView.setStrokeColor(Color.parseColor("#F8F8F8"))
            oldView.setTextColor(Color.parseColor("#7C7F8E"))
        }
        var newView: BackGroundTextView? = null

        when (CustomeBillDateType) {

            DateEnum.TODAY.name -> {

                BeginDate = DateExtendUtil.getCurrentDate()
                EndDate = DateExtendUtil.getCurrentDate()
                this.CustomeBillDateType = CustomeBillDateType
                newView = viewBinding.tvToDay
            }

            DateEnum.YESTERDAY.name -> {

                BeginDate = DateExtendUtil.getHistoryBeginDate(DateEnum.YESTERDAY)
                EndDate = BeginDate
                this.CustomeBillDateType = CustomeBillDateType
                newView = viewBinding.tvYesterDay
            }

            DateEnum.WEEK.name -> {

                BeginDate = DateExtendUtil.formatDate(DateExtendUtil.getMondayOfWeek(Date()))
                EndDate = DateExtendUtil.formatDate(DateExtendUtil.getSundayOfWeek(Date()))
                this.CustomeBillDateType = CustomeBillDateType
                newView = viewBinding.tvWeek
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
                this.CustomeBillDateType = CustomeBillDateType
                newView = viewBinding.tvLastWeek
            }

            DateEnum.MONTH.name -> {

                BeginDate = DateExtendUtil.formatDate(DateExtendUtil.getFirstDateOfMonth(Date()))
                EndDate = DateExtendUtil.formatDate(DateExtendUtil.getLastDateOfMonth(Date()))
                this.CustomeBillDateType = CustomeBillDateType
                newView = viewBinding.tvMonth
            }

            DateEnum.LAST_MONTH.name -> {

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
                this.CustomeBillDateType = CustomeBillDateType
                newView = viewBinding.tvLastMonth
            }

            DateEnum.THISQUARTER.name -> {
                BeginDate = DateExtendUtil.formatDate(DateExtendUtil.getFirstDateOfSeason(Date()))
                EndDate = DateExtendUtil.formatDate(DateExtendUtil.getLastDateOfSeason(Date()))
                this.CustomeBillDateType = CustomeBillDateType
                newView = viewBinding.tvSeason
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
                this.CustomeBillDateType = CustomeBillDateType
                newView = viewBinding.tvLastSeason
            }

            DateEnum.YEAR.name -> {

                BeginDate = "${DateExtendUtil.getYear(Date())}-01-01"
                EndDate = "${DateExtendUtil.getYear(Date())}-12-31"
                this.CustomeBillDateType = CustomeBillDateType
                newView = viewBinding.tvYear
            }

            "无" -> {
                BeginDate = ""
                EndDate = ""
                this.CustomeBillDateType = CustomeBillDateType
                newView = viewBinding.tvOther1
            }

            else -> {
                if (isShow) {
                    newView = viewBinding.tvOther
                } else {
                    TwoDatePickerBuilder(activity, this)
                        .setBeginDate(BeginDate)
                        .setEndDate(EndDate)
                        .setCanceledOnTouchOutside(false)
                        .setSingle(false)
                        .setDateFormat(CustomeBillDateFormat)
                        .setRequestCode(if (requestCode != 0) requestCode else abs(viewBinding.tvOther.hashCode()))
                        .setLoaction(PickerControl.ShowLocation.BOTTOM)
                        .start()
                    return
                }
            }
        }
        if (CustomeBillDateType != DateEnum.OTHER.name) {
            viewBinding.tvOther.text = "自定义"
        }
        newView.setStrokeColor(Color.parseColor("#2F72FE"))
        newView.setNormalBackgroundColor(Color.parseColor("#E6EEFF"))
        newView.setStrokeColor(Color.parseColor("#2F72FE"))
        newView.setTextColor(Color.parseColor("#2F72FE"))
    }


    override fun onDateChange(requestcode: Int, start: String, end: String): Boolean {
        if (this.LimitDays != 0) {
            if (DateExtendUtil.getBetweenTime(
                    Date_Format.YMD,
                    DateExtendUtil.DifTimeType.DAY,
                    end,
                    start
                ) > LimitDays
            ) {
                ToastUtil.show("仅能选择" + (LimitDays) + "天日期")
                return false
            }
        }
        if (DateExtendUtil.getBetweenTime(
                Date_Format.YMD,
                DateExtendUtil.DifTimeType.DAY,
                start,
                end
            ) > 0
        ) {
            ToastUtil.show("不允许结束日期早于开始日期")
            return false
        }

        this._mActivity?.let {
            setDefaultShow(it, start, end, DateEnum.OTHER.name, CustomeBillDateFormat) { m, n, y ->
                callback?.invoke(m, n, DateEnum.OTHER.name)
            }
        }
        return true
    }

    override fun onDismissPicker() {
        this._mActivity?.let { setSaleMainTypeView(it, CustomeBillDateType, true) }
    }

    override fun onDateFormat(type: String) {
        this.CustomeBillDateFormat = type
        DateFormatcallback?.invoke(type)
    }

    fun setCustomeBillDateFormat(block: (CustomeBillDateFormat: String) -> Unit): QueryReportDataLayout {
        this.DateFormatcallback = block
        return this
    }

    var callback: ((String, String, String) -> Unit?)? = null

    var DateFormatcallback: ((String) -> Unit?)? = null
}