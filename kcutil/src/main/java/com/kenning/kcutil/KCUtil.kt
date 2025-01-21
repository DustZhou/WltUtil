package com.kenning.kcutil

import android.app.Activity
import android.app.Application

/**
 *Description :工具类的入口,初始化的地方,在项目application中初始化
 *
 *Date : 2022/9/5
 *@author : KenningChen
 */
object KCUtil {
    var ScanClassAbPath = ""
    fun setScanClassPath(path: String) {
        this.ScanClassAbPath = path
    }
    //预计1.0.11才会有这个功能，2024-11-12+
    private var scanResultCode = "data"
    fun setScanResultCode(code: String) {
        this.scanResultCode = code
    }

    fun getScanResultCode(): String {
        return this.scanResultCode
    }

    internal var application: Application? = null

    fun initKCUtil(application: Application) {
        this.application = application
    }

    var mCurrentAct : Activity?=null
}