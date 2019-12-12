package com.lva.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.multidex.MultiDexApplication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lva.shop.ui.detail.model.NtfModel;
import com.lva.shop.ui.login.LoginActivity;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.Preference;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MyApplication extends MultiDexApplication implements LifecycleObserver {
    String className;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupActivityListener();
        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new NotificationReceivedHandler())
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    private class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            NtfModel ntfModel = new NtfModel();
            JSONObject data = notification.payload.additionalData;
            ntfModel.setTitle(notification.payload.title);
            ntfModel.setContent(notification.payload.body);
            ntfModel.setTime(System.currentTimeMillis());
            ntfModel.setClick(false);
            if (data.has("url")) {
                try {
                    ntfModel.setUrl(data.getString("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            List<NtfModel> ntfModelList = new ArrayList<>();

            if (Preference.getString(getApplicationContext(), AppConstants.NOTI) != null) {
                String arrayListNoti = Preference.getString(getApplicationContext(), AppConstants.NOTI);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<NtfModel>>() {
                }.getType();
                if (gson.fromJson(arrayListNoti, type) != null) {
                    ntfModelList.addAll(gson.fromJson(arrayListNoti, type));
                }
            }
            ntfModelList.add(0, ntfModel);
            Gson gson = new Gson();
            String ntfModelJson = gson.toJson(ntfModelList);
            Preference.save(getApplicationContext(), AppConstants.NOTI, ntfModelJson);
            Intent intent = new Intent(AppConstants.INTENT_FILTER_FROM_NOTI);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }

    private class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            Object activityToLaunch = null;
            if (className == null) {
                activityToLaunch = LoginActivity.class;
            } else {
                try {
                    activityToLaunch = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                className = activity.getClass().getName();
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }
}
