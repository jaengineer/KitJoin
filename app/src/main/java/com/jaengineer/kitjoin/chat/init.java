package com.jaengineer.kitjoin.chat;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.jaengineer.kitjoin.Controllers.LoadNavtiveLibs;

/**
 * Created by Jose Alb on 06/09/2015.
 */
public class init  extends Application {

    private static String TAG = "INIT";

    public static volatile Context applicationContext;

    public static volatile Handler applicationHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "TEXT: " + TAG + " ONCREATE");
        applicationContext = getApplicationContext();
        LoadNavtiveLibs.initNativeLibs(init.applicationContext);

        applicationHandler = new Handler(applicationContext.getMainLooper());

    }

    public static void InitApplication() {

    }
}

