package com.woodyhi.playlist

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import com.woodyhi.player.base.PlayInfo
import com.woodyhi.player.internal.DefaultControllerView
import com.woodyhi.player.vlc.VlcPlayerManager
import com.woodyhi.player.widget.PlayerView

class VideoPlayerActivity : AppCompatActivity() {
    internal var rtmp = "rtmp://live.hkstv.hk.lxdns.com/live/hks1"

    private var systemUiVisibilityWhenPortrait: Int = 0

    private var manager1: VlcPlayerManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // 设置状态栏全透明
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
        systemUiVisibilityWhenPortrait = window.decorView.systemUiVisibility

        val path = intent.dataString

        val playerView1 = findViewById<PlayerView>(R.id.playerView)
        manager1 = VlcPlayerManager(this)
        playerView1.setPlayerManager(manager1)
        val controllerView1 = DefaultControllerView(this)
        controllerView1.setPlayerManger(manager1)
        playerView1.controllerView = controllerView1
        manager1!!.play(PlayInfo(if (path == null) rtmp else path))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val activity = this
        when (newConfig.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                activity.window.decorView.systemUiVisibility = systemUiVisibilityWhenPortrait
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                activity.window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LOW_PROFILE
                                or View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        manager1?.play()
    }

    override fun onPause() {
        super.onPause()
        manager1?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        manager1?.release()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0 && !com.woodyhi.player.base.Util.isPortrait(this)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
