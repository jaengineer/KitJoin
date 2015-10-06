package com.jaengineer.kitjoin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jaengineer.kitjoin.Controllers.LocaleController;
import com.jaengineer.kitjoin.Modules.ActionBar;
import com.jaengineer.kitjoin.Modules.ActionBarMenu;
import com.jaengineer.kitjoin.Modules.BaseFragment;
import com.jaengineer.kitjoin.Modules.SlideView;
import com.jaengineer.kitjoin.Utils.FileLog;
import com.jaengineer.kitjoin.Utils.Utilities;
import com.jaengineer.kitjoin.Utils.layoutUtils;
import com.jaengineer.kitjoin.chat.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jose Alb on 09/09/2015.
 */
public class LoginActivity extends BaseFragment{

    private static String TAG = "LOGIN";

    private int currentViewNum = 0;
    private SlideView[] views = new SlideView[5];
    private final static int done_button = 1;

    @Override
    public View createView(Context context) {

        Log.d(TAG, "TEXT: " + TAG + " ON_CREATE");

        actionBar.setTitle(LocaleController.getString("AppName", R.string.AppName));

        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == done_button) {
                    views[currentViewNum].onNextPressed();
                } else if (id == -1) {
                    onBackPressed();
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();
        menu.addItemWithWidth(done_button, R.drawable.ic_done, Utilities.dp(56));

        fragmentView = new ScrollView(context);
        ScrollView scrollView = (ScrollView) fragmentView;
        scrollView.setFillViewport(true);

        FrameLayout frameLayout = new FrameLayout(context);
        scrollView.addView(frameLayout);
        ScrollView.LayoutParams layoutParams = (ScrollView.LayoutParams) frameLayout.getLayoutParams();
        layoutParams.width = ScrollView.LayoutParams.MATCH_PARENT;
        layoutParams.height = ScrollView.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        frameLayout.setLayoutParams(layoutParams);

        views[0] = new PhoneView(context);

        int a = 0;

        views[0].setVisibility(a == 0 ? View.VISIBLE : View.GONE);
        frameLayout.addView(views[0]);
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) views[0].getLayoutParams();
        layoutParams1.width = layoutUtils.MATCH_PARENT;
        layoutParams1.height = a == 0 ? layoutUtils.WRAP_CONTENT : layoutUtils.MATCH_PARENT;
        layoutParams1.leftMargin = Utilities.dp(Utilities.isTablet() ? 26 : 18);
        layoutParams1.rightMargin = Utilities.dp(Utilities.isTablet() ? 26 : 18);
        layoutParams1.topMargin = Utilities.dp(30);
        layoutParams1.gravity = Gravity.TOP | Gravity.LEFT;
        views[0].setLayoutParams(layoutParams1);

        Bundle savedInstanceState = loadCurrentState();
        if (savedInstanceState != null) {
            currentViewNum = savedInstanceState.getInt("currentViewNum", 0);
        }

         a=0;
        actionBar.setTitle(views[currentViewNum].getHeaderName());
        for ( a = 0; a < views.length; a++) {
            if (savedInstanceState != null) {
                views[a].restoreStateParams(savedInstanceState);
            }
            if (currentViewNum == a) {
                actionBar.setBackButtonImage(views[a].needBackButton() ? R.drawable.ic_ab_back : 0);
                views[a].setVisibility(View.VISIBLE);
                views[a].onShow();
            } else {
                views[a].setVisibility(View.GONE);
            }
        }


        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "TEXT: " + TAG + " ONRESUME");
    }

    private Bundle loadCurrentState() {
        try {
            Bundle bundle = new Bundle();
            SharedPreferences preferences = init.applicationContext.getSharedPreferences("logininfo", Context.MODE_PRIVATE);
            Map<String, ?> params = preferences.getAll();
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String[] args = key.split("_\\|_");
                if (args.length == 1) {
                    if (value instanceof String) {
                        bundle.putString(key, (String) value);
                    } else if (value instanceof Integer) {
                        bundle.putInt(key, (Integer) value);
                    }
                } else if (args.length == 2) {
                    Bundle inner = bundle.getBundle(args[0]);
                    if (inner == null) {
                        inner = new Bundle();
                        bundle.putBundle(args[0], inner);
                    }
                    if (value instanceof String) {
                        inner.putString(args[1], (String) value);
                    } else if (value instanceof Integer) {
                        inner.putInt(args[1], (Integer) value);
                    }
                }
            }
            return bundle;
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        }
        return null;
    }

    @Override
    public boolean onBackPressed() {
        if (currentViewNum == 0) {
            for (SlideView v : views) {
                if (v != null) {
                    v.onDestroyActivity();
                }
            }
            clearCurrentState();
            return true;
        } else if (currentViewNum == 3) {
            views[currentViewNum].onBackPressed();
           // setPage(0, true, null, true);
        } else if (currentViewNum == 4) {
            views[currentViewNum].onBackPressed();
          //  setPage(3, true, null, true);
        }
        return false;
    }


    private void clearCurrentState() {
        SharedPreferences preferences = init.applicationContext.getSharedPreferences("logininfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


    public class PhoneView extends SlideView implements AdapterView.OnItemSelectedListener {

        private EditText codeField;
        private EditText phoneField;
        private TextView countryButton;

        private int countryState = 0;

        private ArrayList<String> countriesArray = new ArrayList<>();
        private HashMap<String, String> countriesMap = new HashMap<>();
        private HashMap<String, String> codesMap = new HashMap<>();

        private boolean ignoreSelection = false;
        private boolean ignoreOnTextChange = false;
        private boolean ignoreOnPhoneChange = false;
        private boolean nextPressed = false;

        public PhoneView(Context context) {
            super(context);

            setOrientation(VERTICAL);

            countryButton = new TextView(context);
            countryButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            countryButton.setPadding(Utilities.dp(12), Utilities.dp(10), Utilities.dp(12), 0);
            countryButton.setTextColor(0xff212121);
            countryButton.setMaxLines(1);
            countryButton.setSingleLine(true);
            countryButton.setEllipsize(TextUtils.TruncateAt.END);
            countryButton.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.CENTER_HORIZONTAL);
            countryButton.setBackgroundResource(R.drawable.spinner_states);
            addView(countryButton);
            LayoutParams layoutParams = (LayoutParams) countryButton.getLayoutParams();
            layoutParams.width = layoutUtils.MATCH_PARENT;
            layoutParams.height = Utilities.dp(36);
            layoutParams.bottomMargin = Utilities.dp(14);
            countryButton.setLayoutParams(layoutParams);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
