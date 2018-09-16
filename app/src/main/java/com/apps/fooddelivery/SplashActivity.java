package com.apps.fooddelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apps.asyncTask.LoadLoginLocal;
import com.apps.interfaces.LoginListener;
import com.apps.sharedPref.SharePref;
import com.apps.utils.Constant;
import com.apps.utils.Methods;

public class SplashActivity extends AppCompatActivity {

    SharePref sharePref;
    LoadLoginLocal loadLoginLocal;
    ProgressBar progressBar;
    Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        hideStatusBar();

        sharePref = new SharePref(this);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        methods = new Methods(SplashActivity.this);

        if(sharePref.getEmail().equals("")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        } else {
            if(methods.isNetworkAvailable()) {
                loadLogin();
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void loadLogin() {
        loadLoginLocal = new LoadLoginLocal(this, new LoginListener() {
            @Override
            public void onStart() {
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(String success, String message) {
//                progressBar.setVisibility(View.GONE);
                if(success.equals("true")) {
                    if(message.equals("0")) {
                        Toast.makeText(SplashActivity.this, getResources().getString(R.string.email_pass_nomatch),Toast.LENGTH_SHORT).show();
                    } else {
                        Constant.isLogged = true;
                        Toast.makeText(SplashActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                        openMainActivity();
                    }
                } else {
                    Toast.makeText(SplashActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadLoginLocal.execute(Constant.URL_LOGIN_1 + sharePref.getEmail() + Constant.URL_LOGIN_2 + sharePref.getPassword());
    }

    private void openMainActivity() {
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}