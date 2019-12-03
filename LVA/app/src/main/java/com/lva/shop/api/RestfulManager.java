package com.lva.shop.api;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.lva.shop.ui.detail.model.History;
import com.lva.shop.ui.login.model.ModelFacebook;
import com.lva.shop.ui.main.model.Knowledge;
import com.lva.shop.ui.main.model.News;
import com.lva.shop.ui.main.model.Product;
import com.lva.shop.ui.main.model.Tutorial;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class RestfulManager {
    public static final String TAG = RestfulManager.class.getSimpleName();
    private RestfulApi.PlfRestService plfRestService;
    private static RestfulManager INSTANCE;
    private static RestfulManager INSTANCE2;
    private static Activity mActivity;

    public static RestfulManager getInstance(Activity activity) {
        mActivity = activity;
        if (INSTANCE == null) {
            INSTANCE = new RestfulManager();
        }
        return INSTANCE;
    }

    public static RestfulManager getInstance(Activity activity, int base) {
        mActivity = activity;
        if (INSTANCE2 == null) {
            INSTANCE2 = new RestfulManager(base);
        }
        return INSTANCE2;
    }

    private RestfulManager() {
        RestfulApi restfulApi = RestfulApi.getInstance(mActivity);
        plfRestService = restfulApi.getRestService();
    }

    private RestfulManager(int base) {
        RestfulApi restfulApi = RestfulApi.getInstance(mActivity, base);
        plfRestService = restfulApi.getRestService();
    }

    public void getHistory(String phone, OnHistoryListener onHistoryListener) {
        try {
            Observable<History> responseHistoryObservable = plfRestService.getHistory(phone);
            responseHistoryObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<History>() {
                        History history;

                        @Override
                        public void onNext(History resHistory) {
                            history = resHistory;
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (onHistoryListener != null) onHistoryListener.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            if (history != null) {
                                if (onHistoryListener != null)
                                    onHistoryListener.onGetHistorySuccess(history);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postLogin(String phone, String uid, ModelFacebook modelFacebook, OnLoginListener onLoginListener) {
        try {
            Observable<ResponseBody> responseLoginObservable = plfRestService.postLogin(phone, uid, modelFacebook.getDisplay_name(), modelFacebook.getPhoto_url(), modelFacebook.getEmail());
            responseLoginObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<ResponseBody>() {
                        ResponseBody resLogin;

                        @Override
                        public void onNext(ResponseBody res) {
                            resLogin = res;
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (onLoginListener != null) onLoginListener.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            if (resLogin != null) {
                                if (onLoginListener != null)
                                    onLoginListener.onLoginSuccess(resLogin);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getHome(OnChangeListener onChangeListener) {
        Observable<ZipRequest> zipObservable = Observable.zip(createListObservable(), objects -> new ZipRequest((Knowledge) objects[0], (Tutorial) objects[1], (News) objects[2], (Product) objects[3]));
        zipObservable.subscribe(new Observer<ZipRequest>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ZipRequest zipRequest) {
                if (onChangeListener != null) onChangeListener.onGetZipSuccess(zipRequest);
            }

            @Override
            public void onError(Throwable e) {
                if (onChangeListener != null) onChangeListener.onError(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private List<Observable<?>> createListObservable() {
        Observable<Knowledge> knowledgeObservable = plfRestService
                .getKnowledge()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<Tutorial> tutorialObservable = plfRestService
                .getTutorial()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<News> newsObservable = plfRestService
                .getNews()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<Product> productObservable = plfRestService
                .getProduct()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        List<Observable<?>> result = new ArrayList<>();
        result.add(knowledgeObservable);
        result.add(tutorialObservable);
        result.add(newsObservable);
        result.add(productObservable);
        return result;
    }

    public interface OnChangeListener {
        void onGetZipSuccess(ZipRequest zipRequest);

        void onError(Throwable e);

    }

    public interface OnLoginListener {
        void onLoginSuccess(ResponseBody responseBody);

        void onError(Throwable e);

    }

    public interface OnHistoryListener {
        void onGetHistorySuccess(History history);

        void onError(Throwable e);

    }
}
