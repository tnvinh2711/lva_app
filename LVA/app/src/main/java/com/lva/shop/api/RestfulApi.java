package com.lva.shop.api;

import android.content.Context;

import com.lva.shop.ui.main.model.Knowledge;
import com.lva.shop.ui.main.model.News;
import com.lva.shop.ui.main.model.Product;
import com.lva.shop.ui.main.model.Tutorial;
import com.lva.shop.utils.NetworkUtils;
import com.lva.shop.utils.NoConnectException;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RestfulApi {
    private static final String TAG = RestfulApi.class.getSimpleName();
    private final String API_BASE_URL = "http://levananh.com/api/";

    private static RestfulApi INSTANCE;

    private PlfRestService restService;
    private static Retrofit retrofit;
    private static Context mContext;

    public static RestfulApi getInstance(Context context) {
        mContext = context;
        if (INSTANCE == null) {
            INSTANCE = new RestfulApi();
        }
        return INSTANCE;
    }

    private RestfulApi() {
        try {
            OkHttpClient httpClient = getCommonClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient)
                    .build();
            restService = retrofit.create(PlfRestService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlfRestService getRestService() {
        return restService;
    }

    private OkHttpClient getCommonClient() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    if (!NetworkUtils.isNetworkConnected(mContext)) {
                        throw new NoConnectException();
                    }
                    Request.Builder builder = chain.request()
                            .newBuilder();
                    Request request = builder.build();
                    return chain.proceed(request);
                })
                .addInterceptor(logging)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public interface PlfRestService {

        @GET("blog")
        Observable<Knowledge> getKnowledge();

        @GET("video")
        Observable<Tutorial> getTutorial();

        @GET("new")
        Observable<News> getNews();

        @GET("product")
        Observable<Product> getProduct();
    }

}
