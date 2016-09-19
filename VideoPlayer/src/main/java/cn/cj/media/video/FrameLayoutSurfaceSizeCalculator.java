package cn.cj.media.video;

import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by June on 2016/8/19.
 */
public class FrameLayoutSurfaceSizeCalculator {
	private static final String TAG                     = FrameLayoutSurfaceSizeCalculator.class.getSimpleName();
	private static final byte   SURFACESIZE_FIT_CENTER  = 0x1;
	private static final byte   SURFACESIZE_FILL        = 0x2;
	private static final byte   SURFACESIZE_CENTER_CROP = 0x3;

	private View        mSurfaceViewParent;
	private int         playerWidth;
	private int         playerHeight;
	private SurfaceView surfaceView;
	private int         videoWidth;
	private int         videoHeight;

	private byte mCurrentSurfaceSize = SURFACESIZE_FIT_CENTER;

	public void setFrame(View linearLayout) {
		this.mSurfaceViewParent = linearLayout;
	}

	public void setSurfaceView(SurfaceView surfaceView) {
		this.surfaceView = surfaceView;
	}

	public void setPlayerWH(int w, int h) {
		this.playerWidth = w;
		this.playerHeight = h;
	}

	public void setVideoSize(int w, int h) {
		this.videoWidth = w;
		this.videoHeight = h;
	}

	/**
	 * change player size & surface size
	 */
	public void changeSurfaceViewSize() {
		Log.d(TAG, "changeSurfaceViewSize -> surfaceview last size : w=" + surfaceView.getWidth() + ", h=" + surfaceView.getHeight());
		Log.d(TAG, "changeSurfaceViewSize -> player: w=" + playerWidth + ", h=" + playerHeight + ", video: w=" + videoWidth + ", h=" + videoHeight);

		// sanity check
		if (playerWidth * playerHeight == 0) {
			return;
		}

		int dw = playerWidth;
		int dh = playerHeight;

		if (videoWidth * videoHeight != 0) {
			float ar = (float) dw / (float) dh;
			Log.d(TAG, "changeSurfaceViewSize -> surfaceview's ParentView aspect ratio is " + ar);
			float v_ar = (float) videoWidth / (float) videoHeight;
			Log.d(TAG, "changeSurfaceViewSize -> video aspect ratio is " + v_ar);

			switch (mCurrentSurfaceSize) {
				case SURFACESIZE_FIT_CENTER:
					// fit view's size
					if (ar > v_ar) {
						dw = (int) (dh * v_ar);
					} else {
						dh = (int) (dw / v_ar);
					}
					break;
				case SURFACESIZE_FILL:
					break;
				case SURFACESIZE_CENTER_CROP:
					if (ar > v_ar) {
						dh = (int) (dw / v_ar);
					} else {
						dw = (int) (dh * v_ar);
					}
					break;
			}
		}

		// surface size
		ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
		lp.width = dw;
		lp.height = dh;

		Log.d(TAG, "changeSurfaceViewSize -> calculated, player: width=" + dw + ", height=" + dh);
		surfaceView.setLayoutParams(lp);

		// frame size
		LinearLayout.LayoutParams p_lp = (LinearLayout.LayoutParams) mSurfaceViewParent.getLayoutParams();
		p_lp.gravity = Gravity.CENTER;
		p_lp.width = playerWidth;
		p_lp.height = playerHeight;
		mSurfaceViewParent.setLayoutParams(p_lp);
		surfaceView.invalidate();
	}
}
