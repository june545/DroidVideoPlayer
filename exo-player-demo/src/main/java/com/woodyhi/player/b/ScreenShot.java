package com.woodyhi.player.b;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.util.Calendar;

import javax.microedition.khronos.opengles.GL10;

import static android.support.constraint.Constraints.TAG;
import static org.checkerframework.checker.units.UnitsTools.h;

/**
 * Created by June on 2018/5/29.
 */
public class ScreenShot {

    //    public String takeScreenShot(Context context, GL10 gl) {
    //        if(context == null){
    //            return null;
    //        }
    //        String filePath = FileUtils.getInstance().getStorePicFile(context);
    //        DisplayMetrics displayMetrics = DeviceHelper.getDisplayMetrics(context);
    //        Bitmap bitmap = getBitmapFromGL(displayMetrics.widthPixels, displayMetrics.heightPixels, gl);
    //        //判断图片大小，排除黑屏的情况 <20K 即为黑屏。
    //        File imagePath = new File(filePath);
    //        FileOutputStream fos = null;
    //        try {
    //            fos = new FileOutputStream(imagePath);
    //            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
    //            fos.flush();
    //        } catch (Exception e) {
    //        } finally {
    //            try {
    //                fos.close();
    //                bitmap.recycle();
    //            } catch (Exception e) {
    //            }
    //            retryCount = 0;
    //        }
    //        return filePath;
    //    }

    /**
     * 将GL10帧数据保存为图片
     *
     * @param w
     * @param h
     * @param gl
     * @return
     */
    private Bitmap getBitmapFromGL(int w, int h, GL10 gl) {
        int b[] = new int[w * (h)];
        int bt[] = new int[w * h];
        IntBuffer ib = IntBuffer.wrap(b);
        ib.position(0);
        gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
        for (int i = 0, k = 0; i < h; i++, k++) {
            for (int j = 0; j < w; j++) {
                int pix = b[i * w + j];
                int pb = (pix >> 16) & 0xff;
                int pr = (pix << 16) & 0xffff0000;
                int pix1 = (pix & 0xff00ff00) | pr | pb;
                bt[(h - k - 1) * w + j] = pix1;
            }
        }
        return Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
    }

    public static void shot(int width_surface, int height_surface) {
        int w = width_surface;
        int h = height_surface;
        int b[] = new int[(int) (w * h)];
        int bt[] = new int[(int) (w * h)];
        IntBuffer buffer = IntBuffer.wrap(b);
        buffer.position(0);
        GLES20.glReadPixels(0, 0, w, h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
        for (int i = 0; i < h; i++) {
            //remember, that OpenGL bitmap is incompatible with Android bitmap
            //and so, some correction need.
            for (int j = 0; j < w; j++) {
                int pix = b[i * w + j];
                int pb = (pix >> 16) & 0xff;
                int pr = (pix << 16) & 0x00ff0000;
                int pix1 = (pix & 0xff00ff00) | pr | pb;
                bt[(h - i - 1) * w + j] = pix1;
            }
        }
        Bitmap inBitmap = null;
        if (inBitmap == null || !inBitmap.isMutable()
                || inBitmap.getWidth() != w || inBitmap.getHeight() != h) {
            inBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        }
        //Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        inBitmap.copyPixelsFromBuffer(buffer);
        //return inBitmap ;
        // return Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
        inBitmap = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        inBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream fis = new ByteArrayInputStream(bitmapdata);

        final Calendar c = Calendar.getInstance();
        long mytimestamp = c.getTimeInMillis();
        String timeStamp = String.valueOf(mytimestamp);
        String myfile = "hari" + timeStamp + ".jpeg";

        File dir_image = new File(Environment.getExternalStorageDirectory() + File.separator +
                "printerscreenshots" + File.separator + "image");
        dir_image.mkdirs();

        try {
            File tmpFile = new File(dir_image, myfile);
            FileOutputStream fos = new FileOutputStream(tmpFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void getBitmap(Context context, TextureView vv)
    {
        String mPath = Environment.getExternalStorageDirectory().toString()
                + "/Pictures/" + System.currentTimeMillis() + ".png";
        Toast.makeText(context, "Capturing Screenshot: " + mPath, Toast.LENGTH_SHORT).show();

        Bitmap bm = vv.getBitmap();
        if(bm == null)
            Log.e(TAG,"bitmap is null");

        OutputStream fout = null;
        File imageFile = new File(mPath);

        try {
            fout = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IOException");
            e.printStackTrace();
        }
    }
}
