package com.appspace.evybook;

import android.app.Application;

import com.appspace.appspacelibrary.manager.Contextor;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import io.fabric.sdk.android.Fabric;

/**
 * Created by siwaweswongcharoen on 8/8/2016 AD.
 */
public class EvyBookApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        
        Fabric.with(this, new Crashlytics());

        // Init Contextor
        Contextor.getInstance().init(getApplicationContext());
        // Control Log
        LoggerUtils.getInstance().setLogEnabled(true);
    }
}
