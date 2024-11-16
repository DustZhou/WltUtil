package com.kenning.kcutil.widget;

/**
 * 图片点击监听
 * @author ZhongYL
 * @version 创建时间：2016-8-31 下午3:32:56
 */
public interface WltViewDrawableClickListener {

    public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
    public void onClick(DrawablePosition target);
}