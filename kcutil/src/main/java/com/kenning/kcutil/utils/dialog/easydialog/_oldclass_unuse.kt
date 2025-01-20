package com.kenning.kcutil.utils.dialog.easydialog

/**
 *Description :
 *@author : KenningChen
 *Date : 2022/10/17
 */
class _oldclass_unuse {
    //package com.kenning.kcutil.utils.dialog.easydialog
    //
    //import android.app.Activity
    //import android.app.Dialog
    //import android.content.Context
    //import android.os.Bundle
    //import android.text.Spanned
    //import android.text.method.LinkMovementMethod
    //import android.view.Gravity
    //import android.view.LayoutInflater
    //import android.view.View
    //import android.view.ViewGroup
    //import android.widget.LinearLayout
    //import android.widget.TextView
    //import androidx.annotation.ColorRes
    //import androidx.core.content.res.ResourcesCompat
    //import androidx.recyclerview.widget.RecyclerView
    //import com.kenning.kcutil.R
    //import com.kenning.kcutil.databinding.EasydialogBinding
    //import com.kenning.kcutil.utils.math.toInt_
    //import com.kenning.kcutil.utils.other.ScreenUtil
    //import com.kenning.kcutil.utils.other.getColorResource
    //import com.kenning.kcutil.utils.recyclerviewextend.BaseRecyclerViewHolder
    //import com.kenning.kcutil.utils.recyclerviewextend.RecycleViewDivider
    //import com.kenning.kcutil.widget.SwitchImageView
    //import com.kenning.kcutil.widget.basicview.BackGroundTextView
    //
    ///**
    // * Description :
    // *
    // * @author : KenningChen
    // * Date : 2021/10/15
    // */
    //class BaseDialog : Dialog {
    //    private var mContext: Context? = null
    //
    //    private var title = ""
    //
    //    private var adapter: RecyclerView.Adapter<*>? = null
    //
    //
    //    /**提示内容*/
    //    private var msg = ""
    //    private var spanned : Spanned?=null
    //
    //    private var Prompt = false
    //
    //    private var tiShiKey = ""
    //
    //    private var isCheckNoTishi = "false"
    //
    //    private var cancel = false
    //
    //    private var hasButtons = true
    //
    //    private var hasTitle = true
    //
    //    private var strList = arrayOf<String?>()
    //
    //    private var itemClick:((Int)->Unit)?=null
    //
    //    /**图片显示 默认不显示*/
    //    private var showPicture = false
    //
    //    private var hideTitleLine = false
    //
    //    private var hideAdapterLine = false
    //
    //    private var keyCancelAble = true
    //
    //    private var mGravity = Gravity.CENTER
    //
    //    constructor(
    //        mContext: Context,
    //        title: String,
    //        strList: Array<String?>,
    //        gravity:Int,
    //        cancel:Boolean,
    //        keyCancelAble:Boolean,
    //        hasButtons:Boolean,
    //        hasTitle:Boolean,
    //        hideTitleLine:Boolean,
    //        hideAdapterLine:Boolean,
    //        itemClick:(Int)->Unit
    //    ) : super(mContext, R.style.commom_dialog) {
    //        this.title = title
    //        this.strList = strList
    //        this.mContext = mContext
    //        this.cancel = cancel
    //        this.keyCancelAble = keyCancelAble
    //        this.hasButtons = hasButtons
    //        this.itemClick = itemClick
    //        this.hasTitle = hasTitle
    //        this.hideAdapterLine = hideAdapterLine
    //        this.hideTitleLine = hideTitleLine
    //        mGravity = gravity
    //    }
    //
    //    constructor(
    //        mContext: Context,
    //        title: String,
    //        adapter: RecyclerView.Adapter<*>,
    //        cancel:Boolean,
    //        keyCancelAble:Boolean,
    //        hasButtons:Boolean,
    //        hasTitle:Boolean,
    //        hideTitleLine:Boolean,
    //        hideAdapterLine:Boolean
    //    ) : super(mContext, R.style.commom_dialog) {
    //        this.title = title
    //        this.adapter = adapter
    //        this.mContext = mContext
    //        this.cancel = cancel
    //        this.keyCancelAble = keyCancelAble
    //        this.hasButtons = hasButtons
    //        this.hasTitle = hasTitle
    //        this.hideAdapterLine = hideAdapterLine
    //        this.hideTitleLine = hideTitleLine
    //    }
    //
    //    constructor(
    //        mContext: Context,
    //        title: String,
    //        msg: String,
    //        gravity:Int,
    //        cancel:Boolean,
    //        keyCancelAble:Boolean,
    //        hasButtons:Boolean,
    //        hasTitle:Boolean,
    //        hideTitleLine:Boolean,
    //        hideAdapterLine:Boolean
    //    ) : super(mContext, R.style.commom_dialog) {
    //        this.title = title
    //        this.msg = msg
    //        this.mContext = mContext
    //        this.cancel = cancel
    //        this.keyCancelAble = keyCancelAble
    //        this.hasButtons = hasButtons
    //        this.hasTitle = hasTitle
    //        this.hideAdapterLine = hideAdapterLine
    //        this.hideTitleLine = hideTitleLine
    //        mGravity = gravity
    //    }
    //
    //    constructor(
    //        mContext: Context,
    //        title: String,
    //        spanned: Spanned,
    //        gravity:Int,
    //        cancel:Boolean,
    //        keyCancelAble:Boolean,
    //        hasButtons:Boolean,
    //        hasTitle:Boolean,
    //        hideTitleLine:Boolean,
    //        hideAdapterLine:Boolean,
    //        showPicture:Boolean = false
    //    ) : super(mContext, R.style.commom_dialog) {
    //        this.title = title
    //        this.spanned = spanned
    //        this.mContext = mContext
    //        this.cancel = cancel
    //        this.keyCancelAble = keyCancelAble
    //        this.hasButtons = hasButtons
    //        this.showPicture = showPicture
    //        this.hasTitle = hasTitle
    //        this.hideAdapterLine = hideAdapterLine
    //        this.hideTitleLine = hideTitleLine
    //        mGravity = gravity
    //    }
    //
    //
    //
    //    private var title_textcolor = R.color.color_333333
    //    private var title_backgroundcolor = R.color.white
    //
    //    private var promptEventIndex = -1
    //
    //    lateinit var binding: EasydialogBinding
    //
    //    override fun onCreate(savedInstanceState: Bundle?) {
    //        super.onCreate(savedInstanceState)
    //        binding=EasydialogBinding.inflate(layoutInflater)
    //        setContentView(binding.root)
    //        setCanceledOnTouchOutside(cancel)
    //
    //        if (!hasTitle){
    //            binding.topView.visibility = View.VISIBLE
    //            binding.titleline.visibility = View.GONE
    //            binding.layouttitle.visibility = View.GONE
    //        }
    //
    //        if (hideTitleLine){
    //            binding.titleline.visibility = View.GONE
    //        }
    //
    //        binding.tvDialogName.setTextColor(getColorResource(title_textcolor))
    //        binding.tvDialogName.setNormalBackgroundColor(getColorResource(title_backgroundcolor))
    //        binding.tvDialogName.text = title
    //        if (showPicture){
    //            binding.picture.visibility = View.VISIBLE
    //        }
    //
    //        val params = window!!.attributes
    //        params.width = tools.dialogWidth
    //        window!!.attributes = params
    //        //定义dialog最大支持高度
    //        val maxHeight = ((mContext as Activity).windowManager.defaultDisplay.height * 0.6).toInt()
    //        if (adapter != null) {
    //            (findViewById<View>(R.id.mRecyclerview) as RecyclerView).adapter = adapter
    //            if (!hideAdapterLine)
    //            (findViewById<View>(R.id.mRecyclerview) as RecyclerView).addItemDecoration(
    //                RecycleViewDivider(
    //                    context,
    //                    tools.recycleViewDividerHeight,
    //                    R.color.color_EAEEEF,
    //                    tools.recycleViewDividerLeft,
    //                    tools.recycleViewDividerRight
    //                )
    //            )
    //        } else if (strList.isNotEmpty()){
    //            adapter = StringListAdapter(strList)
    //            (findViewById<View>(R.id.mRecyclerview) as RecyclerView).adapter = adapter
    //            (findViewById<View>(R.id.mRecyclerview) as RecyclerView).addItemDecoration(
    //                RecycleViewDivider(
    //                    context,
    //                    tools.recycleViewDividerHeight,
    //                    R.color.color_EAEEEF,
    //                    tools.recycleViewDividerLeft,
    //                    tools.recycleViewDividerRight
    //                )
    //            )
    //        } else {
    //            binding.titleline.visibility = View.GONE
    //            adapter = StringAdapter(msg)
    //            (findViewById<View>(R.id.mRecyclerview) as RecyclerView).adapter = adapter
    //        }
    //
    //        findViewById<View>(R.id.main).addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
    //            val height = v.height
    //            if (height > maxHeight) {
    //                params.height = maxHeight
    //                window!!.attributes = params
    //            }
    //        }
    //    }
    //
    //
    //    fun setTitleColors(
    //        @ColorRes textcolor: Int = R.color.color_333333,
    //        @ColorRes backgroundcolor: Int = R.color.white
    //    ): BaseDialog {
    //        title_backgroundcolor = backgroundcolor
    //        title_textcolor = textcolor
    //        return this
    //    }
    //
    //    private var modes: Array<out ButtonMode>? = null
    //
    //    /**设置底部按钮*/
    //    fun setButtonMode(vararg modes: ButtonMode): BaseDialog {
    ////        this.modes = modes
    ////        return this
    //        return setButtonMode(index = -1,prompt = false, modes = modes)
    //    }
    //
    //    /**是否带提示
    //     * @param effectButton 使 `下次不再提示` 等check事件生效的控件
    //     * */
    //    fun setButtonMode(index:Int,prompt: Boolean, vararg modes: ButtonMode): BaseDialog {
    //        if (adapter == null) {
    //            this.Prompt = prompt
    //            tiShiKey = (mContext as Activity).localClassName
    //        }
    //        promptEventIndex = index
    //        this.modes = modes
    //        return this
    //    }
    //
    //    /**绘制底部功能按钮*/
    //    private fun setBottomLayout(modes: Array<out ButtonMode>? = null) {
    //        binding.layoutButton.removeAllViews()
    //        if (modes != null && modes.isNotEmpty()) {
    //            var index = 0
    //            for (item in modes) {
    //                var button: BackGroundTextView = LayoutInflater.from(mContext)
    //                    .inflate(R.layout.item_button, null) as BackGroundTextView
    //                button.tag = index
    //                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
    //                    0, tools.buttonHeight, 1f
    //                )
    //                if (index == 0 && modes.size - 1 > index) {
    //                    button.setEachCornerRadius(
    //                        0,
    //                        0,
    //                        tools.radius,
    //                        0
    //                    )
    //                } else if (index == 0 && modes.size - 1 == index) {
    //                    button.setEachCornerRadius(
    //                        0,
    //                        0,
    //                        tools.radius,
    //                        tools.radius
    //                    )
    //                } else if (index != 0 && modes.size - 1 == index) {
    //                    button.setEachCornerRadius(
    //                        0,
    //                        0,
    //                        0,
    //                        tools.radius
    //                    )
    //                }
    //
    //                if (index > 0) {
    //                    var textview = TextView(mContext)
    //                    val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
    //                        ScreenUtil.dip2px(1f),
    //                        LinearLayout.LayoutParams.MATCH_PARENT
    //                    )
    //                    textview.setBackgroundColor(getColorResource(R.color.color_EAEEEF))
    //                    textview.layoutParams = params
    //                    binding.layoutButton.addView(textview)
    //                }
    //
    //                button.text = item.text
    //                button.setOnClickListener{
    //                    if (Prompt && promptEventIndex == it.tag.toInt_()) {
    //                        tools.setPreferences(context, "EasyDialogTiShi", tiShiKey, isCheckNoTishi)
    //                    }
    //                    item.click?.invoke(this)?:dismiss()
    //                }
    //                button.setTextColor(getColorResource(item.textcolor))
    //                button.setNormalBackgroundColor(getColorResource(item.backgroundcolor))
    //                button.layoutParams = params
    //
    //                binding.layoutButton.addView(button)
    //
    //                index++
    //            }
    //        } else {
    //            var button: BackGroundTextView =
    //                LayoutInflater.from(mContext)
    //                    .inflate(R.layout.item_button, null) as BackGroundTextView
    //            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
    //                0, tools.buttonHeight, 1f
    //            )
    //            button.setEachCornerRadius(
    //                0,
    //                0,
    //                tools.radius,
    //                tools.radius
    //            )
    //
    //            button.text = getStringResource(R.string.我知道了)
    //
    //            button.setTextColor(getColorResource(R.color.color_333333))
    //            button.setNormalBackgroundColor(getColorResource(R.color.white))
    //            button.layoutParams = params
    //
    //            button.setOnClickListener {
    //                if (Prompt) {
    //                    tools.setPreferences(context, "EasyDialogTiShi", tiShiKey, "$isCheckNoTishi")
    //                }
    //                dismiss()
    //            }
    //            binding.layoutButton.addView(button)
    //        }
    //    }
    //
    //    override fun show() {
    //        super.show()
    //        window!!.setBackgroundDrawableResource(android.R.color.transparent)
    //        if (hasButtons) {
    //            if (modes == null) {
    //                setBottomLayout()
    //            } else {
    //                setBottomLayout(modes)
    //            }
    //        }else{
    //            binding.line.visibility = View.GONE
    //            binding.layoutButton.visibility = View.GONE
    //            binding.bottomview.visibility = View.VISIBLE
    //        }
    //    }
    //
    //    var state = true
    //
    //    inner class StringAdapter(var string: String,var spanned: Spanned?=null) : RecyclerView.Adapter<BaseRecyclerViewHolder>() {
    //        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
    //            return if (Prompt) {
    //                BaseRecyclerViewHolder.getHolder(
    //                    parent.context,
    //                    parent,
    //                    R.layout.diaog_msg_withprompt
    //                )
    //            } else {
    //                BaseRecyclerViewHolder.getHolder(parent.context, parent, R.layout.diaog_msg)
    //            }
    //        }
    //
    //        override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
    //            if (spanned!=null){
    //                holder.setText(R.id.tvMsg, spanned)
    //                (holder.getView<TextView>(R.id.tvMsg)).apply {
    //                    movementMethod = LinkMovementMethod.getInstance()
    //                    highlightColor = ResourcesCompat.getColor(mContext!!.resources,R.color
    //                        .transparent,null)
    //                }
    //            }else {
    //                holder.setText(R.id.tvMsg, string)
    //            }
    //            val layoutParams = holder.getView<TextView>(R.id.tvMsg).layoutParams
    //            if(mGravity == Gravity.LEFT){
    //                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
    //                holder.getView<TextView>(R.id.tvMsg).layoutParams = layoutParams
    //            }else if (mGravity == Gravity.RIGHT){
    //                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
    //                holder.getView<TextView>(R.id.tvMsg).layoutParams = layoutParams
    //                holder.getView<TextView>(R.id.tvMsg).gravity = Gravity.RIGHT
    //            }
    //            if (Prompt) {
    //                holder.getView<SwitchImageView>(R.id.switchView).setOnSwitchListener {
    //                    isCheckNoTishi = "$it"
    //                }
    //                holder.getView<View>(R.id.layoutTishi).setOnClickListener {
    //                    holder.getView<SwitchImageView>(R.id.switchView).performClick()
    //                }
    //            }
    //        }
    //
    //        override fun getItemCount(): Int {
    //            return 1
    //        }
    //
    //    }
    //
    //    inner class StringListAdapter(var list: Array<String?>): RecyclerView
    //    .Adapter<BaseRecyclerViewHolder>(){
    //        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
    //            return BaseRecyclerViewHolder.getHolder(parent.context, parent, R.layout.diaog_strlist)
    //        }
    //
    //        override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
    //            holder.setText(R.id.tvMsg, list[position])
    //            holder.getView<TextView>(R.id.tvMsg).gravity = mGravity
    //            holder.setOnclickListioner(R.id.tvMsg){
    //                itemClick?.invoke(holder.adapterPosition)
    //                dismiss()
    //            }
    //        }
    //
    //        override fun getItemCount(): Int {
    //            return list.size
    //        }
    //
    //    }
    //
    //    var tools = DialogTools()
    //
    //    override fun onBackPressed() {
    //        if(keyCancelAble)
    //            super.onBackPressed()
    //    }
    //}
}