package com.kenning.kcutil.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 物联通自定义EditText
 * 1.可移除TextChange监听事件用于设配器滑动防错乱
 * 2.当存在图片时可自定义图片的大小,并对图片增加点击事件
 * Created by zyl on 2017-10-20.
 */

public class WltEditText extends AppCompatEditText {
    private List<TextWatcher> watchers = new ArrayList<TextWatcher>();

    public WltEditText(Context context) {
        super(context);
    }

    public WltEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WltEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        watchers.add(watcher);
        super.addTextChangedListener(watcher);
    }

    public void removeTextChangedListener() {
        for (int i = 0; i < watchers.size(); i++) {
            removeTextChangedListener(watchers.get(i));
        }
    }

    private Drawable drawableRight;
    private Drawable drawableLeft;
    private Drawable drawableTop;
    private Drawable drawableBottom;
    int actionX, actionY;
    private WltViewDrawableClickListener clickListener;

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            drawableLeft = left;
//            int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
//            drawableLeft.setBounds(0, 0, dp, dp);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        }
        if (right != null) {
            drawableRight = right;
//            int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
//            drawableRight.setBounds(0, 0, dp1, dp1);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    public void setCompoundDrawables2(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            drawableLeft = left;
            int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            drawableLeft.setBounds(0, 0, dp, dp);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        }
        if (right != null) {
            drawableRight = right;
            int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            drawableRight.setBounds(0, 0, dp1, dp1);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect bounds;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            actionX = (int) event.getX();
            actionY = (int) event.getY();
            if (drawableBottom != null
                    && drawableBottom.getBounds().contains(actionX, actionY)) {
                if (clickListener != null)
                    clickListener.onClick(WltViewDrawableClickListener.DrawablePosition.BOTTOM);
                return super.onTouchEvent(event);
            }
            if (drawableTop != null
                    && drawableTop.getBounds().contains(actionX, actionY)) {
                if (clickListener != null)
                    clickListener.onClick(WltViewDrawableClickListener.DrawablePosition.TOP);
                return super.onTouchEvent(event);
            }
            if (drawableLeft != null) {
                bounds = drawableLeft.getBounds();
                int x, y;
                int extraTapArea = (int) (13 * getResources().getDisplayMetrics().density + 0.5);
                x = actionX;
                y = actionY;
                if (!bounds.contains(actionX, actionY)) {
                    x = (int) (actionX - extraTapArea);
                    y = (int) (actionY - extraTapArea);
                    if (x <= 0)
                        x = actionX;
                    if (y <= 0)
                        y = actionY;
                    if (x < y) {
                        y = x;
                    }
                }
                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener
                            .onClick(WltViewDrawableClickListener.DrawablePosition.LEFT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;

                }
            }
            if (drawableRight != null) {
                bounds = drawableRight.getBounds();
                int x, y;
                int extraTapArea = 13;
                x = (int) (actionX + extraTapArea);
                y = (int) (actionY - extraTapArea);
                x = getWidth() - x;
                if (x <= 0) {
                    x += extraTapArea;
                }
                if (y <= 0)
                    y = actionY;
                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener
                            .onClick(WltViewDrawableClickListener.DrawablePosition.RIGHT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;
                }
                return super.onTouchEvent(event);
            }

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        drawableRight = null;
        drawableBottom = null;
        drawableLeft = null;
        drawableTop = null;
        super.finalize();
    }

    public WltEditText setDrawableClickListener(WltViewDrawableClickListener listener) {
        this.clickListener = listener;
        return this;
    }

    @Override
    public void setOnKeyListener(OnKeyListener l) {
        super.setOnKeyListener(l);
    }

    /**
     * drawableRight的显示隐藏
     *
     * @param visible
     */
    public void setDrawableRightVisible(boolean visible) {
        if (visible) {
            setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
        } else {
            setCompoundDrawables(drawableLeft, drawableTop, null, drawableBottom);
        }
    }

}
