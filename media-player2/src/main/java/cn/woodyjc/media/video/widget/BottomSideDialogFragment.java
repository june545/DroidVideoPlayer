package cn.woodyjc.media.video.widget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import cn.woodyjc.media.video.R;


/**
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
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                |WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(lp);
    }

}
