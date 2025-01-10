package com.kenning.kcutil.utils.language;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;


import androidx.appcompat.widget.AppCompatTextView;

import com.kenning.kcutil.R;
import com.zyyoona7.popup.AdaptionDialog;

/**
* @Description: 用于显示更多的信息
* @author: create by zyl on 2025/01/09
*/
public class ShowMoreTextView extends AppCompatTextView {
    public ShowMoreTextView(final Context context) {
        super(context);
        this.setOnClickListener(v -> new AdaptionDialog((Activity) context, v, ShowMoreTextView.this.getText().toString()).show());
    }

    public ShowMoreTextView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnClickListener(v -> new AdaptionDialog((Activity) context, v, ShowMoreTextView.this.getText().toString()).show());
    }

    public ShowMoreTextView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setOnClickListener(v -> new AdaptionDialog((Activity) context, v, ShowMoreTextView.this.getText().toString()).show());
    }
}
