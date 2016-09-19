/**
 * 
 */
package cn.cj.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.cj.media.video.fragment.VideoPlayerFragment;
import cn.cj.media.video.player.R;
import cn.cj.util.NetworkUtil;

/**
 * @author June Cheng
 * @date 2015年10月28日 下午10:39:13
 */
public class VideoPlayerActivity extends BaseFragmentActivity {
	private final String		TAG						= "MediaPlayerActivity";

	private LinearLayout		mMediaPlayerContainer;
	private Button				animBtn;

	private VideoPlayerFragment mMediaPlayerFragment	= null;

	private String				mMediaPath;
	private long 				mPlayedTime;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_fragment_media_player);
		parseIntent();
		initView();
		initFragment();
		registerNetworkReceiver();
	}

	private void parseIntent() {
		Intent intent = getIntent();
		String action = intent.getAction();
		if (Intent.ACTION_VIEW.equals(action)) {
			String path = intent.getDataString();
			mMediaPath = path;
			Log.d(TAG, "action path -> " + path);
		} else {
			String path = intent.getDataString();
			if(path != null){
				mMediaPath = path;
				
			}else{
				Bundle bundle = intent.getExtras();
				if(bundle != null){
					mMediaPath = bundle.getString("playurl", null);
					mPlayedTime = bundle.getLong("playedtime", 0);	
				}
			}
			
			Log.d(TAG, "url path -> " + path + ", playedtime=" + mPlayedTime + "(ms)");
		}
	}

	private void initView() {
		mMediaPlayerContainer = (LinearLayout) findViewById(R.id.media_player_container);
		animBtn = (Button) findViewById(R.id.anim_btn);
		animBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String path = "http://lteby1.tv189.com/mobi/vod/ts01/st02/2015/09/21/Q350_2022243504/Q350_2022243504.3gp.m3u8?sign=C33EE726E53A8D95858523574C2B84A7&tm=56057b32&vw=2&ver=v1.1&version=1&app=115020310073&cookie=56057816445f1&session=56057816445f1&uid=104318501709339150304&uname=18501709339&time=20150926004954&videotype=2&cid=C36800735&cname=&cateid=&dev=000001&ep=710&etv=&os=30&ps=0099&clienttype=GT-I9500&deviceid=null&appver=5.2.19.7&res=1080%2A1920&channelid=059998&pid=1000000228&orderid=1100313262678&nid=&netype=11&isp=&cp=00000236&sp=00000014&ip=180.159.137.20&ipSign=19458e98472996c9f9776d8434dd5bb1&guid=2c149261-db0d-5d70-20bf-ee161f8edba6&cdntoken=api_56057b3275843";
				mMediaPlayerFragment.next(path);
			}
		});
	}

	private void initFragment() {
		mMediaPlayerFragment = new VideoPlayerFragment();
		mMediaPlayerFragment.setMediaPath(mMediaPath, (int) mPlayedTime);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.media_player_container, mMediaPlayerFragment, mMediaPlayerFragment.getClass().getSimpleName());
		ft.commit();
		Log.d(TAG, "init VideoPlayerFragment and add it to activity");
	}

	public void m() {
		boolean b = NetworkUtil.isNetworkConnected(getApplicationContext());
		TextView tv = (TextView) findViewById(R.id.info);
		String s = tv.getText().toString() + "\n" + "network is availbale : " + b;
		tv.setText(s);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mMediaPlayerFragment.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onNetworkChanged(boolean available) {
		TextView tv = (TextView) findViewById(R.id.info);
		String s = tv.getText().toString() + "\n" + (available ? "网络连接正常" : "网络连接不可用");
		tv.setText(s);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterNetworkReceiver();
	}
}
