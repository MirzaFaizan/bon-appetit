package com.apps.fooddelivery;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.onesignal.OneSignal;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        OneSignal.startInit(getApplicationContext()).init();

//        MobileAds.initialize(this, getString(R.string.admob_app_id));

//        DBHelper dbHelper = new DBHelper(getApplicationContext());
//        try {
//            dbHelper.createDataBase();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
