package com.woodyhi.playlist

import android.app.Application

class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        application = this
    }


    companion object {
        lateinit var application: BaseApplication;
    }

}