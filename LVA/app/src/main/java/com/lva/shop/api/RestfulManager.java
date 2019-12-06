package com.lva.shop.api;

import android.app.Activity;

import com.lva.shop.ui.detail.model.History;
import com.lva.shop.ui.detail.model.ProductOrder;
import com.lva.shop.ui.login.model.Login;
import com.lva.shop.ui.login.model.ResponseUser;
import com.lva.shop.ui.main.model.Knowledge;
import com.lva.shop.ui.main.model.News;
import com.lva.shop.ui.main.model.Product;
import com.lva.shop.ui.main.model.Tutorial;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.Preference;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class RestfulManager {
    public static final String TAG = RestfulManager.class.getSimpleName();
    private RestfulApi.PlfRestService plfRestService;
    private static RestfulManager INSTANCE;
    private static RestfulManager INSTANCE2;
    private static RestfulManager INSTANCE3;
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

    public void getUserInfo(String phone, String token, OnGetUserListener onGetUserListener) {
        try {
            Observable<ResponseUser> responseUserObservable = plfRestService.getUserInfo(phone, token);
            responseUserObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<ResponseUser>() {
                        ResponseUser responseUser;

                        @Override
                        public void onNext(ResponseUser responseUser) {
                            this.responseUser = responseUser;
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (onGetUserListener != null) onGetUserListener.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            if (responseUser != null) {
                                if (onGetUserListener != null)
                                    onGetUserListener.onGetUserSuccess(responseUser);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postUpdateUser(String name_delivery, String phone_delivery, String address, String province, String district, String ward, OnGetUserListener onGetUserListener) {
        try {
            String phone = Preference.getString(mActivity, AppConstants.PHONE);
            String token = Preference.getString(mActivity, AppConstants.ACCESS_TOKEN);
            Observable<ResponseUser> responseUserObservable = plfRestService.updateUserInfo(phone, token, name_delivery, phone_delivery, address, province, district, ward);
            responseUserObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<ResponseUser>() {
                        ResponseUser responseUser;

                        @Override
                        public void onNext(ResponseUser responseUser) {
                            this.responseUser = responseUser;
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (onGetUserListener != null) onGetUserListener.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            if (responseUser != null) {
                                if (onGetUserListener != null)
                                    onGetUserListener.onGetUserSuccess(responseUser);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void postUpdateUser(String name, Integer dob_d, Integer dob_m, Integer dob_y, OnGetUserListener onGetUserListener) {
        try {
            String phone = Preference.getString(mActivity, AppConstants.PHONE);
            String token = Preference.getString(mActivity, AppConstants.ACCESS_TOKEN);
            Observable<ResponseUser> responseUserObservable = plfRestService.updateUserInfo(phone, token, name, dob_d, dob_m, dob_y);
            responseUserObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<ResponseUser>() {
                        ResponseUser responseUser;

                        @Override
                        public void onNext(ResponseUser responseUser) {
                            this.responseUser = responseUser;
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (onGetUserListener != null) onGetUserListener.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            if (responseUser != null) {
                                if (onGetUserListener != null)
                                    onGetUserListener.onGetUserSuccess(responseUser);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void postUpdateUser(RequestBody file, OnGetUserListener onGetUserListener) {
        try {
            String phone = Preference.getString(mActivity, AppConstants.PHONE);
            String token = Preference.getString(mActivity, AppConstants.ACCESS_TOKEN);
            Observable<ResponseUser> responseUserObservable = plfRestService.updateUserInfo(phone, token, file);
            responseUserObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<ResponseUser>() {
                        ResponseUser responseUser;

                        @Override
                        public void onNext(ResponseUser responseUser) {
                            this.responseUser = responseUser;
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (onGetUserListener != null) onGetUserListener.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            if (responseUser != null) {
                                if (onGetUserListener != null)
                                    onGetUserListener.onGetUserSuccess(responseUser);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postLogin(String phone, String uid, OnLoginListener onLoginListener) {
        try {
            Observable<Login> responseLoginObservable = plfRestService.postLogin(phone, uid);
            responseLoginObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Login>() {
                        Login resLogin;

                        @Override
                        public void onNext(Login res) {
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

    public void postOrder(List<ProductOrder> productList, OnPostOrderListener onPostOrderListener) {
        try {
            String phone = Preference.getString(mActivity, AppConstants.PHONE);
            String token = Preference.getString(mActivity, AppConstants.ACCESS_TOKEN);
            Observable<ResponseBody> responseLoginObservable = plfRestService.postOrder(phone, token, productList);
            responseLoginObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<ResponseBody>() {

                        @Override
                        public void onNext(ResponseBody res) {
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (onPostOrderListener != null) onPostOrderListener.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            if (onPostOrderListener != null)
                                onPostOrderListener.onPostOrderSuccess();
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
        void onLoginSuccess(Login responseBody);

        void onError(Throwable e);

    }

    public interface OnHistoryListener {
        void onGetHistorySuccess(History history);

        void onError(Throwable e);

    }

    public interface OnGetUserListener {
        void onGetUserSuccess(ResponseUser responseUser);

        void onError(Throwable e);

    }

    public interface OnPostOrderListener {
        void onPostOrderSuccess();

        void onError(Throwable e);

    }
}
