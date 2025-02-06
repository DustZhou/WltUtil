package com.kenning.kcutil.utils.dialog.easydialog

import android.app.Activity
import android.content.Context
import android.text.Spanned
import android.view.Gravity
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import com.kenning.kcutil.R
import com.kenning.kcutil.utils.other.getStringResource

/**
 *Description :
 *@author : KenningChen
 *Date : 2021/11/5
 */
open class EasyDialog(private var context: Context) {

    private var title :Any? = null

    private var titleTextColor = R.color.color_333333

    private var titleBgColor = R.color.white

    private var adapter: RecyclerView.Adapter<*>? = null

    private var cancelable = false

    /**提示内容*/
    private var msg = ""
    private var spanned: Spanned? = null

    private var Prompt = false

    private var tiShiKey = ""

    private var isCheckNoTishi = "false"

    private var modes: Array<out ButtonMode>? = null

    private var mBaseDialog: BaseDialog? = null

    private var promptEventIndex = -1

    private var hasButtons = true

    private var hasTitle = true

    private var hideTitleLine = false

    private var hideAdapterLine = false

    private var strList = arrayOf<String?>()

    private var itemClick: ((Int) -> Unit)? = null

    private var showPicture = false

    private var keyCancelAble = true

    private var tools = DialogTools()

    /**记录使用次数,如果大于0,则需要初始化参数*/
    private var count = 0

    /**是否已经执行过初始化*/
    private var mIsReset = false

    private var mGravity = Gravity.LEFT or Gravity.CENTER

    fun getBaseDialog(): BaseDialog? {
        return mBaseDialog
    }

    fun setDialogReact(
        @IntRange(from = 75,to= 90) widthPer:Int = 80,
        @IntRange(from = 60,to= 100) heightPer:Int = 60
    ): EasyDialog{
        tools.dialogWidthPre = widthPer
        tools.dialogMaxHeightPre = heightPer
        return this
    }

    fun setResultType(clazz: Class<*>): EasyDialog{
        tools.resultType = clazz
        return this
    }

    fun setTitle(title: String): EasyDialog {
        reSetDefault()
        this.title = title
        return this
    }

    fun setTitle(title: Spanned): EasyDialog {
        reSetDefault()
        this.title = title
        return this
    }

    fun hideTitleLine(): EasyDialog {
        reSetDefault()
        hideTitleLine = true
        return this
    }

    fun hideAdapterLine(): EasyDialog {
        reSetDefault()
        hideAdapterLine = true
        return this
    }

    fun setTitleColors(
        @ColorRes textcolor: Int = R.color.color_333333,
        @ColorRes backgroundcolor: Int = R.color.white
    ): EasyDialog {
        reSetDefault()
        titleTextColor = textcolor
        titleBgColor = backgroundcolor
        return this
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>): EasyDialog {
        reSetDefault()
        this.adapter = adapter
        return this
    }

    fun setArray(array: Array<String?>, itemClick: (Int) -> Unit): EasyDialog {
        reSetDefault()
        this.strList = array
        this.itemClick = itemClick
        return this
    }

    fun setArray(array: Array<String?>,gravity:Int, itemClick: (Int) -> Unit): EasyDialog {
        reSetDefault()
        mGravity = gravity
        this.strList = array
        this.itemClick = itemClick
        return this
    }

    @JvmOverloads
    fun setContentMsg(msg: String?,gravity:Int=/*Gravity.LEFT or*/ Gravity.CENTER): EasyDialog {
        reSetDefault()
        mGravity = gravity
        this.msg = msg ?: ""
        adapter = null
        return this
    }

    @JvmOverloads
    fun setContentMsg(msg: Spanned,gravity:Int=/*Gravity.LEFT or */Gravity.CENTER): EasyDialog {
        reSetDefault()
        mGravity = gravity
        this.spanned = msg
        adapter = null
        return this
    }

    fun setButtonMode(vararg modes: ButtonMode): EasyDialog {
        reSetDefault()
        this.modes = modes
        return this
    }

    /**设置底部操作按钮的排列方式
     *
     * 0 横向
     *
     * 1 纵向
     * */
    fun setBottomOption(@IntRange(from=0,to = 1) option:Int): EasyDialog{
        tools.bottomButtonOption = option
        return this
    }

