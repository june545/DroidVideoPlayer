package com.woodyhi.player.base;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.woodyhi.player.widget.VideoSurfaceView;

/**
 * @author June
 * @date 2019-06-08
 */
public class AbsPlayerView extends FrameLayout {
    private static final String TAG = AbsPlayerView.class.getSimpleName();

    VideoSurfaceView surfaceView;
    View coverView;
    View controllerView;

    AbsPlayerManager playerManger;

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
        surfaceView = new VideoSurfaceView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        surfaceView.setTag("SurfaceView");
        addView(surfaceView, lp);
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
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

    public AbsPlayerManager getPlayerManger() {
        return playerManger;
    }

    public void setPlayerManager(AbsPlayerManager playerManger) {
        this.playerManger = playerManger;
        surfaceView.setPlayerManager(this.playerManger);
    }

}
