package com.app.qingyi.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhy.autolayout.utils.AutoLayoutHelper;

@SuppressLint("AppCompatCustomView")
public class AutoHeightImageView extends ImageView {

    private Drawable drawable = null;
    private static int mWidth = 0;
    public AutoHeightImageView(Context context) {
        super(context);
    }

    public AutoHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoHeightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        drawable = getDrawable();
        if (mWidth == 0) {
            mWidth = getMeasuredWidth();
            if (drawable != null) {
                setAutoHeight();
            }
        }
    }

    private void setAutoHeight() {
        if(drawable != null){
            float scale = drawable.getMinimumHeight() / (float) drawable.getMinimumWidth();
            float height = mWidth * scale;
            setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) height));
        }
    }

}
