package com.jaengineer.kitjoin;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaengineer.kitjoin.Controllers.LocaleController;
import com.jaengineer.kitjoin.Utils.Utilities;
import com.jaengineer.kitjoin.Utils.hockeyapp;

/**
 * Created by Jose Alb on 07/09/2015.
 */
public class InformationActivity extends Activity {

    private static String TAG = "INFORMATION_ACTIVITY";

    private ViewPager viewPager;
    private ViewGroup pointIndicatorPage;
    private ImageView logoImage;
    private ImageView pointBottonImage;
    private int[] icons;
    private int[] titles;
    private int[] messages;

    private int lastPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_Information);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_layout);

        icons = new int[] {

                R.drawable.information,
                R.drawable.infocloud,
                R.drawable.infosecurity,
        };

        titles = new int[] {

                R.string.kitjoinTitle,
                R.string.cloudTitle,
                R.string.secureTitle
        };

        messages = new int[] {

                R.string.infoMessage,
                R.string.cloudMessage,
                R.string.secureMessage
        };

        viewPager = (ViewPager)findViewById(R.id.intro_view_pager);
        TextView startMessagingButton = (TextView) findViewById(R.id.start_messaging_button);
        startMessagingButton.setText(LocaleController.getString("StartMessaging", R.string.StartMessaging).toUpperCase());//Establece mensaje en el botón de inicio según idioma del dispositivo

        logoImage = (ImageView)findViewById(R.id.icon_information);
        pointBottonImage = (ImageView)findViewById(R.id.icon_indicator_page);
        pointIndicatorPage = (ViewGroup)findViewById(R.id.pages_indicator_point); //grupo de botones inferiores indicadores de la página actual
        pointBottonImage.setVisibility(View.GONE);

        viewPager.setAdapter(new Adapter());
        viewPager.setPageMargin(0);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int position) {

                if (position == ViewPager.SCROLL_STATE_IDLE || position == ViewPager.SCROLL_STATE_SETTLING) {

                    if (lastPage != viewPager.getCurrentItem()) {//Evita el parpadeo

                    lastPage = viewPager.getCurrentItem();//Número de la página actual


                    final ImageView fadeoutImage;//imagen saliente
                    final ImageView fadeinImage;//imagen entrante

                    if (logoImage.getVisibility() == View.VISIBLE) {
                        fadeoutImage = logoImage;//imagen que se va
                        fadeinImage = pointBottonImage;//nueva imágen
                        Log.d(TAG, "TEXT: " + TAG + "logoImage.getVisibility()1: " + logoImage.getVisibility());
                    } else {
                        Log.d(TAG, "TEXT: " + TAG + "logoImage.getVisibility()2: " + logoImage.getVisibility());
                        fadeoutImage = pointBottonImage;
                        fadeinImage = logoImage;
                    }

                    fadeinImage.bringToFront();
                    fadeinImage.setImageResource(icons[lastPage]);//Establece nuevo icono de imágen
                    fadeinImage.clearAnimation();
                    fadeoutImage.clearAnimation();

                    /************* Establece animación de cambiar de imágen ***********************************/
                    Animation outAnimation = AnimationUtils.loadAnimation(InformationActivity.this, R.anim.icon_anim_fade_out);
                    outAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            fadeoutImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    Animation inAnimation = AnimationUtils.loadAnimation(InformationActivity.this, R.anim.icon_anim_fade_in);
                    inAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            fadeinImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                    fadeoutImage.startAnimation(outAnimation);
                    fadeinImage.startAnimation(inAnimation);
                    /*********************************************************************/
                }
                }
            }
        });

        startMessagingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(InformationActivity.this, MainActivity.class);
                intent.putExtra("fromInfoActivity", true);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //registro en hockeyapp, para control de errores en los clientes
        hockeyapp.register(this);
        hockeyapp.updates(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    /********** ADAPTADOR PARA LAS DISTINTAS PÁGINAS DE INTRODUCCIÓN *****************/
    private class Adapter extends PagerAdapter {


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(container.getContext(), R.layout.info_view_layout, null);//carga la vista de cada página: título y mensaje
            TextView headerTextView = (TextView)view.findViewById(R.id.header_text);//título
            TextView messageTextView = (TextView)view.findViewById(R.id.message_text);//mensaje
            container.addView(view, 0);

            headerTextView.setText(getString(titles[position]));//Establece el título de cada página
            messageTextView.setText(Utilities.replaceTags(getString(messages[position])));//establece mensaje sin las etiquetas html

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            int count = pointIndicatorPage.getChildCount();
            for (int a = 0; a < count; a++) {
                View child = pointIndicatorPage.getChildAt(a);
                if (a == position) {
                    child.setBackgroundColor(0xff2ca5e0);
                } else {
                    child.setBackgroundColor(0xffbbbbbb);
                }
            }
        }


        @Override
        public int getCount() {
            return 3;//devuelve número de páginas totales
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (observer != null) {
                super.unregisterDataSetObserver(observer);
            }
        }
    }


}
