package com.jaengineer.kitjoin.Utils;

import android.app.Activity;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.UpdateManager;

/**
 * Created by Jose Alb on 09/09/2015.
 */
public class hockeyapp {

    public static void register(Activity context) {
        CrashManager.register(context, BuildParams.HOCKEY_APP_HASH, new CrashManagerListener() {
            @Override
            public boolean includeDeviceData() {
                return true;
            }
        });
    }

    public static void updates(Activity context) {
        if (BuildParams.DEBUG_VERSION) {
            UpdateManager.register(context, BuildParams.HOCKEY_APP_HASH);
        }
    }
}
