package com.woodyhi.playlist.api

import android.content.Context
import com.woodyhi.playlist.BaseApplication
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Objects

/**
 * @auth June
 * @date 2019/06/18
 */
class ApiManager private constructor() {
    private val retrofit: Retrofit
    var apiService: ApiService? = null
        get() {
            if (field == null) field = retrofit.create(ApiService::class.java)
            return field
        }
        private set

    companion object {
        val instance: ApiManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiManager()
        }
    }

    init {
        val context: Context = BaseApplication.application
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
                .cache(Cache(Objects.requireNonNull(context.externalCacheDir), 1024 * 1024))
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor { chain: Interceptor.Chain ->
                    val response = chain.proceed(chain.request())
                    response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + 3600 * 24)
                            .build()
                }
                .build()
        retrofit = Retrofit.Builder()
                .baseUrl("http://api.m.mtime.cn")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }
}