package com.jaengineer.kitjoin.Controllers;

import android.util.Log;

import com.jaengineer.kitjoin.chat.init;

import java.util.HashMap;

/**
 * Created by Jose Alb on 08/09/2015.
 */

//Determina el idioma del dispositivo
public class LocaleController {

    private static String TAG = "LOCALE_CONTROLLER";

    public static boolean isRTL = false;

    private HashMap<String, String> localeValues = new HashMap<>();
    private static volatile LocaleController Instance = null;



    public static LocaleController getInstance() {

        LocaleController localInstance = Instance;

        if (localInstance == null) {
            synchronized (LocaleController.class) {

                localInstance = Instance;

                if (localInstance == null) {
                    Instance = localInstance = new LocaleController();
                }
            }
        }
        return localInstance;
    }

    private String getStringInternal(String key, int res) {
        String value = localeValues.get(key);
        if (value == null) {
            value = init.applicationContext.getString(res);
        }
        if (value == null) {
            value = "LOC_ERR:" + key;
        }

        Log.d(TAG, "TEXT: " + TAG + "GET_STRING_INTERNAL, value: "+value);
        return value;
    }

    public static String getString(String key, int res) {
        Log.d(TAG, "TEXT: " + TAG + "GET_STRING: "+ getInstance().getStringInternal(key, res));
        return getInstance().getStringInternal(key, res);
    }
}
