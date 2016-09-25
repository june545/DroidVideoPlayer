package cn.cj.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.cj.media.video.player.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button online;
    Button local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        online = (Button) findViewById(R.id.online);
        local = (Button) findViewById(R.id.local);
        online.setOnClickListener(this);
        local.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.online:
                startActivity(new Intent(getApplicationContext(), OnlineVideoActivity.class));
                break;
            case R.id.local:
                startActivity(new Intent(getApplicationContext(), LocalVideoActivity.class));
                break;
        }
    }
}
