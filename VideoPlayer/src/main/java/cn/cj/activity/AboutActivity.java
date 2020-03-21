package cn.cj.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import cn.cj.media.video.player.R;
import cn.cj.util.Util;

public class AboutActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");
        textView = (TextView) findViewById(R.id.ver_name);
        textView.setText("版本：" + Util.getVersionName(getApplicationContext()));
    }

}
