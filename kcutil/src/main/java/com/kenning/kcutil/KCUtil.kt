package com.kenning.kcutil

import android.app.Application

/**
 *Description :工具类的入口,初始化的地方,在项目application中初始化
 *
 *Date : 2022/9/5
 *@author : KenningChen
 */
object KCUtil {
    var ScanClassAbPath = ""
    fun setScanClassPath(path:String){
        this.ScanClassAbPath = path
    }
    internal var application:Application?=null

    fun initKCUtil(application: Application){
        this.application = application
    }
}