package com.kenning.kcutil.utils.datepicker

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kenning.kcutil.KCUtil
import com.kenning.kcutil.R
import com.kenning.kcutil.databinding.ViewDatepickerBottomBinding
import com.kenning.kcutil.utils.date.DateExtendUtil
import com.kenning.kcutil.utils.other.getStringResource

/**
 * description：
 * author: KenningChen
 * date: ON 2022/9/16
 */
class DatePickerBottomFragment  : BottomSheetDialogFragment(),IDatePickerBase {

    private val mViewModel:PickerViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(KCUtil.application!!)
        ).get(PickerViewModel::class.java)
    }

    override lateinit var mView: View

    lateinit var control: PickerControl

    var bundle = Bundle()

    override var isSingleDate = false

    override var startdate = ""
    override var enddate = ""

    var alpha = 0.5f

    private val jr = DateExtendUtil.getTodayDateStr()
    private val zr = DateExtendUtil.getYestodayDateStr()
    private val week = DateExtendUtil.getNear7DaysDAteStr()
//
//    /**显示位置*/
//    var Location = PickerControl.ShowLocation.BOTTOM

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
//                    isEnabled = false
//                    requireActivity().supportFragmentManager.popBackStack()//onBackPressedDispatcher.onBackPressed()
//                    isEnabled = true
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
    ): View? {
        bundle = requireArguments()
//        Location = PickerControl.ShowLocation.valueOf(bundle.getString("location", "BOTTOM"))
//        alpha = bundle.getFloat("alpha", 0.5f)
        title = bundle.getString("title",getStringResource(R.string.选择日期))
//        if (Location == PickerControl.ShowLocation.BOTTOM) {
            mView = ViewDatepickerBottomBinding.inflate(layoutInflater).root
//        } else {
//        mView = ViewDatepickerBinding.inflate(layoutInflater).root
//        }
        return mView
    }

//    override fun onStart() {
//        getDialog()!!.getWindow()!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
//        super.onStart()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mView.findViewById<View>(R.id.backgroundWall).alpha = alpha
        getBeforeData()
        initview()
        control = PickerControl(this)
        control.setCurrentDate()
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

    }

    fun initview() {
        if (isSingleDate) {
            mView.findViewById<View>(R.id.topDateLayout).visibility = View.GONE
            mView.findViewById<View>(R.id.startview).visibility = View.GONE
            mView.findViewById<View>(R.id.layoutstart).visibility = View.GONE
            mView.findViewById<View>(R.id.endview).visibility = View.GONE
            mView.findViewById<View>(R.id.layoutend).visibility = View.GONE
        } else {
            //非单个日期选择的情况也显示标题
            mView.findViewById<View>(R.id.layoutDateCheck).visibility = View.VISIBLE
            mView.findViewById<View>(R.id.layoutOnly).visibility = View.GONE
        }
        mView.findViewById<TextView>(R.id.starttitle).text = title


        mView.findViewById<TextView>(R.id.startdate).text = startdate
//        mBinding.enddate.text = enddate
        isEditWork = false
        if (startdate == jr && enddate == jr) {//今日
            mView.findViewById<RadioGroup>(R.id.rgPicker).check(R.id.rbToday)
        } else if (startdate == zr && enddate == zr) {//昨日
            mView.findViewById<RadioGroup>(R.id.rgPicker).check(R.id.rbYesterday)
        } else if (startdate == week && enddate == jr) {//近7天
            mView.findViewById<RadioGroup>(R.id.rgPicker).check(R.id.rbWeek)
        } else {
            mView.findViewById<RadioGroup>(R.id.rgPicker).check(R.id.rbOther)
        }
        isEditWork = true
//        else if (startdate == thirtyDay && enddate == jr) {//近30天
//            mBinding.rgPicker.check(R.id.rbThirtyDays)
//        } else {
//            mBinding.rgPicker.check(R.id.rbOther)
//        }
    }

    fun bindClick() {
        mView.findViewById<View>(R.id.cancel).setOnClickListener {
            (mViewModel.tagetFragment.value as IPickerListener).onDismissPicker()
            dismiss()
        }

        mView.findViewById<View>(R.id.select).setOnClickListener {

            //日期回传
            val suc = (mViewModel.tagetFragment.value as IPickerListener).onDateChange(
                code, startdate, enddate
            )
//            needBackValues = true
            if (suc) {
                (mViewModel.tagetFragment.value as IPickerListener).onDismissPicker()
                dismiss()
            }
        }

//        mView.findViewById<View>(R.id.backgroundWall).setOnClickListener {
//            dismiss()
//        }

        //与背景墙点击事件重合，单独设置不触发
        mView.findViewById<View>(R.id.layoutDateCheck).setOnClickListener { }
        mView.findViewById<View>(R.id.layoutOnly).setOnClickListener { }
        mView.findViewById<View>(R.id.layoutstart).setOnClickListener { }
        mView.findViewById<View>(R.id.layoutend).setOnClickListener { }
        mView.findViewById<View>(R.id.viewbottom).setOnClickListener { }

        mView.findViewById<RadioGroup>(R.id.rgPicker)
            .setOnCheckedChangeListener { group, checkedId ->
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
            mView.findViewById<TextView>(R.id.startdate).text = "${year}-${month}-${day}"
            this.startdate = "${year}-${month}-${day}"
        } else {
            //赋值临时变量
            if (type == 0)
                startdate = "${year}-${month}-${day}"
            else
                enddate = "${year}-${month}-${day}"

            isEditWork = false
            if (startdate == jr && enddate == jr) {//今日
                mView.findViewById<RadioGroup>(R.id.rgPicker).check(R.id.rbToday)
            } else if (startdate == zr && enddate == zr) {//昨日
                mView.findViewById<RadioGroup>(R.id.rgPicker).check(R.id.rbYesterday)
            } else if (startdate == week && enddate == jr) {//近7天
                mView.findViewById<RadioGroup>(R.id.rgPicker).check(R.id.rbWeek)
            } else {
                mView.findViewById<RadioGroup>(R.id.rgPicker).check(R.id.rbOther)
            }
            isEditWork = true
        }
    }

//    override fun onBackPressedSupport(): Boolean {
//        (activity as BaseActivity).reSetStatusBar(thiscolor, thisalpha)
////        if (needBackValues)
//            (parentFragment as dismissViewListener).onDismissPicker()
//        pop()
//        return true
//    }

//
//    interface dismissViewListener {
//        fun onDismissPicker()
//        fun onDateChange(requestcode: Int, start: String, end: String)
//    }

}