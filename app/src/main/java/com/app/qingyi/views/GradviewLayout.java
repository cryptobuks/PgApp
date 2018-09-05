package com.app.qingyi.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoLayoutHelper;

public class GradviewLayout extends RelativeLayout {
    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

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
        if (!isInEditMode())
        {
            mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, (int) (widthMeasureSpec + 130));
//        重写此方法后默认调用父类的onMeasure方法,分别将宽度测量空间与高度测量空间传入
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);/
    }

    @Override
    public AutoRelativeLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AutoRelativeLayout.LayoutParams(getContext(), attrs);
    }
}
