package cn.cj.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import cn.cj.media.video.player.R;


public class OnlineVideoActivity extends AppCompatActivity implements OnClickListener {

	private EditText	editText;
	private Button		play;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online_video);

		editText = (EditText) findViewById(R.id.edit);
		play = (Button) findViewById(R.id.play);

		play.setOnClickListener(this);
		findViewById(R.id.btn).setOnClickListener(this);
		findViewById(R.id.zjtv).setOnClickListener(this);

		editText.setText(LocalStore.getUrl(getApplicationContext()));
		editText.setText("http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play:
			String s = editText.getText().toString();
			if (!TextUtils.isEmpty(s)) {
				play(s);
			}
			break;
		case R.id.btn:
//			String path = "http://lteby1.tv189.com/mobi/vod/ts01/st02/2015/09/21/Q350_2022243504/Q350_2022243504.3gp.m3u8?sign=C33EE726E53A8D95858523574C2B84A7&tm=56057b32&vw=2&ver=v1.1&version=1&app=115020310073&cookie=56057816445f1&session=56057816445f1&uid=104318501709339150304&uname=18501709339&time=20150926004954&videotype=2&cid=C36800735&cname=&cateid=&dev=000001&ep=710&etv=&os=30&ps=0099&clienttype=GT-I9500&deviceid=null&appver=5.2.19.7&res=1080%2A1920&channelid=059998&pid=1000000228&orderid=1100313262678&nid=&netype=11&isp=&cp=00000236&sp=00000014&ip=180.159.137.20&ipSign=19458e98472996c9f9776d8434dd5bb1&guid=2c149261-db0d-5d70-20bf-ee161f8edba6&cdntoken=api_56057b3275843";
			String path = "http://182.138.48.196:6060/ZTE_CMS/20000000000000000000000000004334.mp4??IASHttpSessionId=SLB2565320151224120012249783&ismp4=1";
			play(path);
			break;

		case R.id.zjtv:
			String url = "http://lteby1.tv189.com/mobi/vod/st02/2015/10/22/Q350_2023016068.3gp?sign=BA1E0C2FF09BF79BBC9B64A23FCF8E86&tm=56336d64&vw=2&ver=v1.1&version=1&app=115020310073&cookie=56336d53e6a1e&session=56336d53e6a1e&uid=104318501709339150304&uname=18501709339&time=20151030211516&videotype=2&cid=C36944646&cname=&cateid=&dev=000001&ep=1&etv=&os=30&ps=0099&clienttype=GT-I9500&deviceid=null&appver=5.2.29.7&res=1080%2A1920&channelid=01994720&pid=1000000228&orderid=1100329631243&nid=&netype=11&isp=&cp=00000175&sp=00000014&ip=180.158.238.61&ipSign=e6bae705d6dd83db8e0a81815f604502&guid=5a7e0875-2b1e-5fc7-30ce-5dd4d803bb60&cdntoken=api_56336d64444c6&qualityCode=484";
			play(url);
			break;
		}
	}

	private void play(String path) {
		Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
		intent.setDataAndType(Uri.parse(path), "video/*");
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalStore.saveUrl(getApplicationContext(), editText.getText().toString());
	}

}
