package cn.woodyjc.media.a;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CENTER /center  按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示
 * <p>
 * CENTER_CROP / centerCrop  按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
 * <p>
 * CENTER_INSIDE / centerInside  将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽
 * <p>
 * FIT_CENTER / fitCenter  把图片按比例扩大/缩小到View的宽度，居中显示
 * <p>
 * FIT_END / fitEnd   把图片按比例扩大/缩小到View的宽度，显示在View的下部分位置
 * <p>
 * FIT_START / fitStart  把图片按比例扩大/缩小到View的宽度，显示在View的上部分位置
 * <p>
 * FIT_XY / fitXY  把图片不按比例扩大/缩小到View的大小显示
 * <p>
 * MATRIX / matrix 用矩阵来绘制
 * Created by June on 2018/5/31.
 */
public class AspectRatioLayout extends FrameLayout {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RESIZE_TYPE_FILL,
            RESIZE_TYPE_FIT_CENTER,
            RESIZE_TYPE_CENTER_CROP,
            RESIZE_TYPE_16_9})
    private @interface ResizeType {
    }

    public static final int RESIZE_TYPE_16_9 = 4;
    public static final int RESIZE_TYPE_FIT_CENTER = 1;
    public static final int RESIZE_TYPE_FILL = 2;
    public static final int RESIZE_TYPE_CENTER_CROP = 3;

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


    public void setResizeType(int resizeType) {
        this.resizeType = resizeType;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (videoWidth * videoHeight <= 0) {
            return;
        }

        float videoAspectRatio = (float) videoWidth / videoHeight;

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float viewAspectRatio = (float) width / height;

        switch (resizeType) {

            case RESIZE_TYPE_FIT_CENTER:
                if (videoAspectRatio > viewAspectRatio) {
                    height = (int) (width / videoAspectRatio);
                } else {
                    width = (int) (height * videoAspectRatio);
                }

                break;

            case RESIZE_TYPE_CENTER_CROP:
                if (videoAspectRatio > viewAspectRatio) {
                    width = (int) (height * videoAspectRatio);
                } else {
                    height = (int) (width / videoAspectRatio);
                }

                break;
            case RESIZE_TYPE_16_9:
                float desiredAspectRatio = 16F / 9;
                if (desiredAspectRatio > viewAspectRatio) {
                    height = (int) (width / desiredAspectRatio);
                } else {
                    width = (int) (height * desiredAspectRatio);
                }
                break;
            case RESIZE_TYPE_FILL:
            default:
                break;
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    /**
     * @param videoWidth
     * @param videoHeight
     */
    public void setVideoSize(int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        requestLayout();
    }
}
