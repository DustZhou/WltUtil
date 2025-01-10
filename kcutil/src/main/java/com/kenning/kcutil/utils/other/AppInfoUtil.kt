package com.kenning.kcutil.utils.other

import androidx.viewbinding.BuildConfig
import com.kenning.kcutil.KCUtil


/**
 *Description :App版本等方法
 *
 *author : created by shaohua.chen on 2021/3/2 2:58 PM
 */

private var VersionName = ""
private var PackageName = ""
/**获取 app 版本名称*/
fun getAppVersionName():String{
    if (VersionName.isEmpty()){
        VersionName = try {
            val pm = KCUtil.application!!.packageManager
            val pi = pm.getPackageInfo(KCUtil.application!!.packageName, 0)
            pi.versionName
        } catch ( e:Exception) {
            ""
        }
    }
    return VersionName
}

/**获取 app 版本名称*/
fun getAppPackageName():String{
    if (PackageName.isEmpty()){
        PackageName = try {
            val pm = KCUtil.application!!.packageManager
            val pi = pm.getPackageInfo(KCUtil.application!!.packageName, 0)
            pi.packageName
        } catch ( e:Exception) {
            ""
        }
    }
    return PackageName
}

/**
 * apk是否已安装
 * @param packageName 包名
 * @return Boolean
 */
fun isTargetAPKInstalled(packageName: String): Boolean{
    try {
        val pm = KCUtil.application!!.packageManager

        pm.getPackageInfo(packageName, 0)

        return true
    }catch (e:Exception){
        if (BuildConfig.DEBUG) {
        }
    }
    return false
}