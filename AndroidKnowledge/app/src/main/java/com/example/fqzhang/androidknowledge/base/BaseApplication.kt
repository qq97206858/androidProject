package com.example.fqzhang.androidknowledge.base

import android.app.Application
import android.content.ComponentCallbacks2
import com.bumptech.glide.Glide
import com.example.fqzhang.androidknowledge.BuildConfig
import com.squareup.leakcanary.LeakCanary

/**
 * Created by fqzhang on 2018/4/6.
 */
class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this)
        }
        Preference.setContext(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }
}