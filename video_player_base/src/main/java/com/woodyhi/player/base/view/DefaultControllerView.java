package com.woodyhi.player.base.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.woodyhi.player.base.LogUtil;
import com.woodyhi.player.base.PlayerCallback;
import com.woodyhi.player.base.PlayerManger;
import com.woodyhi.player.base.ProgressTimer;
import com.woodyhi.player.base.R;
import com.woodyhi.player.base.Util;

/**
 * @author June
 * @date 2019-06-09
 */
public class DefaultControllerView extends FrameLayout {
    private final String TAG = DefaultControllerView.class.getSimpleName();

    private ImageView playPauseBtn;
    private View seekAndTimeView;
    private TextView mCurrentTime;
    private SeekBar mSeekBar;
    private TextView mDurationTime;
    private ImageView fullscreenBtn;

    private PlayerManger playerManger;
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
        playPauseBtn = view.findViewById(R.id.play_pause_btn);
        seekAndTimeView = view.findViewById(R.id.seekbar_and_time_layout);
        mCurrentTime = view.findViewById(R.id.video_playtime);
        mSeekBar = view.findViewById(R.id.seekbar);
        mDurationTime = view.findViewById(R.id.video_durationtime);
        fullscreenBtn = view.findViewById(R.id.open_close_fullscreen);

        progressTimer = new ProgressTimer(this, 500);
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
                        playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
                    } else {
                        playerManger.playback();
                        playPauseBtn.setImageResource(R.drawable.baseline_pause_24);

                        progressTimer.start();
                    }
                }
            }
        });

        fullscreenBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int rotation = Util.getActivityByContext(getContext())
                        .getWindowManager()
                        .getDefaultDisplay()
                        .getRotation();
                switch (rotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        fullscreenBtn.setImageResource(R.drawable.baseline_fullscreen_exit_24);
                        Util.getActivityByContext(getContext())
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        fullscreenBtn.setImageResource(R.drawable.baseline_fullscreen_24);
                        Util.getActivityByContext(getContext())
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new PlayerSeekBarChangeListener());
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (playerManger != null && playerManger.isSeekable())
            if (visibility == View.VISIBLE) {
                progressTimer.start();
            } else {
                progressTimer.stop();
            }
    }

    public void setPlayerManger(PlayerManger playerManger) {
        this.playerManger = playerManger;
        this.playerManger.addPlayerListener(playerCallback);
    }

    PlayerCallback playerCallback = new PlayerCallback() {
        @Override
        public void onCompletion() {
            LogUtil.d(TAG, "onCompletion: --- ");
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
