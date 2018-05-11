package com.woodyhi.player.a;

import android.content.ContentProvider;
import android.content.Context;

import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.upstream.DataSource;
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

import java.io.File;
import java.util.ConcurrentModificationException;

/**
 * Created by June on 2018/5/11.
 */
public class PlayerConfig {
//
//    private static final String DOWNLOAD_CONTENT_DIRECTORY = "downloads";
//
//    private Context context;
//
//    protected String userAgent;
//
//    private File downloadDirectory;
//    private Cache downloadCache;
//    private DownloadManager downloadManager;
//    private DownloadTracker downloadTracker;
//
//
//    private static PlayerConfig instance;
//
//    public static PlayerConfig getInstance(Context c){
//        if(instance == null){
//            instance = new PlayerConfig(c, null);
//        }
//        return instance;
//    }
//
//
//    public PlayerConfig(Context context, String userAgent) {
//        this.context = context;
//        this.userAgent = userAgent;
//    }
//
//    /** Returns a {@link HttpDataSource.Factory}. */
//    public HttpDataSource.Factory buildHttpDataSourceFactory(
//            TransferListener<? super DataSource> listener) {
//        return new DefaultHttpDataSourceFactory(userAgent, listener);
//    }
//
//    /** Returns a {@link DataSource.Factory}. */
//    public DataSource.Factory buildDataSourceFactory(TransferListener<? super DataSource> listener) {
//        DefaultDataSourceFactory upstreamFactory =
//                new DefaultDataSourceFactory(context, listener, buildHttpDataSourceFactory(listener));
//        return buildReadOnlyCacheDataSource(upstreamFactory, getDownloadCache());
//    }
//
//
//    public DownloadTracker getDownloadTracker() {
//        initDownloadManager();
//        return downloadTracker;
//    }
//
//    private synchronized void initDownloadManager() {
//        if (downloadManager == null) {
//            DownloaderConstructorHelper downloaderConstructorHelper =
//                    new DownloaderConstructorHelper(
//                            getDownloadCache(), buildHttpDataSourceFactory(/* listener= */ null));
//            downloadManager =
//                    new DownloadManager(
//                            downloaderConstructorHelper,
//                            MAX_SIMULTANEOUS_DOWNLOADS,
//                            DownloadManager.DEFAULT_MIN_RETRY_COUNT,
//                            new File(getDownloadDirectory(), DOWNLOAD_ACTION_FILE),
//                            DOWNLOAD_DESERIALIZERS);
//            downloadTracker =
//                    new DownloadTracker(
//                            /* context= */ this,
//                            buildDataSourceFactory(/* listener= */ null),
//                            new File(getDownloadDirectory(), DOWNLOAD_TRACKER_ACTION_FILE),
//                            DOWNLOAD_DESERIALIZERS);
//            downloadManager.addListener(downloadTracker);
//        }
//    }
//
//    private File getDownloadDirectory() {
//        if (downloadDirectory == null) {
//            downloadDirectory = context.getExternalFilesDir(null);
//            if (downloadDirectory == null) {
//                downloadDirectory = context.getFilesDir();
//            }
//        }
//        return downloadDirectory;
//    }
//
//    private synchronized Cache getDownloadCache() {
//        if (downloadCache == null) {
//            File downloadContentDirectory = new File(getDownloadDirectory(), DOWNLOAD_CONTENT_DIRECTORY);
//            downloadCache = new SimpleCache(downloadContentDirectory, new NoOpCacheEvictor());
//        }
//        return downloadCache;
//    }
//
//    private static CacheDataSourceFactory buildReadOnlyCacheDataSource(
//            DefaultDataSourceFactory upstreamFactory, Cache cache) {
//        return new CacheDataSourceFactory(
//                cache,
//                upstreamFactory,
//                new FileDataSourceFactory(),
//                /* cacheWriteDataSinkFactory= */ null,
//                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
//                /* eventListener= */ null);
//    }
}
