package com.woodyhi.media.control;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class ImageUtils {

    public static Bitmap getVectorBitmap(Context context, @DrawableRes int id) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            try {
                return drawableToBitmap(ContextCompat.getDrawable(context, id));
            } catch (Resources.NotFoundException e) {
                return drawableToBitmap(null);
            }
        }

        Drawable drawable;
        try {
            drawable = VectorDrawableCompat.create(context.getResources(), id, context.getTheme());
        } catch (Resources.NotFoundException e1) {
            try {
                drawable = ContextCompat.getDrawable(context, id);
            } catch (Resources.NotFoundException e2) {
                return drawableToBitmap(null);
            }
        }

        if (drawable != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                drawable = (DrawableCompat.wrap(drawable)).mutate();

            Bitmap result = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return result;
        }

        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null)
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444);

        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null)
                return bitmapDrawable.getBitmap();
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        else
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable, float scale) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null)
                return bitmapDrawable.getBitmap();
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        else
            bitmap = Bitmap.createBitmap((int) (scale * drawable.getIntrinsicWidth()), (int) (scale * drawable.getIntrinsicHeight()), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap setBitmapColor(Bitmap bitmap, int color) {
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(result, 0, 0, paint);

        return result;
    }

}
