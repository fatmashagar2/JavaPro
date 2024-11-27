package com.example.finalprogectxo;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // تهيئة SoundManager
        SoundManager.getInstance(this);
    }
}
