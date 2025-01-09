package com.zyyoona7.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.kenning.kcutil.R;
import com.kenning.kcutil.databinding.DialogAdaptionBinding;
import com.kenning.kcutil.utils.other.ScreenUtil;


public class AdaptionDialog extends PopupWindow {

    private Context context;

    private View baseView;
    private String msg = "";
    private View window;
    private View triangle;

    private LinearLayout windowLayout;

    private int baseViewLocation[];

    private DialogAdaptionBinding windowBinding;
    private int txtMsgHeight = 0;
    /**
     * @param context
     * @param baseView
     */
    public AdaptionDialog(Context context, View baseView, String msg) {
        super(context);
        this.context = context;
        this.baseView = baseView;
        this.msg = msg;

        setBaseViewLocation();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_window, null);
        ((TextView) view.findViewById(R.id.txt_msg)).setText(msg);
        txtMsgHeight =  ((TextView) view.findViewById(R.id.txt_msg)).getMeasuredHeight();
        init(view);
    }

    /**
     * @param context
     * @param baseView
     * @param windowView
     */
    public AdaptionDialog(Context context, View baseView, View windowView) {
        super(context);
        this.context = context;
        this.baseView = baseView;

        setBaseViewLocation();
        init(windowView);
    }

    private void init(View windowView) {
        windowBinding = DialogAdaptionBinding.inflate(((Activity) context).getLayoutInflater());
//        windowBinding.setPresenter(new Presenter());
        window = windowView;

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);

        // 初始化三角形
        triangle = new View(context);
        triangle.setBackground(context.getResources().getDrawable(R.drawable.triangle));
        LinearLayout.LayoutParams triangleParams = new LinearLayout.LayoutParams(dip2px(10), dip2px(10));
        // 可以动态去修改三角的左右边距，已达到想要的效果，默认15dp
        triangleParams.setMargins(dip2px(15), 0, 0, 0);
        triangle.setLayoutParams(triangleParams);

        // 初始化用于放置悬浮框的layout，包含三角及提示框两个元素
        windowLayout = new LinearLayout(context);
        windowLayout.setOrientation(LinearLayout.VERTICAL);
        windowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        windowLayout.setPadding(dip2px(10), dip2px(5), dip2px(10), dip2px(5));
        windowLayout.addView(triangle);
        windowLayout.addView(window);
        windowBinding.rootView.addView(windowLayout);

        setContentView(windowBinding.getRoot());

        // 使用 AT_MOST 而不是 UNSPECIFIED
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ScreenUtil.INSTANCE.getScreenWidth(), View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(ScreenUtil.INSTANCE.getScreenHeight(), View.MeasureSpec.AT_MOST);
        getContentView().measure(widthMeasureSpec, heightMeasureSpec);
        windowBinding.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void setBaseViewLocation() {
        baseViewLocation = new int[2];
        baseView.getLocationOnScreen(baseViewLocation);
    }

    public void show() {
        baseView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        setBaseViewLocation();
        if (isPlaced())
            showAtBottom();
        else {
            showAtTop();
        }
    }

    /**
     * 判断下方是否有充足空间显示悬浮框
     *
     * @return
     */
    private boolean isPlaced() {
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.y - (baseViewLocation[1] + baseView.getMeasuredHeight()) > getContentView().getMeasuredHeight();
    }
    /**
     * 修改悬浮框位置
     *
     * @param view
     * @param x
     * @param y
     */
    private void setLayout(View view, int x, int y) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, y, x + margin.width, y + margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    public void showAtTop() {
        windowLayout.removeView(triangle);
        windowLayout.addView(triangle);
        triangle.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.inverted_triangle));
        triangle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_bottom_in));
        window.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_bottom_in));
        setLayout(windowLayout, 0, baseViewLocation[1] - getContentView().getMeasuredHeight());
        showAtLocation(baseView, Gravity.CENTER, 0, 0);
    }

    public void showAtBottom() {
        triangle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_top_in));
        window.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_top_in));
        setLayout(windowLayout, 0, baseViewLocation[1] );
        showAtLocation(baseView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        triangle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_top_in));
        window.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_top_in));
        super.setOnDismissListener(onDismissListener);
    }

}

