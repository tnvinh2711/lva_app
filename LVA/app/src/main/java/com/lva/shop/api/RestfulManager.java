package com.lva.shop.api;

import android.app.Activity;

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

public class RestfulManager {
    public static final String TAG = RestfulManager.class.getSimpleName();
    private RestfulApi.PlfRestService plfRestService;
    private static RestfulManager INSTANCE;
    private static Activity mActivity;

    public static RestfulManager getInstance(Activity activity) {
        mActivity = activity;
        if (INSTANCE == null) {
            INSTANCE = new RestfulManager();
        }
        return INSTANCE;
    }

    private RestfulManager() {
        RestfulApi restfulApi = RestfulApi.getInstance(mActivity);
        plfRestService = restfulApi.getRestService();
    }

//    public void getOrder(OnChangeListener onChangeListener) {
//        try {
//            Observable<Order> responseUserInfoObservable = plfRestService.getOrder();
//            responseUserInfoObservable.subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new DisposableObserver<Order>() {
//                        Order resOrder;
//
//                        @Override
//                        public void onNext(Order responseOrder) {
//                            resOrder = responseOrder;
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            if (onChangeListener != null) onChangeListener.onError(e);
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            if (resOrder != null) {
//                                if (onChangeListener != null)
//                                    onChangeListener.onGetOrderSuccess(resOrder);
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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

//        void onGetOrderSuccess(Order resOrder);
    }
}
