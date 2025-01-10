package com.kenning.kcutil

import android.os.Bundle
import android.webkit.WebSettings
import com.kenning.base.BaseActivity
import kotlinx.android.synthetic.main.activity_index_privacypolicy.*


/**
 *  隐私政策
 *  @author : zyl
 *  modify at 2020-04-21 13:54
 */
class PrivacyPolicyActivity : BaseActivity() {
    /** url 和 name 必须要传 */
    private var url = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        getBeforeData()
        webview!!.loadUrl(url)
    }

    override fun closeAct() {
        finish()
    }

    /** 初始化view视图*/
    override fun initView() {
        setContentView(R.layout.activity_index_privacypolicy)
        val webSettings = webview!!.settings
        webSettings.useWideViewPort = false//设置webview推荐使用的窗口
        webSettings.loadWithOverviewMode = true//设置webview加载的页面的模式
        webSettings.displayZoomControls = false//隐藏webview缩放按钮
        webSettings.allowFileAccess = false // 允许访问文件
        webSettings.builtInZoomControls = false // 设置显示缩放按钮
        webSettings.setSupportZoom(true) // 支持缩放
        webSettings.textZoom = 120
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    }
    /**获取前一个页面的参数*/
    override fun getBeforeData() {
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.getString("url", "") != "") {
                url = bundle.getString("url", "")
                tvBack.text = bundle.getString("name", "")
            }
        }
    }
    /**该页面默认的数据（仅仅对于该页面）handle的初始化也默认放这里*/
    override fun defaultData() =Unit
    override fun dealData() {
        TODO("Not yet implemented")
    }

    /**点击事件*/
    override fun bindClickEvent()=Unit
    override fun reLoadApp() {
        TODO("Not yet implemented")
    }

    /** 确保注销配置能够被释放 */
    override fun onDestroy() {
        if (this.webview != null) {
            webview!!.destroy()
        }
        super.onDestroy()
    }
}