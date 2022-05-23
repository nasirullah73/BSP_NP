package com.bsp.orderbooking.util

import android.app.Application
import android.content.Context
import android.net.http.HttpResponseCache
import android.util.Log
import java.io.File
import java.io.IOException

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        myApp = this

        try {
            val httpCacheDir = File(cacheDir, "eTBC")
            val httpCacheSize = 10 * 1024 * 1024.toLong() // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize)
        } catch (e: IOException) {
            Log.i(TAG, "HTTP response cache installation failed:$e")
        }
    }

    companion object {
        private val TAG = App::class.java.simpleName
        private var myApp: Application? = null
        val context: Context get() = myApp!!.applicationContext
    }
}