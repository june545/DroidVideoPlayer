package com.woodyhi.player.b;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

/**
 * Created by June on 2018/5/21.
 */
public class DataSourceFactoryCreator {


    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();


    private Context context;

    private String userAgent;

    private Cache downloadCache;
    private File downloadDirectory;

    public DataSourceFactoryCreator(Context context) {
        this.context = context;

        userAgent = Util.getUserAgent(context, "application_name");
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *                          DataSource factory.
     * @return A new DataSource factory.
     */
    public DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    /**
     * Returns a {@link DataSource.Factory}.
     */
    public DataSource.Factory buildDataSourceFactory(TransferListener<? super DataSource> listener) {
        DefaultDataSourceFactory upstreamFactory =
                new DefaultDataSourceFactory(context, listener, buildHttpDataSourceFactory(listener));
        return buildReadOnlyCacheDataSource(upstreamFactory, getDownloadCache());
    }

    /**
     * Returns a {@link HttpDataSource.Factory}.
     */
    public HttpDataSource.Factory buildHttpDataSourceFactory(
            TransferListener<? super DataSource> listener) {
        return new DefaultHttpDataSourceFactory(userAgent, listener);
    }

    private static CacheDataSourceFactory buildReadOnlyCacheDataSource(
            DefaultDataSourceFactory upstreamFactory, Cache cache) {
        return new CacheDataSourceFactory(
                cache,
                upstreamFactory,
                new FileDataSourceFactory(),
                /* cacheWriteDataSinkFactory= */ null,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                /* eventListener= */ null);
    }

    private static final String DOWNLOAD_CONTENT_DIRECTORY = "downloads";

    private synchronized Cache getDownloadCache() {
        if (downloadCache == null) {
            File downloadContentDirectory = new File(getDownloadDirectory(), DOWNLOAD_CONTENT_DIRECTORY);
            downloadCache = new SimpleCache(downloadContentDirectory, new NoOpCacheEvictor());
        }
        return downloadCache;
    }

    private File getDownloadDirectory() {
        if (downloadDirectory == null) {
            downloadDirectory = context.getExternalFilesDir(null);
            if (downloadDirectory == null) {
                downloadDirectory = context.getFilesDir();
            }
        }
        return downloadDirectory;
    }

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
    //
    //        private List<?> getOfflineStreamKeys(Uri uri) {
    //            return PlayerConfig.getInstance(getApplicationContext())
    //                    .getDownloadTracker().getOfflineStreamKeys(uri);
    //        }
}