    /**设置不再提示*/
    fun withPrompt(index: Int = 0, extendKey: String = ""): EasyDialog {
        reSetDefault()
        promptEventIndex = index
        if (modes == null)
            promptEventIndex = -1
        this.Prompt = true
        tiShiKey = "${(context as Activity).localClassName}$extendKey"
        tools.promptTag = tiShiKey
        return this
    }

    fun withPrompt(index: Int = 0,promptMsg:String, extendKey: String = ""): EasyDialog {
        reSetDefault()
        promptEventIndex = index
        if (modes == null)
            promptEventIndex = -1
        this.Prompt = true
        tiShiKey = "${(context as Activity).localClassName}$extendKey"
        tools.promptTag = tiShiKey
        tools.promptMsg = promptMsg
        return this
    }

    /**设置是否需要底部按钮*/
    fun needNoNButtons(non: Boolean): EasyDialog {
        reSetDefault()
        hasButtons = !non
        return this
    }

    fun needNonTitle(non: Boolean): EasyDialog {
        reSetDefault()
        hasTitle = !non
        return this
    }

    fun cancelAble(cancel: Boolean): EasyDialog {
        reSetDefault()
        cancelable = cancel
        return this
    }

    fun keyCancelAble(cancel: Boolean): EasyDialog {
        reSetDefault()
        keyCancelAble = cancel
        return this
    }

    fun showPicture(pic:Int): EasyDialog {
        reSetDefault()
        tools.errorTitlePic = pic
        showPicture = true
        tools.showPicture = true
        return this
    }

    private fun reSetDefault() {
        if (count > 0 && !mIsReset) {
            title = ""
            titleTextColor = R.color.color_333333
            titleBgColor = R.color.white
            adapter = null
            cancelable = false
            msg = ""
            spanned = null
            Prompt = false
            tiShiKey = ""
            isCheckNoTishi = "false"
            modes = null
            mBaseDialog = null
            promptEventIndex = -1
            hasButtons = true
            hasTitle = true
            hideTitleLine = false
            hideAdapterLine = false
            strList = arrayOf<String?>()
            itemClick = null
            mGravity = Gravity.CENTER

            tools.showPicture = false
            tools.bottomButtonOption = 0
        }
        mIsReset = true
    }

