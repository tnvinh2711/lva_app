package com.lva.shop.api;

import android.content.Context;

import com.lva.shop.ui.detail.model.History;
import com.lva.shop.ui.login.model.Login;
import com.lva.shop.ui.login.model.ResponseUser;
import com.lva.shop.ui.main.model.Knowledge;
import com.lva.shop.ui.main.model.News;
import com.lva.shop.ui.main.model.Product;
import com.lva.shop.ui.main.model.Tutorial;
import com.lva.shop.utils.NetworkUtils;
import com.lva.shop.utils.NoConnectException;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class RestfulApi {
    private static final String TAG = RestfulApi.class.getSimpleName();
    private final String API_BASE_URL = "http://levananh.com/api/";
    private final String API_BASE_URL_2 = "http://online.levananh.com/api/";

    private static RestfulApi INSTANCE;
    private static RestfulApi INSTANCE2;
    private static RestfulApi INSTANCE3;

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

    public static RestfulApi getInstance(Context context, boolean isMultiPart) {
        mContext = context;
        if (INSTANCE3 == null) {
            INSTANCE3 = new RestfulApi(isMultiPart);
        }
        return INSTANCE3;
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

    private RestfulApi(boolean isMultiPart) {
        try {
            OkHttpClient httpClient = getCommonClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL_2)
                    .addConverterFactory(ScalarsConverterFactory.create())
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

        @GET("getUserInfo")
        Observable<ResponseUser> getUserInfo(
                @Query("phone") String phone,
                @Query("token") String token
        );

        @Multipart
        @POST("updateUserInfo")
        Observable<ResponseUser> updateUserInfo(
                @Query("phone") String phone,
                @Query("token") String token,
                @Part("name_delivery") String name_delivery,
                @Part("phone_delivery") String phone_delivery,
                @Part("address") String address,
                @Part("province") String province,
                @Part("district") String district,
                @Part("ward") String ward,
                @Part("name") String name,
                @Part("dob_d") Integer dob_d,
                @Part("dob_m") Integer dob_m,
                @Part("dob_y") Integer dob_y,
                @Part("image\"; filename=\"pp.png\" ") RequestBody file
                );

        @GET("history")
        Observable<History> getHistory(
                @Query("phone") String phone
        );

        @POST("login")
        Observable<Login> postLogin(
                @Query("phone") String phone,
                @Query("uid") String uid
        );

    }

}
