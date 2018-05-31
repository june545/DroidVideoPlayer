package cn.woodyjc.media.video.player;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.woodyjc.media.video.R;
import cn.woodyjc.media.video.widget.BottomSideDialogFragment;

public class ControllerFragment extends BottomSideDialogFragment {

    private ImageView playPauseBtn;
    private LinearLayout mSeekBarLayout;
    private TextView mCurrentTime;
    private SeekBar mSeekBar;                                                    // 进度
    private TextView mDurationTime;
    private ImageView openCloseFullscreenBtn;
    private boolean mSeeking = false;
    private ImageView floatingViewIv;

    @Override
    public void onStart() {
        super.onStart();
//        getDialog().setCanceledOnTouchOutside(false);
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_controller, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        playPauseBtn = (ImageView) view.findViewById(R.id.play_pause_btn);
        mSeekBarLayout = (LinearLayout) view.findViewById(R.id.seekbar_layout);
        mCurrentTime = (TextView) view.findViewById(R.id.video_playtime);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);
        mDurationTime = (TextView) view.findViewById(R.id.video_durationtime);
        openCloseFullscreenBtn = (ImageView) view.findViewById(R.id.open_close_fullscreen);
        floatingViewIv = (ImageView) view.findViewById(R.id.show_flotingview);
    }


}
