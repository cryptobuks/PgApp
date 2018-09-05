package com.app.qingyi.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class GradviewLayout extends RelativeLayout {
    public GradviewLayout(Context context) {
        super(context);
    }

    public GradviewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradviewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec / 10 * 13);
//        重写此方法后默认调用父类的onMeasure方法,分别将宽度测量空间与高度测量空间传入
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);/
    }
}
