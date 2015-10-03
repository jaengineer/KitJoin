package com.jaengineer.kitjoin.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jaengineer.kitjoin.Modules.TypefaceSpan;
import com.jaengineer.kitjoin.R;
import com.jaengineer.kitjoin.chat.init;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Jose Alb on 08/09/2015.
 */
public class Utilities {

    private static String TAG = "UTILITIES";

    public static final int FLAG_TAG_BR = 1;
    public static final int FLAG_TAG_BOLD = 2;
    public static final int FLAG_TAG_COLOR = 4;
    public static final int FLAG_TAG_ALL = FLAG_TAG_BR | FLAG_TAG_BOLD | FLAG_TAG_COLOR;

    public static float density = 1;
    public static int statusBarHeight = 0;
    private static Boolean isTablet = null;

    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();

    public static DisplayMetrics displayMetrics = new DisplayMetrics();

    static {
        density = init.applicationContext.getResources().getDisplayMetrics().density;

    }

    public static float getPixelsInCM(float cm, boolean isX) {
        return (cm / 2.54f) * (isX ? displayMetrics.xdpi : displayMetrics.ydpi);
    }

    /******* devuelve el texto de una cadena html *************/
    public static Spannable replaceTags(String stringhtml) {

        //FLAG_TAG_ALL = 7
        return replaceTags(stringhtml, FLAG_TAG_ALL);
    }

    public static Spannable replaceTags(String strhtml, int flag) {
        //str: texto con etiquetas html

        try {
            int start;
            int end;
            //String: objeto que una vez instanciado no se puede cambiar su estado.
            //StringBuilder: secuencia de carácteres modificable: su contenido y capacidad puede variar.
            StringBuilder stringBuilder = new StringBuilder(strhtml);

            if ((flag & FLAG_TAG_BR) != 0) {

                while ((start = stringBuilder.indexOf("<br>")) != -1) {
                    stringBuilder.replace(start, start + 4, "\n");

                }
                while ((start = stringBuilder.indexOf("<br/>")) != -1) {
                    stringBuilder.replace(start, start + 5, "\n");//Extrae la cadena hasta </br> e incorpora salto de línea
                }
            }
            ArrayList<Integer> bolds = new ArrayList<>();
            if ((flag & FLAG_TAG_BOLD) != 0) {//si hay cadenas en negrita
               ;
                while ((start = stringBuilder.indexOf("<b>")) != -1) {//mientras encuentre etiqueta <b>

                    stringBuilder.replace(start, start + 3, "");
                    end = stringBuilder.indexOf("</b>");

                    if (end == -1) {
                        end = stringBuilder.indexOf("<b>");
                    }

                    stringBuilder.replace(end, end + 4, "");//elimina las etiquetas html de negrita

                    bolds.add(start);
                    bolds.add(end);

                }
            }
            ArrayList<Integer> colors = new ArrayList<>();
            if ((flag & FLAG_TAG_COLOR) != 0) {
                Log.d(TAG, "TEXT: " + TAG + " COLOR ");
                while ((start = stringBuilder.indexOf("<c#")) != -1) {
                    stringBuilder.replace(start, start + 2, "");
                    end = stringBuilder.indexOf(">", start);
                    int color = Color.parseColor(stringBuilder.substring(start, end));
                    stringBuilder.replace(start, end + 1, "");
                    end = stringBuilder.indexOf("</c>");
                    stringBuilder.replace(end, end + 4, "");
                    colors.add(start);
                    colors.add(end);
                    colors.add(color);
                }
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stringBuilder);
            for (int a = 0; a < bolds.size() / 2; a++) {
                spannableStringBuilder.setSpan(new TypefaceSpan(Utilities.getTypeface("fonts/rmedium.ttf")), bolds.get(a * 2), bolds.get(a * 2 + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for (int a = 0; a < colors.size() / 3; a++) {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(colors.get(a * 3 + 2)), colors.get(a * 3), colors.get(a * 3 + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return spannableStringBuilder;
        } catch (Exception e) {
            Log.d(TAG, "TEXT: " + TAG + " ERROR: "+e);
            FileLog.e("tmessages", e);
        }
        return new SpannableStringBuilder(strhtml);
    }

    public static Typeface getTypeface(String assetPath) {
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(init.applicationContext.getAssets(), assetPath);
                    typefaceCache.put(assetPath, t);
                } catch (Exception e) {
                    FileLog.e("Typefaces", " Could not get typeface '" + assetPath + "' because " + e.getMessage());
                    return null;
                }
            }
            return typefaceCache.get(assetPath);
        }
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int)Math.ceil(density * value);
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            init.applicationHandler.post(runnable);
        } else {
            init.applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static void showKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }


    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void cancelRunOnUIThread(Runnable runnable) {
        init.applicationHandler.removeCallbacks(runnable);
    }

    public static boolean isTablet() {
        if (isTablet == null) {
            isTablet = init.applicationContext.getResources().getBoolean(R.bool.isTablet);
        }
        return isTablet;
    }

    public native static long doPQNative(long _what);


}
