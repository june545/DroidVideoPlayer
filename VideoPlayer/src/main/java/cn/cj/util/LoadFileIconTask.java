package cn.cj.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import cn.cj.comm.BitmapUtil;
import cn.cj.comm.MimeTypeUtil;
import cn.cj.comm.PackageUtil;
import cn.cj.media.video.player.R;

/**
 * Created by June on 2016/8/15.
 */
public class LoadFileIconTask extends AsyncTask<String, Void, Bitmap> {
    private final String TAG = LoadFileIconTask.class.getSimpleName();
    private Context context;
    private WeakReference<ImageView> imgWeakReference;
    private String logString; // for debug
    private static int count;

    public LoadFileIconTask(Context context, ImageView img) {
        this.context = context;
        imgWeakReference = new WeakReference<ImageView>(img);
        //			logString = "---" + toString() + "--- constructor ";
        //			System.out.println(logString);
        img.setImageResource(R.drawable.touchFeedback); // 默认背景
        Log.d(TAG, "task count: " + ++count + ", task address : " + toString());
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        //			logString += "--- doInBackground";
        //			System.out.println(logString);
        //			if(isCancelled()){
        //				System.out.println("Cancelled-----------------------------do");
        //			}
        if (params.length > 0 && params[0] != null) {
            if (params[0].endsWith(".apk")) {
                return ((BitmapDrawable) PackageUtil.getAppIcon(context, params[0])).getBitmap();

            } else if (MimeTypeUtil.checkIfVideoByPath(params[0])) {
                //				return BitmapUtil.getVideoThumbnail(params[0], 64, 48, MediaStore.Video.Thumbnails.MICRO_KIND);
                return ThumbnailUtils.createVideoThumbnail(params[0], MediaStore.Video.Thumbnails.MICRO_KIND);

            }
            if (MimeTypeUtil.checkIfPictureByPath(params[0])) {
                return BitmapUtil.getImageThumbnail(params[0], 64, 48);

            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bmp) {
        //			if(isCancelled()){
        //				System.out.println("Cancelled-----------------------------post");
        //			}
        //			logString += "---onPostExecute";
        //			System.out.println(logString);
        ImageView img = imgWeakReference.get();
        if (img == null) {
            img.setImageResource(R.drawable.touchFeedback);
        } else {
            img.setImageBitmap(bmp);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //			logString += "--- onCancelled";
        //			System.out.println(logString);
    }

}
