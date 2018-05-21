package com.woodyhi.player.b;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.offline.FilteringManifestParser;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

/**
 * Created by June on 2018/5/21.
 */
public class MediaSourceCreator {

    private Context context;

    private DataSourceFactoryCreator dataSourceFactoryCreator;

    private final DataSource.Factory manifestDataSourceFactory;
    private final DataSource.Factory mediaDataSourceFactory;

    public MediaSourceCreator(Context context){
        this.context = context;
        dataSourceFactoryCreator = new DataSourceFactoryCreator(context);

        mediaDataSourceFactory = dataSourceFactoryCreator.buildDataSourceFactory(true);
        manifestDataSourceFactory = dataSourceFactoryCreator.buildDataSourceFactory(false);
    }

    @SuppressWarnings("unchecked")
    public MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory)
//                        .setManifestParser(
//                                new FilteringManifestParser<>(
//                                        new DashManifestParser(), (List<RepresentationKey>) getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory)
//                        .setManifestParser(
//                                new FilteringManifestParser<>(
//                                        new SsManifestParser(), (List<StreamKey>) getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
//                        .setPlaylistParser(
//                                new FilteringManifestParser<>(
//                                        new HlsPlaylistParser(), (List<RenditionKey>) getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

//    private List<?> getOfflineStreamKeys(Uri uri) {
//        return getDownloadTracker().getOfflineStreamKeys(uri);
//    }

//    public DownloadTracker getDownloadTracker() {
//        initDownloadManager();
//        return downloadTracker;
//    }
}
