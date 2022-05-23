package com.bsp.orderbooking.network

import com.bsp.orderbooking.BuildConfig
import com.bsp.orderbooking.util.App
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiAdapter {
    val SERVER_URL: String = if(BuildConfig.DEBUG) "https://api.npoint.io/" else "https://api.npoint.io/"
    var logging = HttpLoggingInterceptor()
    var cacheSize: Long = 10 * 1024 * 1024 // 10 MB

    var cache: Cache = Cache(App.context.cacheDir, cacheSize)

    private val getOkHttpClint: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            builder.cache(cache)
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
            builder.addInterceptor {
                val newRequest = it.request().newBuilder().build()
                it.proceed(newRequest)
            }
            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(logging)
            }
            return builder.build()
        }
    val apiClient: ApiClient =
        Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .client(getOkHttpClint)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)

}