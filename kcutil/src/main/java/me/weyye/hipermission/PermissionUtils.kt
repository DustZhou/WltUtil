package me.weyye.hipermission

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import com.kenning.kcutil.R
import com.kenning.kcutil.utils.other.getStringResource
import java.util.*

/**
 * Description : 权限工具类
 * @author : zyl
 * Date : 2023-10-07 修改
 */
object PermissionUtils {


    /**
     * todo 现在就定位、相机、蓝牙（else），如果要用其他权限，这里要添加对应图标和文字
     */
    @SuppressLint("CheckResult")
    fun requestPermissions(activity: Activity, permissions: Array<String>, callback: (Boolean) -> Unit) {
        if (permissions.find {
                it == Manifest.permission.BLUETOOTH_SCAN
            } != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            // 蓝牙低于Android12 默认给授权的
            callback.invoke(true)
            return
        }
        val permissionItems = ArrayList<PermissionItem>()
        var title = ""
        var message = ""
        permissions.forEach {
            when (it) {
                Manifest.permission.WRITE_EXTERNAL_STORAGE->{
                    title = getStringResource(R.string.温馨提示)
                    message = getStringResource(R.string.请允许管家婆物联通访问您的存储若不允许您将无法使用文件相关功能)
                    permissionItems.add(
                        PermissionItem(it, getStringResource(R.string.文件写入), R.drawable.permission_ic_storage)
                    )
                }
                Manifest.permission.READ_EXTERNAL_STORAGE->{
                    title = getStringResource(R.string.温馨提示)
                    message = getStringResource(R.string.请允许管家婆物联通访问您的存储若不允许您将无法使用文件相关功能)
                    permissionItems.add(
                        PermissionItem(it, getStringResource(R.string.文件查看), R.drawable.permission_ic_storage)
                    )
                }
                Manifest.permission.RECORD_AUDIO -> {
                    title = getStringResource(R.string.温馨提示)
                    message = getStringResource(R.string.请允许管家婆物联通访问您的录音若不允许您将无法使用语音识别功能)
                    permissionItems.add(
                        PermissionItem(it, getStringResource(R.string.录音), R.drawable.permission_ic_micro_phone)
                    )
                }
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    title = getStringResource(R.string.温馨提示)
                    message = getStringResource(R.string.请允许管家婆物联通访问您的位置若不允许您将无法使用定位签到功能)
                    permissionItems.add(
                        PermissionItem(it, getStringResource(R.string.位置), R.drawable.permission_ic_location)
                    )
                }
                Manifest.permission.CAMERA -> {
                    title = getStringResource(R.string.温馨提示)
                    message = getStringResource(R.string.请允许管家婆物联通访问您的相机若不允许您将无法使用扫描和拍照功能)
                    permissionItems.add(
                        PermissionItem(it, getStringResource(R.string.相机), R.drawable.permission_ic_camera)
                    )
                }
                else -> {
                    title = getStringResource(R.string.温馨提示)
                    message = getStringResource(R.string.请允许管家婆物联通访问您的蓝牙若不允许您将无法使用连接打印机功能)
                    permissionItems.add(
                        PermissionItem(it, getStringResource(R.string.蓝牙), R.drawable.permission_ic_sensors)
                    )
                }
            }
        }
        HiPermission.create(activity)
            .title(title)
            .permissions(permissionItems)
            .filterColor(
                ResourcesCompat.getColor(
                    activity.resources,
                    R.color.colorPrimary,
                    activity.theme
                )
            )//图标的颜色
            .msg(message)
            .style(R.style.PermissionBlueStyle)
            .checkMutiPermission(object : PermissionCallback {
                override fun onClose() {
                    callback(false)
                }

                override fun onFinish() {
                    callback(true)
                }

                override fun onDeny(permisson: String, position: Int) {
                    callback(false)
                }

                override fun onGuarantee(permisson: String, position: Int) = Unit
            });
    }

}