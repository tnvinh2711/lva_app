package com.lva.shop.api;

import android.content.Context;

import com.lva.shop.ui.detail.model.History;
import com.lva.shop.ui.login.model.ModelFacebook;
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
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class RestfulApi {
    private static final String TAG = RestfulApi.class.getSimpleName();
    private final String API_BASE_URL = "http://levananh.com/api/";
    private final String API_BASE_URL_2 = "http://online.levananh.com/api/";

    private static RestfulApi INSTANCE;
    private static RestfulApi INSTANCE2;

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

    public static RestfulApi getInstance(Context context, int base) {
        mContext = context;
        if (INSTANCE2 == null) {
            INSTANCE2 = new RestfulApi(base);
        }
        return INSTANCE2;
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

    private RestfulApi(int base) {
        try {
            OkHttpClient httpClient = getCommonClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL_2)
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

        @GET("history")
        Observable<History> getHistory(
                @Query("phone") String phone
        );

        @POST("login")
        Observable<ResponseBody> postLogin(
                @Query("phone") String phone,
                @Query("uid") String uid,
                @Query("display_name") String display_name,
                @Query("photo_url") String photo_url,
                @Query("email") String email
        );

    }

}
