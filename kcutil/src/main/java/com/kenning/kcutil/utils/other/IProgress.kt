package com.kenning.kcutil.utils.other

import android.content.Context

/**
 *Description :
 *author : KenningChen
 *Date : 2021/7/1
 */
interface IProgress {
    /**
     * 加载进度框
     * @param context String
     * @param isCancelable Boolean
     */
    fun showProgress(context: String = "", isCancelable: Boolean = false)

    /**
     * 取消进度框
     */
    fun dismissProgress()
}

fun Context.dismissProgress() {
    when (this) {
        is IProgress -> this.dismissProgress()
        else -> {}
    }
}

fun Context.showProgress(content: String, isCancelable: Boolean) {
    when (this) {
        is IProgress -> this.showProgress(content, isCancelable)
        else -> {}
    }
}