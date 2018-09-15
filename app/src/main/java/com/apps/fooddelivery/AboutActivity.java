package com.apps.fooddelivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.asyncTask.LoadAbout;
import com.apps.interfaces.LoginListener;
import com.apps.items.ItemAbout;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;
import com.apps.utils.Methods;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity {

    Toolbar toolbar;
    LoadAbout loadAbout;
    TextView textView_appname,textView_email,textView_website, textView_company, textView_contact, textView_version, textView_desc;
    ImageView imageView_logo;
    LinearLayout ll_email, ll_website, ll_company, ll_contact;
    //    DBHelper dbHelper;
    ProgressDialog pbar;
    Methods methods;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        methods = new Methods(this);
        methods.forceRTLIfSupported(getWindow());

        toolbar = (Toolbar) this.findViewById(R.id.toolbar_about);
        toolbar.setTitle(getString(R.string.about));
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		toolbar.setBackgroundColor(Constant.color);

        pbar = new ProgressDialog(this);
        pbar.setMessage(getResources().getString(R.string.loading));
        pbar.setCancelable(false);

//        webView = (WebView) findViewById(R.id.webView);
        textView_appname = (TextView)findViewById(R.id.textView_about_appname);
        textView_email = (TextView)findViewById(R.id.textView_about_email);
        textView_website = (TextView)findViewById(R.id.textView_about_site);
        textView_company = (TextView)findViewById(R.id.textView_about_company);
        textView_contact = (TextView)findViewById(R.id.textView_about_contact);
        textView_version = (TextView)findViewById(R.id.textView_about_appversion);
        textView_desc = (TextView)findViewById(R.id.textView_about_desc);
        imageView_logo = (ImageView)findViewById(R.id.imageView_about_logo);

        ll_email = (LinearLayout)findViewById(R.id.ll_email);
        ll_website = (LinearLayout)findViewById(R.id.ll_website);
        ll_contact = (LinearLayout)findViewById(R.id.ll_contact);
        ll_company = (LinearLayout)findViewById(R.id.ll_company);

//		textView_appname.setText(getResources().getString(R.string.app_name));
//        textView_email.setText(getResources().getString(R.string.aboutus_email));
//        ll_email.setVisibility(View.VISIBLE);
////        textView_website.setText(getResources().getString(R.string.aboutus_website));
//        textView_desc.setText(getResources().getString(R.string.aboutus_desc));
//		imageView_logo.setImageDrawable(getResources().getDrawable(R.drawable.about_logo));

        if(Constant.itemAbout == null) {
            if (methods.isNetworkAvailable()) {
                loadAboutTask();
            } else {
//				if(!dbHelper.getAbout()) {
//					Toast.makeText(AboutActivity.this, "First Time Load Application from Internet ", Toast.LENGTH_SHORT).show();
//				}
            }
        } else {
            setVariables();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    private void loadAboutTask(){
        loadAbout = new LoadAbout(AboutActivity.this, new LoginListener() {
            @Override
            public void onStart() {
                pbar.show();
            }

            @Override
            public void onEnd(String success, String message) {
                pbar.hide();
                if(success.equals("true")) {
                    setVariables();
                }
            }
        });
        loadAbout.execute(Constant.URL_ABOUT);
    }

    public void setVariables() {
        textView_appname.setText(Constant.itemAbout.getAppName());
        if(!Constant.itemAbout.getEmail().trim().isEmpty()) {
            ll_email.setVisibility(View.VISIBLE);
            textView_email.setText(Constant.itemAbout.getEmail());
        }

        if(!Constant.itemAbout.getWebsite().trim().isEmpty()) {
            ll_website.setVisibility(View.VISIBLE);
            textView_website.setText(Constant.itemAbout.getWebsite());
        }

        if(!Constant.itemAbout.getAuthor().trim().isEmpty()) {
            ll_company.setVisibility(View.VISIBLE);
            textView_company.setText(Constant.itemAbout.getAuthor());
        }

        if(!Constant.itemAbout.getContact().trim().isEmpty()) {
            ll_contact.setVisibility(View.VISIBLE);
            textView_contact.setText(Constant.itemAbout.getContact());
        }

        if(!Constant.itemAbout.getAppVersion().trim().isEmpty()) {
            textView_version.setText(Constant.itemAbout.getAppVersion());
        }
        textView_desc.setText(Html.fromHtml(Constant.itemAbout.getAppDesc()));
        if(Constant.itemAbout.getAppLogo().trim().isEmpty()) {
            imageView_logo.setVisibility(View.GONE);
        } else {
            Picasso
                    .with(AboutActivity.this)
                    .load(Constant.URL_ABOUT_US_LOGO+Constant.itemAbout.getAppLogo())
                    .into(imageView_logo);
        }

//        String mimeType = "text/html;charset=UTF-8";
//        String encoding = "utf-8";
//
//        String text = "<html><head>"
//                + "<style> body{color: #000 !important;text-align:left}"
//                + "</style></head>"
//                + "<body>"
//                + desc
//                + "</body></html>";
//
//        webView.loadData(text, mimeType, encoding);
    }
}

