package com.uuch.adlibrary;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by aaron on 16/8/2.
 * 自定义Application
 */
public class LApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
