package com.kenning.kcutil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kenning.base.BaseBusinessFragment
import com.kenning.kcutil.databinding.FragmentFirstBinding
import com.kenning.kcutil.utils.date.DateEnum
import com.kenning.kcutil.utils.date.DateExtendUtil
import com.kenning.kcutil.utils.datepicker.IPickerListener
import com.kenning.kcutil.utils.datepicker.PickerControl
import com.kenning.kcutil.utils.datepicker.TwoDatePickInterface
import com.kenning.kcutil.utils.math.CHENG
import com.kenning.kcutil.utils.math.keepPoint
import com.kenning.kcutil.utils.datepicker.TwoDatePickerBuilder
import com.kenning.kcutil.utils.other.ToastUtil
import java.util.Date

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : BaseBusinessFragment(), IPickerListener, TwoDatePickInterface {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    /**开始日期：yyyy-MM-DD*/
    var BeginDate: String = DateExtendUtil.formatDate(DateExtendUtil.getMondayOfWeek(Date()))

    /**截止日期：yyyy-MM-DD*/
    var EndDate: String = DateExtendUtil.formatDate(DateExtendUtil.getSundayOfWeek(Date()))
    var CustomeBillDateType = DateEnum.WEEK.name

    /**记录非自定义时段的时间类型  MM 按月*/
    var CustomeBillDateFormat = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //用于搜索栏的方式
        binding.etBillCode.setRightImageClickEvent {
            ToastUtil.show("点击了右边图标，可以更换其图标，通用常用是语音识别")
        }
        //用于搜索栏的方式
        binding.etBillCode.setOnTextChangedListener {
            if (it.contains("\n")) {
                ToastUtil.show("内容变化了，一般用于在线报表，回车后进行搜索" + it)
            } else {
                ToastUtil.show("内容变化了，一般用于本地选择器，即搜即得" + it)
            }
        }
        //用于选择栏的方式（暗色）
        binding.etBillCode1.setOnClickListener {
            ToastUtil.show("点击了,模拟赋值下")
            binding.etBillCode1.getEdittext().setText("123456789")
        }
        //用于选择栏的方式（暗色）
        binding.etBillCode1.setOnTextChangedListener {
            if (it == "") {
                ToastUtil.show("因为是textview，主要用于监听删除时把内容也清空，要设置这个方法才会动态显示隐藏删除按钮")
            }
        }
        //用于选择栏的方式（亮色）
        binding.etBillCode2.setOnClickListener {
            ToastUtil.show("点击了,模拟赋值下")
            binding.etBillCode2.getEdittext().setText("帆帆帆帆")
        }
        //用于选择栏的方式（亮色）
        binding.etBillCode2.setOnTextChangedListener {
            if (it == "") {
                ToastUtil.show("因为是textview，主要用于监听删除时把内容也清空，要设置这个方法才会动态显示隐藏删除按钮")
            }
        }
        fun onTimeCall(startdate: String, enddate: String, dateEnumName: String) {
            BeginDate = startdate
            EndDate = enddate
            CustomeBillDateType = dateEnumName
            Log.e("///", "BeginDate=$BeginDate EndDate=$EndDate dateEnumName=$dateEnumName")
        }
        // 日期1
        binding.layoutDate.setTitle("日期")
            .setDisplayColumn(arrayListOf(DateEnum.TODAY, DateEnum.YESTERDAY, DateEnum.WEEK, DateEnum.OTHER))
            .setDefaultShow(
                requireActivity(), BeginDate, EndDate, CustomeBillDateType, CustomeBillDateFormat, ::onTimeCall
            ).setDateEvent(requireActivity(), 0, ::onTimeCall).setCustomeBillDateFormat {
                CustomeBillDateFormat = it
                Log.e("///", "CustomeBillDateFormat=$it")
            }

        // 日期2
        binding.layoutDate2.setDefaultShow(
            BeginDate,
            EndDate,
            CustomeBillDateType,
            CustomeBillDateFormat
        ).setRadioChangListenner(::onTimeCall).setDateEvent(requireActivity(), ::onTimeCall).setCustomeBillDateFormat {
            CustomeBillDateFormat = it
            Log.e("///", "2 CustomeBillDateFormat=$it")
        }


        binding.buttonFirst.setOnClickListener {
//            DatePickerBuilder(this).setBeginDate("2023-02-28")
//                .setEndDate("2024-02-29")
//                .setSingle(true)
//                .setRequestCode(111)
//                .setLoaction(PickerControl.ShowLocation.BOTTOM)
//                .start(R.id.fcvMain)
            val a = 11111.23333
            val b = 2.4
            Log.e("kenning", a.CHENG(b).CHENG("2,7").keepPoint(3))

            TwoDatePickerBuilder(requireActivity(), this)
                .setBeginDate("")
                .setEndDate("")
                .setSingle(false)
                .setDateFormat("MM")
                .setRequestCode(1)
                .setLoaction(PickerControl.ShowLocation.BOTTOM)
                .start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismissPicker() {
//        Log.d("kenning", "日期选择器关闭")
    }

    override fun onDateChange(requestcode: Int, start: String, end: String): Boolean {
        binding.textviewFirst.text = "$start - $end"
        return true
    }

    override fun onDateFormat(type: String) {

    }
}