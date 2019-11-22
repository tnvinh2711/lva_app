package com.lva.shop;

import android.content.Context;

import androidx.multidex.MultiDexApplication;


public class MyApplication extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
