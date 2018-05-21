package com.woodyhi.player.b;

/**
 * Created by June on 2018/5/11.
 */
@Deprecated
public class PlayerUtil {



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
