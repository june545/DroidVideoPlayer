package com.woodyhi.gl.screenshot;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Calendar;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by June on 2018/5/29.
 */
public class ScreenShot {

    // Making created bitmap (from OpenGL points) compatible with Android bitmap.
    private static Bitmap createBitmapFromGLSurface(int x, int y, int w, int h)
            throws OutOfMemoryError {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            GLES20.glReadPixels(x, y, w, h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    public static void shotNew(int width, int height) {
        Bitmap inBitmap = createBitmapFromGLSurface(0, 0, width, height);

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


    public static void shot(int width_surface, int height_surface) {
        int w = width_surface;
        int h = height_surface;
        int b[] = new int[w * h];
        int bt[] = new int[w * h];
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
        Bitmap inBitmap = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);

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


}
