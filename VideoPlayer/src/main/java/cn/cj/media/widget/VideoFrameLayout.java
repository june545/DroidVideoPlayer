package cn.cj.media.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by June on 2016/8/22.
 */
public class VideoFrameLayout extends FrameLayout {

	public static final int padding = 6;

	protected boolean isSelectedBackground;

	public VideoFrameLayout(Context context) {
		super(context);
	}

	public VideoFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VideoFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setSelectedBackground(boolean selected) {
		setPadding(padding, padding, padding, padding);
		isSelectedBackground = selected;
		//        Log.d(toString(), "----setSelected--" + selected);
		if (selected) {
			setBackgroundDrawable(selectedDrawable);
		} else {
			setBackgroundDrawable(drawable);
		}
	}

	// selected背景
	static ShapeDrawable selectedDrawable = new ShapeDrawable(new Shape() {
		@Override
		public void draw(Canvas canvas, Paint paint) {
			int d = padding / 2;
			paint.setColor(0xffc70000);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(padding);
			canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);

			paint.reset();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.BLACK);
			canvas.drawRect(d, d, canvas.getWidth() - d, canvas.getHeight() - d, paint);
		}
	});

	// normal背景
	static ShapeDrawable drawable = new ShapeDrawable(new Shape() {
		@Override
		public void draw(Canvas canvas, Paint paint) {
			int d = 1;
			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(2);
			canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), paint);

			paint.reset();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.BLACK);
			canvas.drawRect(d, d, canvas.getWidth() - d, canvas.getHeight() - d, paint);
		}
	});
}
