package cn.woodyjc.media.video.a;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import cn.woodyjc.media.video.player.DisplayUtil;


/**
 * <p>手势:</p>
 * <ul>
 * <li>单点</li>
 * <li>向上滑动</li>
 * <li>向下滑动</li>
 * <li>向左滑动</li>
 * <li>向右滑动</li>
 * </ul>
 * <p>
 * Created by June on 2018/5/9.
 */
public class GestureSupport {
    private final String TAG = GestureSupport.class.getSimpleName();

    public static final byte GESTURE_FROM_LEFT_TO_RIGHT = 0x4;
    public static final byte GESTURE_FROM_RIGHT_TO_LEFT = 0x5;
    public static final byte GESTURE_FROM_BOTTOM_TO_TOP = 0x6;
    public static final byte GESTURE_FROM_TOP_TO_BOTTOM = 0x7;


    /**
     * {@link #GESTURE_FROM_LEFT_TO_RIGHT},{@link #GESTURE_FROM_RIGHT_TO_LEFT}
     */
    private byte horizontalActualDirection = 0;
    /**
     * {@link #GESTURE_FROM_BOTTOM_TO_TOP},{@link #GESTURE_FROM_TOP_TO_BOTTOM}
     */
    private byte verticalActualDirection = 0;


    private Context mContext;
    private View mView;


    public enum GestureDirection {
        NON, HORIZONTAL, VERTICAL
    }

    public GestureDirection direction = GestureDirection.NON;


    private GestureClickListener mGestureClickListener;
    private GestureFromLeftToRightListener mGestureFromLeftToRightListener;
    private GestureFromRightToLeftListener mGestureFromRightToLeftListener;
    private GestureFromTopToBottomListener mGestureFromTopToBottomListener;
    private GestureFromBottomToTopListener mGestureFromBottomToTopListener;

    private GestureDetector mGestureDetector;


    public GestureSupport(Context context, View view) {
        this.mContext = context;
        this.mView = view;

        view.setOnTouchListener(new MyTouchListener());
        mGestureDetector = new GestureDetector(mContext, new MySimpleGestureListener());
    }

    public void setGestureClickListener(GestureClickListener mGestureClickListener) {
        this.mGestureClickListener = mGestureClickListener;
    }

    public void setGestureFromLeftToRightListener(GestureFromLeftToRightListener mGestureFromLeftToRightListener) {
        this.mGestureFromLeftToRightListener = mGestureFromLeftToRightListener;
    }

    public void setGestureFromRightToLeftListener(GestureFromRightToLeftListener mGestureFromRightToLeftListener) {
        this.mGestureFromRightToLeftListener = mGestureFromRightToLeftListener;
    }

    public void setGestureFromTopToBottomListener(GestureFromTopToBottomListener mGestureFromTopToBottomListener) {
        this.mGestureFromTopToBottomListener = mGestureFromTopToBottomListener;
    }

    public void setGestureFromBottomToTopListener(GestureFromBottomToTopListener mGestureFromBottomToTopListener) {
        this.mGestureFromBottomToTopListener = mGestureFromBottomToTopListener;
    }

    private class MyTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.v(TAG, " MyTouchListener " + event.getAction());
            if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
                // avoid multi point action causing confusion
                return true;

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (horizontalActualDirection == GESTURE_FROM_LEFT_TO_RIGHT) {
                    if (mGestureFromLeftToRightListener != null) {
                        mGestureFromLeftToRightListener.onEnd();
                    }
                }
                if (horizontalActualDirection == GESTURE_FROM_RIGHT_TO_LEFT) {
                    if (mGestureFromRightToLeftListener != null) {
                        mGestureFromRightToLeftListener.onEnd();
                    }
                }
                horizontalActualDirection = 0;

                if (verticalActualDirection == GESTURE_FROM_BOTTOM_TO_TOP) {
                    if (mGestureFromBottomToTopListener != null) {
                        mGestureFromBottomToTopListener.onEnd();
                    }
                }
                if (verticalActualDirection == GESTURE_FROM_TOP_TO_BOTTOM) {
                    if (mGestureFromTopToBottomListener != null) {
                        mGestureFromTopToBottomListener.onEnd();
                    }
                }
                verticalActualDirection = 0;

                if (direction == GestureDirection.HORIZONTAL) {

                } else if (direction == GestureDirection.VERTICAL) {

                }

                direction = GestureDirection.NON;

            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (mGestureFromLeftToRightListener != null) {
                    mGestureFromLeftToRightListener.onEnd();
                }

                if (mGestureFromRightToLeftListener != null) {
                    mGestureFromRightToLeftListener.onEnd();
                }

                if (mGestureFromBottomToTopListener != null) {
                    mGestureFromBottomToTopListener.onEnd();
                }

