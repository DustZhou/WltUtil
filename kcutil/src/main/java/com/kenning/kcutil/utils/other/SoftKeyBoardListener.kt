package com.kenning.kcutil.utils.other

import android.app.Activity
import android.graphics.Rect
import android.view.View
import com.kenning.kcutil.utils.other.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
/**
* @Description:软键盘监听动态调整布局高度
* @author: create by zyl on 2024/10/29
*/
class SoftKeyBoardListener(activity: Activity) {
    // activity的根视图
    private val rootView : View
    // 记录根视图显示的高度
    private var rootViewVisibleHeight = 0
    private var onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener? = null

    interface OnSoftKeyBoardChangeListener {
        fun keyBoardShow(height: Int)
        fun keyBoardHide(height: Int)
    }

    fun setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener?) {
        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener
    }

    init {
        // 获取activity的根视图
        rootView = activity.window.decorView
        // 监听视图树中全局布局发生改变或者视图树中某个视图的可视状态发生改变
        rootView.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener { // 获取当前根视图在屏幕上显示的大小
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val visibleHeight = rect.height()
            if (rootViewVisibleHeight == 0) {
                rootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }

            //根视图显示高度没有变化，可以看做软键盘显示/隐藏状态没有变化
            if (rootViewVisibleHeight == visibleHeight) {
                return@OnGlobalLayoutListener
            }

            // 根视图显示高度变小超过200，可以看做软键盘显示了
            if (rootViewVisibleHeight - visibleHeight > 200) {
                if (onSoftKeyBoardChangeListener != null) {
                    onSoftKeyBoardChangeListener!!.keyBoardShow(rootViewVisibleHeight - visibleHeight)
                }
                rootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }

            // 根视图显示高度变大超过了200，可以看做软键盘隐藏了
            if (visibleHeight - rootViewVisibleHeight > 200) {
                if (onSoftKeyBoardChangeListener != null) {
                    onSoftKeyBoardChangeListener!!.keyBoardHide(visibleHeight - rootViewVisibleHeight)
                }
                rootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }
        })
    }
}