package com.kenning.kcutil

import android.app.Activity
import android.app.Application
import android.os.Bundle
import me.yokeyword.fragmentation.Fragmentation

/**
 *Description :
 *
 *Date : 2022/9/5
 *@author : KenningChen
 */
class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KCUtil.initKCUtil(this)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                KCUtil.mCurrentAct  = activity
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
        Fragmentation.builder()
            // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
            .stackViewMode(Fragmentation.BUBBLE)
            .debug(BuildConfig.DEBUG) // 实际场景建议.debug(BuildConfig.DEBUG)
            .install();
    }
}