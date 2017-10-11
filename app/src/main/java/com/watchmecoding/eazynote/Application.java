package com.watchmecoding.eazynote;

import android.util.Log;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("SHumbul", "onCreate: " + "app is initiated");

    }
}
