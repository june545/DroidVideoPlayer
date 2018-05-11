package com.woodyhi.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.offline.FilteringManifestParser;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser;
import com.google.android.exoplayer2.source.dash.manifest.RepresentationKey;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import com.google.android.exoplayer2.source.hls.playlist.RenditionKey;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.StreamKey;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.woodyhi.player.a.PlayerConfig;
import com.woodyhi.player.b.PlayerManager;

import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    private PlayerView playerView;

    private SimpleExoPlayer simpleExoPlayer;

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private DataSource.Factory mediaDataSourceFactory;


    //            Uri uri = Uri.parse("http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4");
    //    Uri uri = Uri.parse("rtmp://live.hkstv.hk.lxdns.com/live/hks");
//    Uri uri = Uri.parse("rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov");
    Uri uri = Uri.parse("https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        playerView = findViewById(R.id.player_view);


        // ----- Create the player
        simpleExoPlayer = creatingPlayer(getApplicationContext());

        // ----- Bind the player to the view.
        playerView.setPlayer(simpleExoPlayer);

        // ----- Prepare the player with the source.
//        simpleExoPlayer.prepare(createMediaSource(uri));
        simpleExoPlayer.prepare(new PlayerManager(getApplicationContext()).buildMediaSource(uri));

        // play automaticly when ready
        simpleExoPlayer.setPlayWhenReady(true);
    }

    private SimpleExoPlayer creatingPlayer(Context context){
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        SimpleExoPlayer player =
                ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        return player;
    }

    private MediaSource createMediaSource(Uri uri){
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                Util.getUserAgent(getApplicationContext(), "yourApplicationName"), bandwidthMeter1);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        // Prepare the player with the source.
        //        player.prepare(videoSource);
        return videoSource;
    }
    private MediaSource buildMediaSource(
            Uri uri,
            String overrideExtension,
            @Nullable Handler handler,
            @Nullable MediaSourceEventListener listener) {
        @C.ContentType int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
                : Util.inferContentType("." + overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }


//    @SuppressWarnings("unchecked")
//    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
//        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
//        switch (type) {
//            case C.TYPE_DASH:
//                return new DashMediaSource.Factory(
//                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
//                        buildDataSourceFactory(false))
//                        .setManifestParser(
//                                new FilteringManifestParser<>(
//                                        new DashManifestParser(), (List<RepresentationKey>) getOfflineStreamKeys(uri)))
//                        .createMediaSource(uri);
//            case C.TYPE_SS:
//                return new SsMediaSource.Factory(
//                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
//                        buildDataSourceFactory(false))
//                        .setManifestParser(
//                                new FilteringManifestParser<>(
//                                        new SsManifestParser(), (List<StreamKey>) getOfflineStreamKeys(uri)))
//                        .createMediaSource(uri);
//            case C.TYPE_HLS:
//                return new HlsMediaSource.Factory(mediaDataSourceFactory)
//                        .setPlaylistParser(
//                                new FilteringManifestParser<>(
//                                        new HlsPlaylistParser(), (List<RenditionKey>) getOfflineStreamKeys(uri)))
//                        .createMediaSource(uri);
//            case C.TYPE_OTHER:
//                return new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
//            default: {
//                throw new IllegalStateException("Unsupported type: " + type);
//            }
//        }
//    }



    /**
//     * Returns a new DataSource factory.
//     *
//     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
//     *     DataSource factory.
//     * @return A new DataSource factory.
//     */
//    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
//        return PlayerConfig.getInstance(getApplicationContext())
//                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
//    }
//
//    private List<?> getOfflineStreamKeys(Uri uri) {
//        return PlayerConfig.getInstance(getApplicationContext())
//                .getDownloadTracker().getOfflineStreamKeys(uri);
//    }
}
