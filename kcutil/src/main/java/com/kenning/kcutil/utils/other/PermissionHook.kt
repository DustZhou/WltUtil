package com.kenning.kcutil.utils.other

import android.Manifest
import android.app.Activity
import android.os.Build
import android.view.View
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import java.lang.reflect.Field


/**
 *Description : View 权限判断
 *@author : KenningChen
 *Date : 2023-06-01
 */
object PermissionHook {

    private var mPerssionMap = hashMapOf<String,Array<String>>(
        PermissionGroup.PHOTO.name to arrayOf(Manifest.permission.CAMERA),
        PermissionGroup.BLUETOOTH.name to arrayOf(Manifest.permission.BLUETOOTH,Manifest
            .permission.BLUETOOTH_ADMIN),
        PermissionGroup.PHONE.name to arrayOf(Manifest.permission.CALL_PHONE),
    )

    fun putAllPermission(mPerssionMap : HashMap<String,Array<String>>){
        this.mPerssionMap.putAll(mPerssionMap)
    }

//    private var photoPermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA)

    private class OnClickListenerProxy(private val listener: View.OnClickListener?,
                                       val pgroup:String, var toast:String = "") :
        View.OnClickListener {
        override fun onClick(v: View) {
            if (listener != null) {
                if (mPerssionMap[pgroup] == null || mPerssionMap[pgroup]!!.isEmpty()){
                    listener.onClick(v)
                    return
                }
                val rxPermissions = RxPermissions(v.context as Activity)
                val permisionlist = mPerssionMap[pgroup]!!
                rxPermissions.requestEach(*permisionlist)
                    .subscribe { permission: Permission ->
                        if (permission.granted) {
                            if (permisionlist[permisionlist.size - 1] == permission.name) {
                                listener.onClick(v)
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                        } else {
                            ToastUtil.show(toast.ifEmpty { "没有对应权限" })
                        }
                    }

            }
        }

//        private fun hook(view: View,pgroup:PermissionGroup) {
//
//        }
    }


    fun hookView(view: View,pgroup:String,toast:String="") {
        val oldOnClickListener = getClickListener(view) ?: return
        val newOnClickListener = OnClickListenerProxy(oldOnClickListener,pgroup,toast)
        view.setOnClickListener(newOnClickListener)
    }

    private fun getClickListener(view: View): View.OnClickListener? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getClickListenerV14(view)
        } else {
            getClickListenerV(view)
        }
    }

    /**
     * API 14 以下
     */
    private fun getClickListenerV(view: View): View.OnClickListener? {
        var listener: View.OnClickListener? = null
        try {
            val clazz = Class.forName("android.view.View")
            val field: Field = clazz.getDeclaredField("mOnClickListener")
            listener = field.get(view) as View.OnClickListener?
        } catch (e: ClassNotFoundException) {
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }
        return listener
    }

    /**
     * API 14 以上
     */
    private fun getClickListenerV14(view: View): View.OnClickListener? {
        var listener: View.OnClickListener? = null
        try {
            val clazz = Class.forName("android.view.View")
            val field = clazz.getDeclaredField("mListenerInfo")
            var listenerInfo: Any? = null
            if (field != null) {
                field.isAccessible = true
                listenerInfo = field[view]
            }
            val cls = Class.forName("android.view.View\$ListenerInfo")
            val declaredField = cls.getDeclaredField("mOnClickListener")
            if (declaredField != null && listenerInfo != null) {
                listener = declaredField[listenerInfo] as View.OnClickListener
            }
        } catch (e: ClassNotFoundException) {
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }
        return listener
    }
}

enum class PermissionGroup{
    /**拍照相关*/
    PHOTO,
    /**蓝牙连接*/
    BLUETOOTH,
    /**拨打电话权限*/
    PHONE
}

fun View.setHook(pgroup:String,toast:String=""){
    PermissionHook.hookView(this,pgroup,toast)
}