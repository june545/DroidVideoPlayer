package com.woodyhi.playlist.api;

import android.content.Context;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @auth June
 * @date 2019/06/18
 */
public class ApiManager {

    static ApiManager apiManager;
    Context context;
    ApiService apiService;

    private ApiManager(Context context) {
        this.context = context;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(context.getExternalCacheDir(), 1024 * 1024))
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(chain -> {
                    Response response = chain.proceed(chain.request());
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=3600")
                            .build();
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.m.mtime.cn")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        apiService = retrofit.create(ApiService.class);
    }


    public static ApiManager getInstance(Context context) {
        if (apiManager == null) {
            apiManager = new ApiManager(context);
        }
        return apiManager;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
