package com.kenning.kcutil

import android.os.Bundle
import android.util.Log
import com.kenning.base.BaseActivity
import com.kenning.kcutil.databinding.ActivityMainBinding
import com.kenning.kcutil.utils.date.DateExtendUtil
import com.kenning.kcutil.utils.date.Date_Format
import com.kenning.kcutil.utils.date.formatBy
import com.kenning.kcutil.utils.datepicker.IPickerListener
import com.kenning.kcutil.utils.other.PermissionGroup
import com.kenning.kcutil.utils.other.setHook
import java.util.Date

class MainActivity : BaseActivity(), IPickerListener {

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val da = DateExtendUtil.getBalanceDateByDay(
            Date().formatBy(Date_Format.YMD),
            Date_Format.YMD,
            5
        )
        Log.e("kenning", da)
        loadRootFragment(binding.fcvMain.id, FirstFragment())

        binding.fab.setOnClickListener { view ->
//            lifecycleScope.launch {
//
//                val view_body = LayoutInflater.from(this@MainActivity).inflate(
//                    R.layout.view_test_dialog, null
//                )
//                view_body.view1.setOnClickListener { ToastUtil.show("click view1") }
//                view_body.view2.setOnClickListener { ToastUtil.show("click view2") }
//
//
//                val result = BaseFragmentDialog(view_body)
//                    .setTitle("测试")
//                    .setButtonMode(
//                        DialogFragmentButtonMode("hh"),
//                        DialogFragmentButtonMode("YY")
//                    )
//                    .showAsSuspend(
//                        supportFragmentManager,
//                        BaseFragmentDialog::class.java.simpleName
//                    )
//                if (result.toString() == "YY"){
//                    ToastUtil.show("click yy")
//                }else{
//                    ToastUtil.show("click other")
//                }
//            }
        }
        binding.tagswitch.setOnSwitchSuspendListener{

        }
        binding.tagswitch.setHook(
            PermissionGroup.PHONE.name,
            "没有电话权限,无法执行该功能,请先去设置权限"
        )
    }

    override fun closeAct() {
        TODO("Not yet implemented")
    }

    override fun getBeforeData() {
        TODO("Not yet implemented")
    }

    override fun defaultData() {
        TODO("Not yet implemented")
    }

    override fun dealData() {
        TODO("Not yet implemented")
    }

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun bindClickEvent() {
        TODO("Not yet implemented")
    }

    override fun reLoadApp() {
        TODO("Not yet implemented")
    }

    override fun onDismissPicker() {

    }

    override fun onDateChange(requestcode: Int, start: String, end: String): Boolean {
        binding.tag11.text = start
        return true
    }

}