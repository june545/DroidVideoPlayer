package com.woodyhi.player.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 宽和高其中一条边Match_parent固定为父布局大小，另一条wrap_content 根据16/9计算大小
 *
 * @author June
 */
public class FrameLayout_16_9 extends FrameLayout {
    private final static String TAG = FrameLayout_16_9.class.getSimpleName();


    public FrameLayout_16_9(@NonNull Context context) {
        super(context);
    }

    public FrameLayout_16_9(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayout_16_9(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        float aspectRatio = 9 / 16F;

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
            width = widthSpecSize;
            height = heightSpecSize;
        } else if (widthSpecMode == MeasureSpec.EXACTLY) {
            width = widthSpecSize;
            height = (int) (width * aspectRatio);
            if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                height = heightSpecSize;
            }
        } else if (heightSpecMode == MeasureSpec.EXACTLY) {
            height = heightSpecSize;
            width = (int) (height / aspectRatio);
            if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                width = widthSpecSize;
            }
        }

//        setMeasuredDimension(width, height); // 显示不出子View
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
}
