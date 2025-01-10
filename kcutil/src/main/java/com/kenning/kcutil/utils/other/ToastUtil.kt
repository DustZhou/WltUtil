package com.kenning.kcutil.utils.other

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import com.kenning.kcutil.KCUtil
import java.lang.reflect.Field

object ToastUtil {
    private var toast: Toast? = null
    private var sFieldTN: Field? = null
    private var sFieldTNHandler: Field? = null

    init {
        try {
            // android 11 版本以上Toast类重构过， 不再有参数mTN
            if (Build.VERSION.SDK_INT < 30) {
                sFieldTN = Toast::class.java.getDeclaredField("mTN")
                sFieldTN?.isAccessible = true
                sFieldTNHandler = sFieldTN?.type?.getDeclaredField("mHandler")
                sFieldTNHandler?.isAccessible = true
            } else {

            }
        } catch (e: Exception) {
//            e.printStackTrace()
        }
    }

    private fun hook(toast: Toast) {
        try {
            val tn = sFieldTN!![toast]
            val preHandler: Handler = sFieldTNHandler!![tn] as Handler
            sFieldTNHandler!![tn] = SafelyHandlerWrapper(preHandler)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private class SafelyHandlerWrapper constructor(private val impl: Handler) :
        Handler() {
        override fun dispatchMessage(msg: Message) {
            try {
                super.dispatchMessage(msg)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        override fun handleMessage(msg: Message) {
            impl.handleMessage(msg)
        }
    }

    fun show(msg: String) {
//        Toast.makeText(App.instance, msg, Toast.LENGTH_SHORT).show()
        try {
//            if (Build.VERSION.SDK_INT >= 30) {
                toast?.cancel()
                toast = Toast.makeText(KCUtil.application, msg, Toast.LENGTH_SHORT)
//            } else {
//                if (toast == null) {
//                    toast = Toast.makeText(KCUtil.application, msg, Toast.LENGTH_SHORT)
//                } else {
//                    toast!!.setText(msg)
//                    toast!!.duration = Toast.LENGTH_SHORT
//                }
//                hook(toast!!)
//            }
            toast?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelToast() {
        toast?.cancel()
    }
}