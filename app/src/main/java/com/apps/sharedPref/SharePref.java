package com.apps.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.apps.utils.Constant;

public class SharePref {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharePref(Context context) {
        sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREF_LOGIN,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getEmail() {
        return sharedPreferences.getString(Constant.SHARED_PREF_EMAIL,"");
    }

    public String getPassword() {
        return sharedPreferences.getString(Constant.SHARED_PREF_PASSWORD,"");
    }

    public void setSharedPreferences(String email, String password) {
        editor.putString(Constant.SHARED_PREF_EMAIL,email);
        editor.putString(Constant.SHARED_PREF_PASSWORD,password);
        editor.apply();
    }
}
