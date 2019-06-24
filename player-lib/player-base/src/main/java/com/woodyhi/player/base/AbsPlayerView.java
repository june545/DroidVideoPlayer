package com.woodyhi.player.base;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

/**
 * @author June
 * @date 2019-06-08
 */
public class AbsPlayerView extends FrameLayout {
    private static final String TAG = AbsPlayerView.class.getSimpleName();

    View videoView;
    View coverView;
    View controllerView;

    public AbsPlayerView(Context context) {
        this(context, null);
    }

    public AbsPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        setBackgroundColor(Color.parseColor("#FF021509"));
    }

    private void init(Context context) {

    }

    public void setVideoView(View view) {
        this.videoView = view;
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        videoView.setTag("SurfaceView");
        addView(videoView, 0, lp);
    }

    /** SurfaceView or TextureView */
    public View getVideoView() {
        return videoView;
    }

    public void setCoverView(View view) {
        View cv = findViewWithTag("Cover");
        if (cv != null) {
            removeView(cv);
            coverView = null;
        }
        this.coverView = view;
        this.coverView.setTag("Cover");
        addView(coverView);
    }

    public void setControllerView(View view) {
        View cv = findViewWithTag("Controller");
        if (cv != null) {
            removeView(cv);
            controllerView = null;
        }
        this.controllerView = view;
        this.controllerView.setTag("Controller");
        addView(controllerView);

        this.setOnClickListener(v -> {
            if (controllerView.getVisibility() == View.VISIBLE) {
                controllerView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bottom_slide_down));
                controllerView.setVisibility(View.GONE);
            } else {
                controllerView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bottom_slide_up));
                controllerView.setVisibility(View.VISIBLE);
            }
        });
    }

    public View getControllerView() {
        return controllerView;
    }

}
