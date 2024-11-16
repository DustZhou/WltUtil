package com.kenning.kcutil.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kenning.kcutil.R;
import com.kenning.kcutil.utils.math.MathUtilsKt;


/**
 * @Description: 左右两侧有加减按钮的EditText
 * @author: create by zyl on 2024/11/16
 */
public class AddAndSubtractEditText extends FrameLayout implements View.OnClickListener {

    private Context context;

    // 累加 图片按钮
    private View ivAdd;
    // 递减 图片按钮
    private View ivSubtract;
    // EditText
    private WltEditText etMain;

    public AddAndSubtractEditText(@NonNull Context context) {
        this(context, null);
    }

    public AddAndSubtractEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddAndSubtractEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        // 初始化布局
        initView();
        // 绑定事件
        bindListener();
    }

    /**
     * 内部方法， 初始化布局
     */
    private void initView() {
        // 挂载布局
        addView(inflate(context, R.layout.widget_add_and_subtract_edittext, null));
        //初始化View
        ivAdd = findViewById(R.id.ivAdd);
        ivSubtract = findViewById(R.id.ivSubtract);
        etMain = findViewById(R.id.etMain);
    }

    /**
     * 内部方法， 绑定控件事件
     */
    private void bindListener() {
        ivAdd.setOnClickListener(this);
        ivSubtract.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // 累加按钮点击
        if (view.getId() == R.id.ivAdd) {
            double qty = MathUtilsKt.toDouble_(etMain.getText().toString());
            etMain.setText(MathUtilsKt.keepPoint(MathUtilsKt.JIA(qty, 1) + "", 20));
            etMain.clearFocus();
        }
        // 递减按钮点击
        if (view.getId() == R.id.ivSubtract) {
            double qty = MathUtilsKt.toDouble_(etMain.getText().toString());
            if (qty <= 1.0) {
                etMain.setText("");
            } else {
                etMain.setText(MathUtilsKt.keepPoint(MathUtilsKt.JIAN(qty, 1) + "", 20));
            }
            etMain.clearFocus();
        }
    }

    /**
     * 公开方法， 获取内部ivAdd对象
     */
    public View getIvAdd() {
        return ivAdd;
    }

    /**
     * 公开方法， 获取内部ivSubtract对象
     */
    public View getIvSubtract() {
        return ivSubtract;
    }

    /**
     * 公开方法， 获取内部etMain对象
     */
    public WltEditText getEtMain() {
        return etMain;
    }

    /**
     * 公开方法， 给EditText设置内容
     *
     * @param text
     */
    public void setText(String text) {
        if (text != null) {
            etMain.setText(text);
        }
    }

    /**
     * 公开方法， 获取当前EditText的值
     */
    public String getText() {
        return etMain.getText().toString();
    }

    /**
     * 公开方法，设置编辑框是否可编辑
     */
    public void setEditable(boolean value) {
        if (value) {
            ivAdd.setVisibility(VISIBLE);
            ivSubtract.setVisibility(VISIBLE);
            etMain.setFocusableInTouchMode(true);
        } else {
            ivAdd.setVisibility(GONE);
            ivSubtract.setVisibility(GONE);
            etMain.setFocusableInTouchMode(false);
        }
    }

}