                if (mGestureFromTopToBottomListener != null) {
                    mGestureFromTopToBottomListener.onEnd();
                }
            }

            if (mGestureDetector.onTouchEvent(event)) {
                return true;
            }
            return false;
        }
    }

    private class MySimpleGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean gestureDown = false;

        public MySimpleGestureListener() {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.v(TAG, "onSingleTapUp " + e.getAction());
            if (mGestureClickListener != null) {
                mGestureClickListener.onClick();
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.v(TAG, "onSingleTapConfirmed " + e.getAction());
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.v(TAG, "onDoubleTap " + e.getAction());
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.v(TAG, "onDoubleTapEvent " + e.getAction());
            if (e.getAction() == MotionEvent.ACTION_UP) {

            }
            return super.onDoubleTapEvent(e);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.v(TAG, "onShowPress " + e.getAction());
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.v(TAG, "e1:x=" + e1.getRawX() + ",y=" + e1.getRawY()
                    + ", e2:x=" + e2.getRawX() + ",y=" + e2.getRawY()
                    + ",  distanceX=" + distanceX + ", distanceY=" + distanceY);
            float disX = e2.getRawX() - e1.getRawX();
            float disY = e2.getRawY() - e1.getRawY();
            Log.v(TAG, "X = " + disX + ", Y = " + disY);

            if (gestureDown) {// 1、首先，判定此次手势方向
                Log.v(TAG, "onScroll gestureDown");
                if (Math.abs(disY) - Math.abs(disX) > 1) { // 垂直方向
                    direction = GestureDirection.VERTICAL;
                    if (distanceY > 0) {
                        if (mGestureFromBottomToTopListener != null) {
                            mGestureFromBottomToTopListener.onStart();
                            verticalActualDirection = GESTURE_FROM_BOTTOM_TO_TOP;
                        }
                    }
                    if (distanceY < 0) {
                        if (mGestureFromTopToBottomListener != null) {
                            mGestureFromTopToBottomListener.onStart();
                            verticalActualDirection = GESTURE_FROM_TOP_TO_BOTTOM;
                        }
                    }

                } else if (Math.abs(disX) - Math.abs(disY) > 1) { // 水平方向
                    direction = GestureDirection.HORIZONTAL;
                    if (disX > 0) {
                        if (mGestureFromLeftToRightListener != null) {
                            mGestureFromLeftToRightListener.onStart();
                            horizontalActualDirection = GESTURE_FROM_LEFT_TO_RIGHT;
                        }
                    }
                    if (disX < 0) {
                        if (mGestureFromRightToLeftListener != null) {
                            mGestureFromRightToLeftListener.onStart();
                            horizontalActualDirection = GESTURE_FROM_RIGHT_TO_LEFT;
                        }
                    }
                }
                gestureDown = false;

            } else { // 2、然后，处理

                // 垂直方向
                if (direction == GestureDirection.VERTICAL) {
                    int y = DisplayUtil.px2dip(mContext, (int) disY); // 优化滑动的流畅性
                    if (disY > 0) {
                        if (mGestureFromTopToBottomListener != null) {
                            mGestureFromTopToBottomListener.onScroll(e1, e2);
                        }
                    }
                    if (disY < 0) {
                        if (mGestureFromBottomToTopListener != null) {
                            mGestureFromBottomToTopListener.onScroll(e1, e2);
                        }
                    }

                }
                // 水平
                else if (direction == GestureDirection.HORIZONTAL) {
                    if (disX > 0) {
                        if (mGestureFromLeftToRightListener != null) {
                            mGestureFromLeftToRightListener.onScroll(e1, e2);
                        }
                    }
                    if (disX < 0) {
                        if (mGestureFromRightToLeftListener != null) {
                            mGestureFromRightToLeftListener.onScroll(e1, e2);
                        }
                    }
                }
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.v(TAG, "onDown X=" + e.getX() + ", Y=" + e.getY() + ", rawX=" + e.getRawX() + ", rawY=" + e.getRawY());
            //            int delta = playerControlBar.getHeight();
            //            Log.d(TAG, "PlayerBottomBar.getHeight() " + delta);
            // 设置手势有效区域 if nessesary
            //			if (e.getX() > delta && e.getY() > delta && mPlayerWidth - e.getX() > delta && mPlayerHeight - e.getY() > delta) {
            //				Log.d(TAG, " gesture is at available region");
            gestureDown = true;
            //			} else {
            // any down action will result in setting gestureDown true on the available rect region
            //				gestureDown = false;
            //				Log.d(TAG, " gesture is unavailable ");
            //			}
            return true;
        }
    }

    public interface GestureClickListener {
        void onClick();
    }

    public interface GestureSwipeListener {
        void onStart();

        void onScroll(MotionEvent e1, MotionEvent e2);

        void onEnd();
    }

    public interface GestureFromLeftToRightListener extends GestureSwipeListener {
    }

    public interface GestureFromRightToLeftListener extends GestureSwipeListener {
    }

    public interface GestureFromTopToBottomListener extends GestureSwipeListener {
    }

    public interface GestureFromBottomToTopListener extends GestureSwipeListener {
    }

}

