package com.woodyhi.player.internal;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.LogUtil;
import com.woodyhi.player.base.PlayerCallback;
import com.woodyhi.player.base.ProgressTimer;
import com.woodyhi.player.base.R;
import com.woodyhi.player.base.Util;

/**
 * @author June
 * @date 2019-06-09
 */
public class DefaultControllerView extends FrameLayout {
    private final String TAG = DefaultControllerView.class.getSimpleName();

    private View player_control_bottom_bar;
    private ImageView playPauseBtn;
    private View seekAndTimeView;
    private TextView mCurrentTime;
    private SeekBar mSeekBar;
    private TextView mDurationTime;
    private ImageView fullscreenBtn;

    private AbsPlayerManager playerManger;
    private ProgressTimer progressTimer;

    private boolean isUserSeeking;

    public DefaultControllerView(@NonNull Context context) {
        this(context, null);
    }

    public DefaultControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        setup(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.video_player_controller, this);
        player_control_bottom_bar = view.findViewById(R.id.player_control_bottom_bar);
        playPauseBtn = view.findViewById(R.id.play_pause_btn);
        seekAndTimeView = view.findViewById(R.id.seekbar_and_time_layout);
        mCurrentTime = view.findViewById(R.id.video_playtime);
        mSeekBar = view.findViewById(R.id.seekbar);
        mDurationTime = view.findViewById(R.id.video_durationtime);
        fullscreenBtn = view.findViewById(R.id.open_close_fullscreen);

        this.setOnLongClickListener(v -> {
            playerManger.takeSnapshot(getContext());
            return true;
        });
        this.setOnClickListener(v -> {
            if (player_control_bottom_bar.getVisibility() == View.VISIBLE) {
                player_control_bottom_bar.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bottom_slide_down));
                player_control_bottom_bar.setVisibility(View.GONE);
            } else {
                player_control_bottom_bar.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bottom_slide_up));
                player_control_bottom_bar.setVisibility(View.VISIBLE);
            }
        });

        progressTimer = new ProgressTimer(this, 300);
        progressTimer.setCallback(new ProgressTimer.Callback() {
            @Override
            public void onTick(long ms) {
                if (playerManger == null) return;

                int duration = playerManger.getDuration();
                int currentPosition = playerManger.getCurrentPosition();
                LogUtil.d(TAG, " currentPosition " + currentPosition + "  duration " + duration);
                if (duration > 0 && currentPosition <= duration) {
                    if (!isUserSeeking) {
                        mSeekBar.setMax(duration);
                        mSeekBar.setProgress(currentPosition);
                    }

                    mCurrentTime.setText(Util.formatMillisTime(currentPosition));
                    mDurationTime.setText(Util.formatMillisTime(duration));
                }
            }
        });
        progressTimer.start();
    }

    private void setup(Context context) {
        playPauseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerManger != null) {
                    if (playerManger.isPlaying()) {
                        playerManger.pause();
                        playerManger.pause(true);
                        playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
                    } else {
                        playerManger.play(null);
                        playPauseBtn.setImageResource(R.drawable.baseline_pause_24);

                        progressTimer.start();
                    }
                }
            }
        });

        fullscreenBtn.setOnClickListener(v -> {
            Activity activity = Util.getActivityByContext(getContext());
            switch (activity.getResources().getConfiguration().orientation) {
                case Configuration.ORIENTATION_PORTRAIT:
                    fullscreenBtn.setImageResource(R.drawable.baseline_fullscreen_exit_24);
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    fullscreenBtn.setImageResource(R.drawable.baseline_fullscreen_24);
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new PlayerSeekBarChangeListener());
        mSeekBar.setOnClickListener(null); // 点击父布局thumb状态变成pressed
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            if (playerManger != null && playerManger.isSeekable())
                progressTimer.start();
        } else {
            progressTimer.stop();
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                fullscreenBtn.setImageResource(R.drawable.baseline_fullscreen_24);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                fullscreenBtn.setImageResource(R.drawable.baseline_fullscreen_exit_24);
                break;
        }
    }

    public void setPlayerManger(AbsPlayerManager playerManger) {
        this.playerManger = playerManger;
        this.playerManger.addPlayerCallback(playerCallback);
    }

    PlayerCallback playerCallback = new PlayerCallback() {
        @Override
        public void onBufferingUpdate(float percent) {
            mSeekBar.setSecondaryProgress((int) (mSeekBar.getMax() * percent / 100));
        }

        @Override
        public void onPlaying() {
            playPauseBtn.setImageResource(R.drawable.baseline_pause_24);
        }

        @Override
        public void onCompletion() {
            LogUtil.d(TAG, "onCompletion: --- " + playerManger.getCurrentPosition());
            // 进度矫正
            mCurrentTime.setText(Util.formatMillisTime(mSeekBar.getMax()));
            mSeekBar.setProgress(mSeekBar.getMax());

            playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
            progressTimer.stop();
        }
    };

    class PlayerSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        int tempSeekProgress = 0;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (tempSeekProgress >= 0) {
                if (playerManger != null)
                    playerManger.seekTo(tempSeekProgress);
            }
            isUserSeeking = false;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isUserSeeking = true;
            tempSeekProgress = 0;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                tempSeekProgress = progress;
            }
        }
    }

}
