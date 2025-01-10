package com.kenning.kcutil.utils.other

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupWindow
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.kenning.kcutil.KCUtil
import com.kenning.kcutil.utils.fragmenthelper.FragmentHelper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 *Description :
 *@author : KenningChen
 *Date : 2021/9/14
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
fun <T:Serializable>T. deepCopy(): T {
    try {
        ByteArrayOutputStream().use { byteOut ->
            ObjectOutputStream(byteOut).use { outputStream ->
                outputStream.writeObject(this)
                ByteArrayInputStream(byteOut.toByteArray()).use { byteIn ->
                    ObjectInputStream(byteIn).use {
                            inputStream ->
                        return inputStream.readObject() as T
                    }
                }
            }
        }
    } catch (e: Exception) {
    }
    return this
}

/**数据是否在指定范围内*/
infix fun  <T:Any>T.inOf(list:ArrayList<T>):Boolean{
    return list.indexOf(this) != -1
}

infix fun  <T:Any>T.outOf(list:ArrayList<T>):Boolean{
    return list.indexOf(this) == -1
}

fun getStringResource(@StringRes stringid:Int):String{
    return KCUtil.application?.resources?.getString(stringid)?:""
}

fun getStringResource(@StringRes stringid:Int,vararg values: Any?):String{
    val source = KCUtil.application?.resources?.getString(stringid)?:""
    return String.format(source,*values)
}

fun getColorResource(@ColorRes colorid:Int):Int{
    KCUtil.application?.resources?.let {
        return ResourcesCompat.getColor(it,colorid,null)
    }?: run {
        return -1
    }

}

fun getDrawableResource(@DrawableRes drawableid:Int): Drawable?{
    KCUtil.application?.resources?.let {
        return ResourcesCompat.getDrawable(
            it,
            drawableid,
            null
        )
    }?: run {
        return null
    }
}

fun  getDimensionResource(@DimenRes id:Int):Float{
    return KCUtil.application?.resources?.getDimension(id)?:0f
}

/**
 * 判断字符是否为中文或其他特殊字符
 */
fun isChinese(c: Char): Boolean {
    return when {
        c.toString() == "￥" -> true
        c.toString() == "￠" -> true
        else -> c.toByte().toChar() != c
    }// 根据字节码判断
}

fun kcBlock(block:()->Unit):()->Unit{
    return block
}


fun Activity.CloseSoftInput() {
    if (this.currentFocus != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            this.currentFocus!!
                .windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0) //强制隐藏键盘
    }else{
        val imm =  this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.window.decorView.windowToken, 0)
    }
}

fun EditText.CloseSoftInput() {
    when(this.context){
        is Activity -> {
            val imm =  (this.context as Activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.windowToken, 0)
        }
        is ContextThemeWrapper -> {
            val imm =  (this.context as ContextThemeWrapper).getSystemService(Context.INPUT_METHOD_SERVICE) as
                    InputMethodManager
            imm.hideSoftInputFromWindow(this.windowToken, 0)
        }
    }
}

fun Fragment.CloseSoftInput() {
    val imm = this.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    imm.hideSoftInputFromWindow(this.requireActivity().window.decorView.windowToken, 0) //强制隐藏键盘
}

fun PopupWindow.CloseSoftInput() {
    val imm = this.contentView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    imm.hideSoftInputFromWindow(this.contentView.windowToken, 0) //强制隐藏键盘
}

fun Dialog.CloseSoftInput() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    imm.hideSoftInputFromWindow(this.window!!.decorView.windowToken, 0) //强制隐藏键盘
}

fun FragmentHelper.closeFragment_(fragment: Fragment, bundle: Bundle?=null){
    if (bundle!=null) {
        fragment.setFragmentResult(fragment::class.java.simpleName, bundle)
    }
    fragmentManager_.beginTransaction().remove(fragment).commit()
}