    suspend fun buildAsSuspend():Any?{
        tools.supportSuspend = true
        count++
        mIsReset = false
        if (title == null) {
            this.title = getStringResource(R.string.温馨提示)
        }

       return if (Prompt) {
            isCheckNoTishi = tools.getPreferences(
                c = context,
                getType = "EasyDialogTiShi",
                getKey = tiShiKey,
                defValue = "false"
            )
            if (isCheckNoTishi == "true") {
                //直接执行
                modes?.let {
                    it[promptEventIndex].click?.invoke(null)
                }
            } else {
                //跳转提示窗口
                if (modes == null) {
                    mBaseDialog = BaseDialog(
                        context, title!!, msg,mGravity, cancelable,keyCancelAble, hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine
                    )
                        .setTitleColors(
                            titleTextColor,
                            titleBgColor
                        ).setButtonMode(index = -1, prompt = true)
                        .setDialogTool(tools)
                    mBaseDialog?.showAsSuspend()
                } else {
                    if (spanned != null) {
                        mBaseDialog =
                            BaseDialog(
                                context, title!!, spanned!!,mGravity, cancelable,keyCancelAble,
                                hasButtons,
                                hasTitle, hideTitleLine, hideAdapterLine, showPicture
                            ).setButtonMode(
                                index = promptEventIndex,
                                prompt = true,
                                modes = modes!!
                            ).setTitleColors(titleTextColor, titleBgColor)
                                .setDialogTool(tools)
                        mBaseDialog?.showAsSuspend()
                    } else {
                        mBaseDialog =
                            BaseDialog(
                                context, title!!, msg,mGravity, cancelable,keyCancelAble,
                                hasButtons, hasTitle,
                                hideTitleLine, hideAdapterLine
                            )
                                .setButtonMode(
                                    index = promptEventIndex,
                                    prompt = true,
                                    modes = modes!!
                                ).setTitleColors(titleTextColor, titleBgColor)
                                .setDialogTool(tools)
                        mBaseDialog?.showAsSuspend()
                    }
                }
            }
        } else {
            //跳转提示窗口
            if (modes == null) {
                if (adapter != null) {
                    mBaseDialog = BaseDialog(
                        context, title!!, adapter!!, cancelable,keyCancelAble, hasButtons,
                        hasTitle, hideTitleLine, hideAdapterLine
                    ).setTitleColors(
                        titleTextColor,
                        titleBgColor
                    ).setButtonMode()
                        .setDialogTool(tools)
                    mBaseDialog?.showAsSuspend()
                } else if (itemClick != null && strList.isNotEmpty()) {
                    mBaseDialog = BaseDialog(
                        context,
                        title!!,
                        strList,
                        mGravity,
                        cancelable,
                        keyCancelAble,
                        hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine,
                        itemClick!!
                    ).setTitleColors(titleTextColor, titleBgColor).setButtonMode()
                        .setDialogTool(tools)
                    mBaseDialog?.showAsSuspend()
                } else if (spanned != null) {
                    mBaseDialog = BaseDialog(
                        context, title!!, spanned!!,mGravity, cancelable,keyCancelAble, hasButtons,
                        hasTitle, hideTitleLine, hideAdapterLine, showPicture
                    ).setTitleColors(
                        titleTextColor,
                        titleBgColor
                    ).setButtonMode()
                        .setDialogTool(tools)
                    mBaseDialog?.showAsSuspend()
                } else {
                    mBaseDialog = BaseDialog(
                        context, title!!, msg,mGravity, cancelable,keyCancelAble, hasButtons,
                        hasTitle, hideTitleLine, hideAdapterLine
                    ).setTitleColors(
                        titleTextColor,
                        titleBgColor
                    ).setButtonMode()
                        .setDialogTool(tools)
                    mBaseDialog?.showAsSuspend()
                }
            } else {
                if (adapter != null) {
                    mBaseDialog = BaseDialog(
                        context,
                        title!!,
                        adapter!!,
                        cancelable,
                        keyCancelAble,
                        hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine
                    ).setButtonMode(modes = modes!!).setTitleColors(titleTextColor, titleBgColor)
                        .setDialogTool(tools)
                    mBaseDialog?.showAsSuspend()
                } else if (itemClick != null && strList.isNotEmpty()) {
                    mBaseDialog = BaseDialog(
                        context,
                        title!!,
                        strList,
                        mGravity,
                        cancelable,
                        keyCancelAble,
                        hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine,
                        itemClick!!
                    ).setButtonMode(modes = modes!!).setTitleColors(titleTextColor, titleBgColor)
                        .setDialogTool(tools)
                    mBaseDialog?.showAsSuspend()
                } else if (spanned != null) {
                    mBaseDialog = BaseDialog(
                        context,
                        title!!,
                        spanned!!,
                        mGravity,
                        cancelable,
                        keyCancelAble,
                        hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine,
                        showPicture
                    ).setButtonMode(modes = modes!!).setTitleColors(titleTextColor, titleBgColor)
                        .setDialogTool(tools)
                    mBaseDialog?.showAsSuspend()
                } else {
                    mBaseDialog = BaseDialog(
                        context,
                        title!!,
                        msg,
                        mGravity,
                        cancelable,
                        keyCancelAble,
                        hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine
                    ).setButtonMode(modes = modes!!).setTitleColors(titleTextColor, titleBgColor)
                        .setDialogTool(tools)
                    mBaseDialog?.showAsSuspend()
                }
            }
        }
    }

