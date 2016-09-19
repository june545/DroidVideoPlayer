/**
 * 
 */
package cn.cj.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * @author June Cheng
 * @date 2015年11月5日 上午12:31:29
 */
public class BaseFragmentActivity extends FragmentActivity {
	private NetworkBroadcastReceiver	mNetworkReceiver;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	public void registerNetworkReceiver() {
		if (mNetworkReceiver == null) {
			mNetworkReceiver = new NetworkBroadcastReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			registerReceiver(mNetworkReceiver, filter);
		}
	}

	public void unregisterNetworkReceiver() {
		if (mNetworkReceiver != null) {
			unregisterReceiver(mNetworkReceiver);
		}
	}

	public void onNetworkChanged(boolean available) {

	}

	class NetworkBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
				Log.i("TAG", "unconnect");
				// unconnect network
				onNetworkChanged(false);
			} else {
				onNetworkChanged(true);
				// connect network
			}
		}

	}
}
