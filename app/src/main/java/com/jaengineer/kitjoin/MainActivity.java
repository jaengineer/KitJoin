package com.jaengineer.kitjoin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;


import com.jaengineer.kitjoin.Controllers.LocaleController;
import com.jaengineer.kitjoin.Modules.ActionBarLayout;
import com.jaengineer.kitjoin.Modules.BaseFragment;
import com.jaengineer.kitjoin.Modules.DrawerLayoutContainer;
import com.jaengineer.kitjoin.Modules.SlideView;
import com.jaengineer.kitjoin.Utils.Utilities;
import com.jaengineer.kitjoin.Utils.layoutUtils;
import com.jaengineer.kitjoin.chat.init;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity implements ActionBarLayout.ActionBarLayoutDelegate{


    private static String TAG = "MAIN_ACTIVITY";

    private ActionBarLayout actionBarLayout;
    protected DrawerLayoutContainer drawerLayoutContainer;
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList<>();

    private boolean finished;

    private Runnable lockRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);

        Log.d(TAG, "TEXT: " + TAG + " ONCREATE");

        init.InitApplication();

        Intent intent = getIntent();

        if (intent != null && !intent.getBooleanExtra("fromInfoActivity", false)) {
            Intent intentInfo = new Intent(this, InformationActivity.class);
            startActivity(intentInfo);
            super.onCreate(savedInstanceState);
            finish();
        }

        super.onCreate(savedInstanceState);

        actionBarLayout = new ActionBarLayout(this);

        drawerLayoutContainer = new DrawerLayoutContainer(this);
        setContentView(drawerLayoutContainer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        drawerLayoutContainer.addView(actionBarLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        drawerLayoutContainer.setParentActionBarLayout(actionBarLayout);
        actionBarLayout.setDrawerLayoutContainer(drawerLayoutContainer);
        actionBarLayout.init(mainFragmentsStack);
        actionBarLayout.setDelegate(this);


        if (actionBarLayout.fragmentsStack.isEmpty()) {

            Log.d(TAG, "TEXT: " + TAG + " fragmentsStack.isEmpty()");
            actionBarLayout.addFragmentToStack(new LoginActivity());
            drawerLayoutContainer.setAllowOpenDrawer(false, false);
        }
        else {
            Log.d(TAG, "TEXT: " + TAG + "NO fragmentsStack.isEmpty()");
            boolean allowOpen = true;

            if (actionBarLayout.fragmentsStack.size() == 1 && actionBarLayout.fragmentsStack.get(0) instanceof LoginActivity) {

                Log.d(TAG, "TEXT: " + TAG + "actionBarLayout.fragmentsStack.size() == 1");
                allowOpen = false;
            }
            drawerLayoutContainer.setAllowOpenDrawer(allowOpen, false);

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d(TAG, "TEXT: " + TAG + " PULSA MENU");
           long num =  Utilities.doPQNative(434434);
            Log.d(TAG, "TEXT: " + TAG + " LLAMADA NATIVA "+num);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "TEXT: " + TAG + " ON_RESUME");
        super.onResume();
        actionBarLayout.onResume();
    }

    private void onFinish() {
        if (finished) {
            return;
        }
        finished = true;
        if (lockRunnable != null) {
            Utilities.cancelRunOnUIThread(lockRunnable);
            lockRunnable = null;
        }

    }


    @Override
    public boolean onPreIme() {
        return false;
    }

    @Override
    public boolean needPresentFragment(BaseFragment fragment, boolean removeLast, boolean forceWithoutAnimation, ActionBarLayout layout) {

        Log.d(TAG, "TEXT: " + TAG + " needPresentFragment");
        drawerLayoutContainer.setAllowOpenDrawer(!(fragment instanceof LoginActivity /*|| fragment instanceof CountrySelectActivity*/), false);
        return true;
    }

    @Override
    public boolean needAddFragmentToStack(BaseFragment fragment, ActionBarLayout layout) {
        drawerLayoutContainer.setAllowOpenDrawer(!(fragment instanceof LoginActivity /*|| fragment instanceof CountrySelectActivity*/), false);
        return true;
    }

    @Override
    public boolean needCloseLastFragment(ActionBarLayout layout) {

        if (layout.fragmentsStack.size() <= 1) {
            onFinish();
            finish();
            return false;
        }
        return true;
    }

    @Override
    public void onRebuildAllFragments(ActionBarLayout layout) {
       // drawerLayoutAdapter.notifyDataSetChanged();
    }
}
