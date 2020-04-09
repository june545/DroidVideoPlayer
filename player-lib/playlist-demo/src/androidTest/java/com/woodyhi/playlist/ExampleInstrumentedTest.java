package com.woodyhi.playlist;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.woodyhi.playlist.model.Trailer;
import com.woodyhi.playlist.model.TrailerListData;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.woodyhi.playlist", appContext.getPackageName());
    }

    @Test
    public void testLiveDataCall() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(Objects.requireNonNull(context.getExternalCacheDir()), 1024 * 1024))
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(chain -> {
                    Response response = chain.proceed(chain.request());
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + 3600 * 24)
                            .build();
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.m.mtime.cn")
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        LiveData<TrailerListData> liveData = retrofit.create(BackendService.class).getTrailersData();
        LiveData<List<Trailer>> b = Transformations.switchMap(liveData, input -> {
            MutableLiveData<List<Trailer>> m = new MutableLiveData<>();
            m.setValue(input.getTrailers());
            return m;
        });
        new Handler(Looper.getMainLooper()).post(() ->
                b.observeForever(trailers ->
                        trailers.forEach(trailer ->
                                System.out.println(trailer.getVideoTitle()))));
    }
}