    fun build() {
        count++
        mIsReset = false
        if (title == null) {
            this.title = getStringResource(R.string.温馨提示)
        }

        if (Prompt) {
            isCheckNoTishi = tools.getPreferences(
                c = context,
                getType = "EasyDialogTiShi",
                getKey = tiShiKey,
                defValue = "false"
            )
            if (isCheckNoTishi == "true") {
                //直接执行
                modes?.let {
                    it[promptEventIndex].click?.invoke(null)
                }
            } else {
                //跳转提示窗口
                if (modes == null) {
                    mBaseDialog = BaseDialog(
                        context, title!!, msg,mGravity, cancelable,keyCancelAble, hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine
                    )
                        .setTitleColors(
                            titleTextColor,
                            titleBgColor
                        ).setButtonMode(index = -1, prompt = true)
                        .setDialogTool(tools)
                    mBaseDialog?.show()
                } else {
                    if (spanned != null) {
                        mBaseDialog =
                            BaseDialog(
                                context, title!!, spanned!!,mGravity, cancelable,keyCancelAble,
                                hasButtons,
                                hasTitle, hideTitleLine, hideAdapterLine, showPicture
                            ).setButtonMode(
                                index = promptEventIndex,
                                prompt = true,
                                modes = modes!!
                            ).setTitleColors(titleTextColor, titleBgColor)
                                .setDialogTool(tools)
                        mBaseDialog?.show()
                    } else {
                        mBaseDialog =
                            BaseDialog(
                                context, title!!, msg,mGravity, cancelable,keyCancelAble,
                                hasButtons, hasTitle,
                                hideTitleLine, hideAdapterLine
                            )
                                .setButtonMode(
                                    index = promptEventIndex,
                                    prompt = true,
                                    modes = modes!!
                                ).setTitleColors(titleTextColor, titleBgColor)
                                .setDialogTool(tools)
                        mBaseDialog?.show()
                    }
                }
            }
        } else {
            //跳转提示窗口
            if (modes == null) {
                if (adapter != null) {
                    mBaseDialog = BaseDialog(
                        context, title!!, adapter!!, cancelable,keyCancelAble, hasButtons,
                        hasTitle, hideTitleLine, hideAdapterLine
                    ).setTitleColors(
                        titleTextColor,
                        titleBgColor
                    ).setButtonMode()
                        .setDialogTool(tools)
                    mBaseDialog?.show()
                } else if (itemClick != null && strList.isNotEmpty()) {
                    mBaseDialog = BaseDialog(
                        context,
                        title!!,
                        strList,
                        mGravity,
                        cancelable,
                        keyCancelAble,
                        hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine,
                        itemClick!!
                    ).setTitleColors(titleTextColor, titleBgColor).setButtonMode()
                        .setDialogTool(tools)
                    mBaseDialog?.show()
                } else if (spanned != null) {
                    mBaseDialog = BaseDialog(
                        context, title!!, spanned!!,mGravity, cancelable,keyCancelAble, hasButtons,
                        hasTitle, hideTitleLine, hideAdapterLine, showPicture
                    ).setTitleColors(
                        titleTextColor,
                        titleBgColor
                    ).setButtonMode()
                        .setDialogTool(tools)
                    mBaseDialog?.show()
                } else {
                    mBaseDialog = BaseDialog(
                        context, title!!, msg,mGravity, cancelable,keyCancelAble, hasButtons,
                        hasTitle, hideTitleLine, hideAdapterLine
                    ).setTitleColors(
                        titleTextColor,
                        titleBgColor
                    ).setButtonMode()
                        .setDialogTool(tools)
                    mBaseDialog?.show()
                }
            } else {
                if (adapter != null) {
                    mBaseDialog = BaseDialog(
                        context,
                        title!!,
                        adapter!!,
                        cancelable,
                        keyCancelAble,
                        hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine
                    ).setButtonMode(modes = modes!!).setTitleColors(titleTextColor, titleBgColor)
                        .setDialogTool(tools)
                    mBaseDialog?.show()
                } else if (itemClick != null && strList.isNotEmpty()) {
                    mBaseDialog = BaseDialog(
                        context,
                        title!!,
                        strList,
                        mGravity,
                        cancelable,
                        keyCancelAble,
                        hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine,
                        itemClick!!
                    ).setButtonMode(modes = modes!!).setTitleColors(titleTextColor, titleBgColor)
                        .setDialogTool(tools)
                    mBaseDialog?.show()
                } else if (spanned != null) {
                    mBaseDialog = BaseDialog(
                        context,
                        title!!,
                        spanned!!,
                        mGravity,
                        cancelable,
                        keyCancelAble,
                        hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine,
                        showPicture
                    ).setButtonMode(modes = modes!!).setTitleColors(titleTextColor, titleBgColor)
                        .setDialogTool(tools)
                    mBaseDialog?.show()
                } else {
                    mBaseDialog = BaseDialog(
                        context,
                        title!!,
                        msg,
                        mGravity,
                        cancelable,
                        keyCancelAble,
                        hasButtons,
                        hasTitle,
                        hideTitleLine, hideAdapterLine
                    ).setButtonMode(modes = modes!!).setTitleColors(titleTextColor, titleBgColor)
                        .setDialogTool(tools)
                    mBaseDialog?.show()
                }
            }
        }
    }
}