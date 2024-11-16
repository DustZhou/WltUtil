package com.kenning.kcutil.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.kenning.kcutil.widget.WltViewDrawableClickListener.DrawablePosition;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 增加DrawableLeft, Right点击事件
 */
public class WltTextView extends AppCompatTextView {
    private List<TextWatcher> watchers = new ArrayList<TextWatcher>();

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

    public WltTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WltTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (clickListener != null)
                        clickListener.onClick(DrawablePosition.RIGHT);
                    return true;
                }
                return false;
            }
        });
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String texts = s.toString();
                //结尾竟然是\r\n
                if (texts.endsWith("\n")) {
                    if (clickListener != null)
                        clickListener.onClick(DrawablePosition.RIGHT);
                }
            }
        });

    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect bounds;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            actionX = (int) event.getX();
            actionY = (int) event.getY();
            if (drawableBottom != null
                    && drawableBottom.getBounds().contains(actionX, actionY)) {
                if (clickListener != null)
                    clickListener.onClick(DrawablePosition.BOTTOM);
                return super.onTouchEvent(event);
            }
            if (drawableTop != null
                    && drawableTop.getBounds().contains(actionX, actionY)) {
                if (clickListener != null)
                    clickListener.onClick(DrawablePosition.TOP);
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
                            .onClick(DrawablePosition.LEFT);
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
                            .onClick(DrawablePosition.RIGHT);
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

    public WltTextView setDrawableClickListener(WltViewDrawableClickListener listener) {
        this.clickListener = listener;
        return this;
    }

}