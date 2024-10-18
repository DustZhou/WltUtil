package com.kenning.kcutil.utils.datepicker


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kenning.kcutil.KCUtil
import com.kenning.kcutil.R
import com.kenning.kcutil.utils.date.DateExtendUtil
import com.kenning.kcutil.databinding.ViewTwodatepickerBottomBinding
import com.kenning.kcutil.utils.date.Date_Format

/**
 *
 */
class TwoDatePickerBottomFragment : BottomSheetDialogFragment(), IDatePickerBase {

    private val mViewModel: PickerViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(KCUtil.application!!)
        ).get(PickerViewModel::class.java)
    }

    override lateinit var mView: View

    private lateinit var viewBinding: ViewTwodatepickerBottomBinding

    lateinit var control: PickerControl

    var bundle = Bundle()

    override var isSingleDate = false

    override var startdate = ""
    override var enddate = ""

    var alpha = 0.5f

    private val jr = DateExtendUtil.getTodayDateStr()
    private val zr = DateExtendUtil.getYestodayDateStr()
    private val week = DateExtendUtil.getNear7DaysDAteStr()

    var type = ""
    var code = -1

    private lateinit var mContext: Context

    //日期标题
    var title = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (mViewModel.tagetFragment.value as IPickerListener).onDismissPicker()
                    dismiss()
                }
            })
    }

    override fun getTheme(): Int {
        return R.style.KCBottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bundle = requireArguments()
        title = bundle.getString("title", "选择日期")
        viewBinding = ViewTwodatepickerBottomBinding.inflate(layoutInflater)
        mView = viewBinding.root
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBeforeData()
        initview()
        control = PickerControl(this)
        bindClick()

        if (code == -1) {
//            ToastUtil.showToast("缺少参数 RequestCode")
            dismiss()
        }
    }

    /**
     * 禁止[BottomSheetDialogFragment]的用户手势操作
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            val bottomSheet = (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }

        // Do something with your dialog like setContentView() or whatever
        return dialog
    }

    fun getBeforeData() {

        isSingleDate = bundle.getBoolean("isSingleDate", false)
        startdate = bundle.getString("start", "")
        enddate = bundle.getString("end", "")
        code = bundle.getInt("code", -1)
        type = bundle.getString("dateFormat", "")

    }

    fun initview() {
        if (isSingleDate) {
            viewBinding.topDateLayout.visibility = View.GONE
            viewBinding.startview.visibility = View.GONE
           viewBinding.layoutstart.visibility = View.GONE
           viewBinding.endview.visibility = View.GONE
           viewBinding.layoutend.visibility = View.GONE
            viewBinding.rgDatePicker.visibility = View.GONE
            viewBinding.starttitle.visibility = View.VISIBLE
        } else {
           viewBinding.rgDatePicker.visibility = View.VISIBLE
           viewBinding.layoutOnly.visibility = View.GONE
           viewBinding.starttitle.visibility = View.GONE
        }

       viewBinding.starttitle.text = title

       viewBinding.startdate.text = startdate
//        mBinding.enddate.text = enddate
        isEditWork = false
        if (startdate == jr && enddate == jr) {//今日
           viewBinding.rgPicker.check(R.id.rbToday)
        } else if (startdate == zr && enddate == zr) {//昨日
           viewBinding.rgPicker.check(R.id.rbYesterday)
        } else if (startdate == week && enddate == jr) {//近7天
           viewBinding.rgPicker.check(R.id.rbWeek)
        } else {
           viewBinding.rgPicker.check(R.id.rbOther)
        }
        isEditWork = true
//        else if (startdate == thirtyDay && enddate == jr) {//近30天
//            mBinding.rgPicker.check(R.id.rbThirtyDays)
//        } else {
//            mBinding.rgPicker.check(R.id.rbOther)
//        }

        if (type == "MM") {
//           viewBinding.switchType.performClick()
            viewBinding.rbDatePickerLab2.isChecked = true
            viewBinding.startview.text = "开始月份"
            viewBinding.endview.text = "结束月份"
            viewBinding.day.visibility = View.GONE
            viewBinding.dayEnd.visibility = View.GONE
            viewBinding.dayonly.visibility = View.GONE
        }else{
            viewBinding.rbDatePickerLab1.isChecked = true
            viewBinding.startview.text = "开始日期"
            viewBinding.endview.text = "结束日期"
            viewBinding.day.visibility = View.VISIBLE
            viewBinding.dayEnd.visibility = View.VISIBLE
            viewBinding.dayonly.visibility = View.VISIBLE
        }


    }

    fun bindClick() {
       viewBinding.cancel.setOnClickListener {
            (mViewModel.tagetFragment.value as IPickerListener).onDismissPicker()
            dismiss()
        }
        //日期回传
       viewBinding.select.setOnClickListener {
            if (type == "MM") {
                //取月的第一天
                startdate = startdate.substring(0, 7) + "-01"
                //取月的最后一天
                enddate = DateExtendUtil.getDayDelayByMonth(enddate, 0)
            }
            (mViewModel.tagetFragment.value as TwoDatePickInterface).onDateFormat(type)
            //日期回传
            val suc = (mViewModel.tagetFragment.value as IPickerListener).onDateChange(
                code, startdate, enddate
            )
            if (suc) {
                (mViewModel.tagetFragment.value as IPickerListener).onDismissPicker()
                dismiss()
            }
        }

        //与背景墙点击事件重合，单独设置不触发
       viewBinding.layoutDateCheck.setOnClickListener { }
       viewBinding.layoutOnly.setOnClickListener { }
       viewBinding.layoutstart.setOnClickListener { }
       viewBinding.layoutend.setOnClickListener { }
       viewBinding.viewbottom.setOnClickListener { }

       viewBinding.rgPicker.setOnCheckedChangeListener { group, checkedId ->
                if (isEditWork)
                    if (checkedId != R.id.rbOther) {
                        if (checkedId == R.id.rbToday) {
                            startdate = jr
                            enddate = jr
                        } else if (checkedId == R.id.rbYesterday) {
                            startdate = zr
                            enddate = zr
                        } else if (checkedId == R.id.rbWeek) {
                            startdate = week
                            enddate = jr
                        }
                        control.getDateString()
                        control.setCurrentDate(true)
                    }
            }

        control.bindClick()

        //切换日/月
       viewBinding.switchType.setOnSwitchListener {
            if (it) {
                type = "DD"
               viewBinding.startview.text = "开始日期"
               viewBinding.endview.text = "结束日期"
               viewBinding.day.visibility = View.VISIBLE
               viewBinding.dayEnd.visibility = View.VISIBLE
               viewBinding.dayonly.visibility = View.VISIBLE
            } else {
                type = "MM"
               viewBinding.startview.text = "开始月份"
               viewBinding.endview.text = "结束月份"
               viewBinding.day.visibility = View.GONE
               viewBinding.dayEnd.visibility = View.GONE
               viewBinding.dayonly.visibility = View.GONE

            }
        }
        control.setCurrentDate()

       viewBinding.rgDatePicker.setOnCheckedChangeListener { radioGroup, i ->

            if (viewBinding.rbDatePickerLab1.isChecked) {
                type = "DD"
               viewBinding.startview.text = "开始日期"
               viewBinding.endview.text = "结束日期"
               viewBinding.day.visibility = View.VISIBLE
               viewBinding.dayEnd.visibility = View.VISIBLE
               viewBinding.dayonly.visibility = View.VISIBLE
            } else {
                type = "MM"
               viewBinding.startview.text = "开始月份"
               viewBinding.endview.text = "结束月份"
               viewBinding.day.visibility = View.GONE
               viewBinding.dayEnd.visibility = View.GONE
               viewBinding.dayonly.visibility = View.GONE

            }
        }




    }

    var isEditWork = true

    var needBackValues = false

    /**
     * type 0 开始 1 结束
     * */
    override fun putDateToView(vararg date: String, type: Int, isSignal: Boolean) {
        var year = date[0]
        var month = date[1]
        var day = date[2]

        @SuppressLint("SetTextI18n")
        if (isSignal) {
           viewBinding.startdate.text = "${year}-${month}-${day}"
            this.startdate = "${year}-${month}-${day}"
        } else {
            //赋值临时变量
            if (type == 0)
                startdate = "${year}-${month}-${day}"
            else
                enddate = "${year}-${month}-${day}"

            isEditWork = false
            if (startdate == jr && enddate == jr) {//今日
               viewBinding.rgPicker.check(R.id.rbToday)
            } else if (startdate == zr && enddate == zr) {//昨日
               viewBinding.rgPicker.check(R.id.rbYesterday)
            } else if (startdate == week && enddate == jr) {//近7天
               viewBinding.rgPicker.check(R.id.rbWeek)
            } else {
               viewBinding.rgPicker.check(R.id.rbOther)
            }
            isEditWork = true
        }
    }


}