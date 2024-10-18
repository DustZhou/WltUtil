package com.kenning.kcutil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kenning.base.BaseBusinessFragment
import com.kenning.kcutil.databinding.FragmentFirstBinding
import com.kenning.kcutil.utils.datepicker.IPickerListener
import com.kenning.kcutil.utils.datepicker.PickerControl
import com.kenning.kcutil.utils.datepicker.TwoDatePickInterface
import com.kenning.kcutil.utils.math.CHENG
import com.kenning.kcutil.utils.math.keepPoint
import com.kenning.kcutil.utils.datepicker.TwoDatePickerBuilder

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
//            DatePickerBuilder(this).setBeginDate("2023-02-28")
//                .setEndDate("2024-02-29")
//                .setSingle(true)
//                .setRequestCode(111)
//                .setLoaction(PickerControl.ShowLocation.BOTTOM)
//                .start(R.id.fcvMain)
            val a = 11111.23333
            val b = 2.4
            Log.e("kenning",a.CHENG(b).CHENG("2,7").keepPoint(3))

            TwoDatePickerBuilder(requireActivity(),this)
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

    override fun onDateChange(requestcode: Int, start: String, end: String):Boolean {
        binding.textviewFirst.text = "$start - $end"
        return true
    }

    override fun onDateFormat(type: String) {

    }
}