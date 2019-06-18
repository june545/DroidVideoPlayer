package com.woodyhi.playlist

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.woodyhi.player.base.PlaybackInfo
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
        (manager1 as VlcPlayerManager).setSurfaceView(playerView1.surfaceView)
        playerView1.setPlayerManager(manager1)
        val controllerView1 = DefaultControllerView(this)
        controllerView1.setPlayerManger(manager1)
        playerView1.controllerView = controllerView1
        manager1!!.playback(PlaybackInfo(if (path == null) rtmp else path))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val activity = this
        when (newConfig.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                activity.window.decorView.systemUiVisibility = systemUiVisibilityWhenPortrait
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        manager1?.release()
    }
}
