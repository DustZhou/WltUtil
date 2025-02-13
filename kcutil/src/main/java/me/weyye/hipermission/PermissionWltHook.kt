package me.weyye.hipermission

import android.Manifest
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kenning.kcutil.R
import com.kenning.kcutil.utils.other.getStringResource
import java.lang.reflect.Field


/**
 *Description : View 权限判断
 *@author : KenningChen
 *Date : 2023-06-01
 */
object PermissionWltHook {

    private var mPerssionMap = hashMapOf<String, Array<String>>(
        PermissionGroup.PHOTO.name to arrayOf(Manifest.permission.CAMERA),
        PermissionGroup.BLUETOOTH.name to arrayOf(
            Manifest.permission.BLUETOOTH, Manifest
                .permission.BLUETOOTH_ADMIN
        ),
        PermissionGroup.PHONE.name to arrayOf(Manifest.permission.CALL_PHONE),
    )

    fun putAllPermission(mPerssionMap: HashMap<String, Array<String>>) {
        PermissionWltHook.mPerssionMap.putAll(mPerssionMap)
    }

    private class OnClickListenerProxy(
        private val listener: View.OnClickListener?,
        val pgroup: String, var toast: String
    ) :
        View.OnClickListener {
        override fun onClick(v: View) {
            if (listener != null) {
                if (mPerssionMap[pgroup] == null || mPerssionMap[pgroup]!!.isEmpty()) {
                    listener.onClick(v)
                    return
                }
                PermissionUtils.requestPermissions(v.context as Activity, mPerssionMap[pgroup]!!) {
                    if (it) {
                        listener.onClick(v)
                    } else {
                        Toast.makeText(v.context, toast, Toast.LENGTH_SHORT)
                    }
                }
            }
        }

    }


    fun hookView(view: View, pgroup: String, toast: String) {
        val oldOnClickListener = getClickListener(view) ?: return
        val newOnClickListener = OnClickListenerProxy(oldOnClickListener, pgroup, toast)
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
            Log.e("HookViewManager", "ClassNotFoundException: " + e.message)
        } catch (e: NoSuchFieldException) {
            Log.e("HookViewManager", "NoSuchFieldException: " + e.message)
        } catch (e: IllegalAccessException) {
            Log.e("HookViewManager", "IllegalAccessException: " + e.message)
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
//            Log.e("HookViewManager", "ClassNotFoundException: " + e.message)
        } catch (e: NoSuchFieldException) {
//            Log.e("HookViewManager", "NoSuchFieldException: " + e.message)
        } catch (e: IllegalAccessException) {
//            Log.e("HookViewManager", "IllegalAccessException: " + e.message)
        }
        return listener
    }
}

enum class PermissionGroup {
    /**拍照相关*/
    PHOTO,

    /**蓝牙连接*/
    BLUETOOTH,

    /**拨打电话权限*/
    PHONE
}

fun View.setPermissionHook(pgroup: String, toast: String = "") {
    //toast 不推荐为空，不然可以不支持是因为系统权限，还是物联通上的权限没有，准备用pgroup来判断
    val message = when (pgroup) {
        Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
            getStringResource(R.string.请允许管家婆物联通访问您的存储若不允许您将无法使用文件相关功能)
        }

        Manifest.permission.READ_EXTERNAL_STORAGE -> {
            getStringResource(R.string.请允许管家婆物联通访问您的存储若不允许您将无法使用文件相关功能)
        }

        Manifest.permission.RECORD_AUDIO -> {
            getStringResource(R.string.请允许管家婆物联通访问您的录音若不允许您将无法使用语音识别功能)
        }

        Manifest.permission.ACCESS_FINE_LOCATION -> {
            getStringResource(R.string.请允许管家婆物联通访问您的位置若不允许您将无法使用定位签到功能)
        }

        Manifest.permission.CAMERA -> {
            getStringResource(R.string.请允许管家婆物联通访问您的相机若不允许您将无法使用扫描和拍照功能)
        }

        else -> {
            getStringResource(R.string.请允许管家婆物联通访问您的蓝牙若不允许您将无法使用连接打印机功能)
        }
    }
    PermissionWltHook.hookView(this, pgroup, message)
}