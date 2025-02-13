package com.kenning.base

//import com.gyf.barlibrary.ImmersionBar
//import com.gyf.barlibrary.OSUtils
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.kenning.kcutil.utils.language.LanguageUtil
import org.jetbrains.annotations.NotNull

/**
 *Description :
 *
 *author : created by shaohua.chen on 2021/1/29 2:45 PM
 */
abstract class BaseActivity : SupportActivity()/*, IProgress*/ {
    /**
     * 此方法先于 onCreate()方法执行 。用于加载配置的语言
     */
    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(context))
    }
    var reStartApp = false
//    private var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        reStartApp = savedInstanceState != null
        super.onCreate(savedInstanceState)
//        ARouter.getInstance().inject(this)
    }

    abstract fun closeAct()

    /**获取前一个页面的参数*/
    abstract fun getBeforeData()

    /**该页面默认的数据（仅仅对于该页面）handle的初始化也默认放这里*/
    abstract fun defaultData()

    /**前一个页面和默认数据之间的处理，以及一些临时变量的赋值处理*/
    abstract fun dealData()

    /**页面的数据显示*/
    abstract fun initView()

    /**点击事件*/
    abstract fun bindClickEvent()

    /**
     * @param layoutRes xml资源
     * @param sort activity基本方法的执行顺序是否按默认的顺序执行
     *             true
     *             false 需要自己重新在onCreate实现
     */
    open fun setContentView(@LayoutRes layoutRes: Int, @NotNull sort: Boolean = false) {
        if (reStartApp) {
            reLoadApp()
            return
        }
        setContentView(layoutRes)
        if (sort) {
            getBeforeData()
            defaultData()
            dealData()
            initView()
            bindClickEvent()
        }
    }

    open fun setContentView(root: View, @NotNull sort: Boolean = false) {
        if (reStartApp) {
            reLoadApp()
            return
        }
        setContentView(root)
        if (sort) {
            getBeforeData()
            defaultData()
            dealData()
            initView()
            bindClickEvent()
        }
    }

    abstract fun reLoadApp()

    /**覆写页面关闭执行的方法*/
    override fun onBackPressedSupport() {
//        super.onBackPressedSupport()
        closeAct()
    }

    override fun finish() {
        //如果有软键盘弹出，需要将软键盘消失
        super.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}