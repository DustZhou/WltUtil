package com.kenning.kcutil.utils.fragmenthelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LifecycleOwner
import java.lang.Exception

/**
 *Description : fragment 工具类 处理关于fragment的跳,显示隐藏以及fragment的管理
 * @param fragmentManager
 * @param isSuper 是否是FragmentActivity下的FragmentManager
 *@author : KenningChen
 *Date : 2023-07-28
 */
class FragmentHelper(val fragmentManager_: FragmentManager, val isSuper: Boolean) {

    private var previousFragment: Fragment? = null

    /**
     * 获取当前fragment
     */
    fun getCurrentFragmen() = previousFragment

    /**
     * 加载多个fragment, 并显示第一个fragment
     * @param containerId Int 容器id
     * @param fragments Array<out Fragment> 要加载的fragment集合
     */
    fun loadMultipleFragment(containerId: Int = -1,vararg fragments: Fragment){
        fragments.forEach {
            fragmentManager_.beginTransaction()
                .add(containerId, it, it::class.java.simpleName)
                .addToBackStack(null)
                .commit()
            previousFragment = it
        }

        showFragment(fragments.first()::class.java.simpleName)
    }

    /**
     * 如果指定fragment不存在,添加一个,如果存在,则直接显示
     */
    fun addFragment(containerId: Int = -1, fragment: Fragment) {
        if (fragment == previousFragment) {
            return
        }
        if (previousFragment != null) {
            fragmentManager_.findFragmentByTag(previousFragment!!.javaClass.simpleName)?.apply {
                fragmentManager_.beginTransaction()
                    .hide(this)
                    .commit()
            }
        }

        val show = fragmentManager_.findFragmentByTag(fragment::class.java.simpleName)
        if (show == null) {
            fragmentManager_.beginTransaction()
                .add(containerId, fragment, fragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
        } else {
            fragmentManager_.beginTransaction()
                .show(show)
                .commit()
        }
        previousFragment = show?:fragment
    }

    /**
     * 显示指定tag的Fragment实例
     */
    fun showFragment(fragmentTag: String) {

        val show = fragmentManager_.findFragmentByTag(fragmentTag)

        if (show == null) {
            throw Exception("There is no fragment instance with the specified tag")
        } else {
            fragmentManager_.beginTransaction()
                .show(show)
                .commit()
        }

        if (previousFragment != null && previousFragment != show) {
            fragmentManager_.findFragmentByTag(previousFragment!!.javaClass.simpleName)?.apply {
                fragmentManager_.beginTransaction()
                    .hide(this)
                    .commit()
            }
        }

        previousFragment = show
    }

    /**
     * 跳转到指定fragment 并支持回传参数,返回后需要销毁该fragment所以不添加 addToBackStack(null)
     */
    fun startFragmentForResult(
        owner: LifecycleOwner,
        show: Fragment,
        containerId: Int = -1,
        hideSelf: Boolean = true,
        callback: (Bundle) -> Unit
    ) {
        fragmentManager_.apply {
            setFragmentResultListener(show::class.java.simpleName, owner) { s, resBundle ->
                clearFragmentResult(s)
                clearFragmentResultListener(s)
                callback(resBundle)
            }

            beginTransaction()
                .add(containerId, show, show::class.java.simpleName)
                .commit()

            if (previousFragment != null && hideSelf) {
                fragmentManager_.findFragmentByTag(previousFragment!!.javaClass.simpleName)?.apply {
                    fragmentManager_.beginTransaction()
                        .hide(this)
                        .commit()
                }
            }

            previousFragment = show
        }
    }

    /**
     * 关闭fragment,并携带参数bungle回传显示前一个fragment
     */
    fun closeFragment(fragment:Fragment,bundle: Bundle?=null){
        if (bundle!=null) {
            fragment.setFragmentResult(fragment::class.java.simpleName, bundle)
        }
        if (previousFragment != null) {
            showFragment(previousFragment!!.javaClass.simpleName)
        }
        fragmentManager_.beginTransaction().remove(fragment).commit()
    }
}