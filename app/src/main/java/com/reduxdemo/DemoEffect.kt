package com.reduxdemo

import android.widget.Toast
import ceneax.app.lib.redux.EmptyReduxSlot
import ceneax.app.lib.redux.ReduxEffect

/**
 *Description : 处理Act/Fgm的事件
 *
 *Date : 2022/9/29
 *@author : KenningChen
 */
class DemoEffect: ReduxEffect<DemoViewModel, EmptyReduxSlot>(){

    private val TAG = "KENNING"

    //测试事件
    fun showTost(content:String){
    }

    fun changetext(content:String){
        //...
        stateManager.updatatext(content)
    }

    fun changetext2(content:String){
        //...
        stateManager.updatatext2(content)
    }
}