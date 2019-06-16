package com.woodyhi.player.ijk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woodyhi.player.base.AbsPlayerManager;
import com.woodyhi.player.base.PlaybackInfo;
import com.woodyhi.player.internal.DefaultControllerView;

/**
 * @auth June
 * @date 2019/06/10
 */
@Deprecated
public class IjkPlayerFragment extends Fragment {

    View rootView;
    IjkPlayerView videoPlayerView;

    AbsPlayerManager playerManger;
    PlaybackInfo playbackInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ijk_player, container, false);

        videoPlayerView = rootView.findViewById(R.id.video_player_view);
        playerManger = videoPlayerView.getPlayerManger();
        DefaultControllerView controllerView = new DefaultControllerView(getActivity());
        controllerView.setPlayerManger(playerManger);
        videoPlayerView.setControllerView(controllerView);
        playerManger.playback(playbackInfo);

        return rootView;
    }

    /**
     * 待播放视频
     */
    public void playback(PlaybackInfo info) {
        if (playerManger != null) {
            playerManger.playback(info);
        } else {
            this.playbackInfo = info;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (playerManger != null)
            playerManger.release();
    }
}
