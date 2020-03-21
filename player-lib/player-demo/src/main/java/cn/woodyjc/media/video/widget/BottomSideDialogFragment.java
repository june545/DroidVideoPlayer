package cn.woodyjc.media.video.widget;

import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import cn.woodyjc.media.video.R;


/**
 * DialogFragment位于屏幕底部
 *
 * Created by June on 2018/4/27.
 */
public class BottomSideDialogFragment extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(null);// ？在android8.0中
        window.setWindowAnimations(R.style.bottomSideDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.windowAnimations = R.style.bottomSideDialogAnim;
        lp.dimAmount = 0F;
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL/*Dialog外触摸事件被后面view消费*/
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION/*隐藏导航栏*/
                |WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;/*状态栏背景透明（可能手机系统是半透明）*/
        window.setAttributes(lp);
    }

}
