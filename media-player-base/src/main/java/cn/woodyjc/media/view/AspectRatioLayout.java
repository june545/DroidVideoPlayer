package cn.woodyjc.media.view;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * 缩放类型：
 * </p>
 * <ul>
 * <li>{@link #RESIZE_TYPE_FIT_CENTER}</li>
 * <li>{@link #RESIZE_TYPE_FILL}</li>
 * <li>{@link #RESIZE_TYPE_CENTER_CROP}</li>
 * <li>{@link #RESIZE_TYPE_16_9}</li>
 * </ul>
 * </p>
 * Created by June on 2018/5/31.
 */
public class AspectRatioLayout extends FrameLayout {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RESIZE_TYPE_FIT_CENTER,
            RESIZE_TYPE_FILL,
            RESIZE_TYPE_CENTER_CROP,
            RESIZE_TYPE_16_9})
    private @interface ResizeType {
    }

    /**
     * 按比例缩放至 宽（高）等于或小于父view宽（高），并居中显示
     */
    public static final int RESIZE_TYPE_FIT_CENTER = 1;
    /**
     * 不考虑纵横比，使得宽（高）等于父view宽（高），相当于填满父view
     */
    public static final int RESIZE_TYPE_FILL = 2;
    /**
     * 按此比例缩放至宽（高）等于或大于父view宽（高），超出的被裁剪
     */
    public static final int RESIZE_TYPE_CENTER_CROP = 3;
    /**
     * 纵横比为固定值 16 / 9， 按此比例缩放至宽（高）等于或小于父view宽（高），且居中
     */
    public static final int RESIZE_TYPE_16_9 = 4;

    @ResizeType
    private int resizeType;

    private int videoWidth;
    private int videoHeight;

    public AspectRatioLayout(@NonNull Context context) {
        super(context);
    }

    public AspectRatioLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRatioLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getResizeType() {
        return resizeType;
    }

    public void setResizeType(@ResizeType int resizeType) {
        if (this.resizeType != resizeType) {
            this.resizeType = resizeType;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float aspectRatio;
        if (resizeType == RESIZE_TYPE_16_9) {
            aspectRatio = 16F / 9;
        } else {
            if (videoWidth * videoHeight <= 0) {
                return;
            }
            aspectRatio = (float) videoWidth / videoHeight;
        }

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float viewAspectRatio = (float) width / height;

        switch (resizeType) {
            case RESIZE_TYPE_FIT_CENTER:
                if (aspectRatio > viewAspectRatio) {
                    height = (int) (width / aspectRatio);
                } else {
                    width = (int) (height * aspectRatio);
                }

                break;

            case RESIZE_TYPE_CENTER_CROP:
                if (aspectRatio > viewAspectRatio) {
                    width = (int) (height * aspectRatio);
                } else {
                    height = (int) (width / aspectRatio);
                }

                break;
            case RESIZE_TYPE_16_9:
                if (width == 0 && height != 0) {
                    width = (int) (height * aspectRatio);
                } else if (height == 0 && width != 0) {
                    height = (int) (width / aspectRatio);
                } else if (aspectRatio > viewAspectRatio) {
                    height = (int) (width / aspectRatio);
                } else {
                    width = (int) (height * aspectRatio);
                }
                break;
            case RESIZE_TYPE_FILL:
            default:
                break;
        }

        Log.d("22222", "width " + width + " ,  height " + height);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    /**
     * @param videoWidth
     * @param videoHeight
     */
    public void setAspectRatioByVideoSize(int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        requestLayout();
    }
}
