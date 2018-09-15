package com.apps.fooddelivery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.asyncTask.LoadAbout;
import com.apps.interfaces.LoginListener;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LoadAbout loadAbout;
    Methods methods;
    public Toolbar toolbar;
    DrawerLayout drawer;
    TextView textView_header_message;
    MenuItem menuItem_login;
    FragmentManager fm;
    ProgressDialog pbar;
    String selectedFragment = "";
    NavigationView navigationView;
    Boolean mRecentlyBackPressed = false;
    Handler mExitHandler = new Handler();
    Menu menu;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView_main);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        methods = new Methods(this);
        methods.setStatusColor(getWindow(),toolbar);
        methods.forceRTLIfSupported(getWindow());
        fm = getSupportFragmentManager();

        pbar = new ProgressDialog(this);
        pbar.setMessage(getString(R.string.loading));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(false);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        toggle.setHomeAsUpIndicator(R.mipmap.nav);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuItem_login = navigationView.getMenu().findItem(R.id.nav_login);
        textView_header_message = (TextView)navigationView.getHeaderView(0).findViewById(R.id.tv_header_msg);

        changeLoginTitle();

        if(!Constant.isFromCheckOut) {
            FragmentHome f1 = new FragmentHome();
            loadFrag(f1, "home", fm);
            getSupportActionBar().setTitle(getResources().getString(R.string.home));
            navigationView.setCheckedItem(R.id.nav_home);
        } else {
            Constant.isFromCheckOut = false;
            FragmentOrderList f1 = new FragmentOrderList();
            loadFrag(f1,"orderlist",fm);
            toolbar.setTitle(getString(R.string.orderlist));
            navigationView.setCheckedItem(R.id.nav_orderlistr);
        }

        checkPer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        methods.changeCart(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_cart) {
            Intent intent = new Intent(MainActivity.this,CartActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                FragmentHome f1 = new FragmentHome();
                loadFrag(f1,"home",fm);
                toolbar.setTitle(getString(R.string.app_name));
                break;
//            case R.id.button_drawer_login:
//                jsonUtils.clickLogin();
            case R.id.nav_cat:
                FragmentCat fcat = new FragmentCat();
                loadFrag(fcat,"cat",fm);
                toolbar.setTitle(getString(R.string.category));
                break;
            case R.id.nav_hotel_list:
                FragmentHotelList flist = new FragmentHotelList();
                loadFrag(flist,"list",fm);
                toolbar.setTitle(getString(R.string.hotel_list));
                break;
            case R.id.nav_orderlistr:
                FragmentOrderList forder = new FragmentOrderList();
                loadFrag(forder,"orderlist",fm);
                toolbar.setTitle(getString(R.string.orderlist));
                break;
            case R.id.nav_profile:
                FragmentProfile fprof = new FragmentProfile();
                loadFrag(fprof,"profile",fm);
                toolbar.setTitle("Profile");
                break;
            case R.id.nav_about:
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_rate:
                final String appName = getPackageName();//your application package name i.e play store application url
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="
                                    + appName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + appName)));
                }
                break;
            case R.id.nav_shareapp:
                Intent ishare = new Intent(Intent.ACTION_SEND);
                ishare.setType("text/plain");
                ishare.putExtra(Intent.EXTRA_TEXT,getResources().getString(R.string.app_name)+" - http://play.google.com/store/apps/details?id="+getPackageName());
                startActivity(ishare);
                break;
            case R.id.nav_priacy:
                if(Constant.itemAbout == null) {
                    if (methods.isNetworkAvailable()) {
                        loadAboutTask();
                    } else {
//				if(!dbHelper.getAbout()) {
//					Toast.makeText(AboutActivity.this, "First Time Load Application from Internet ", Toast.LENGTH_SHORT).show();
//				}
                    }
                } else {
                    openPrivacyDialog();
                }
                break;
            case R.id.nav_login:
                methods.clickLogin();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        selectedFragment = name;
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.frame_nav, f1, name);
        ft.commit();
    }

    private void changeLoginTitle() {
        if(Constant.isLogged) {
            menuItem_login.setTitle(getString(R.string.logout));
            menuItem_login.setIcon(ContextCompat.getDrawable(MainActivity.this,R.mipmap.logout));
            textView_header_message.setText(getString(R.string.hi) + " " + Constant.itemUser.getName());
        } else {
            menuItem_login.setTitle(getString(R.string.login));
            menuItem_login.setIcon(ContextCompat.getDrawable(MainActivity.this,R.mipmap.login));
            textView_header_message.setText(getString(R.string.hi) + " " + getString(R.string.guest));
        }
    }

    public void openPrivacyDialog() {
        Dialog dialog;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new Dialog(MainActivity.this,android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            dialog = new Dialog(MainActivity.this);
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_privacy);

        WebView webview = (WebView)dialog.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
//		webview.loadUrl("file:///android_asset/privacy.html");
        String mimeType = "text/html;charset=UTF-8";
        String encoding = "utf-8";

        if(Constant.itemAbout != null) {
            String text = "<html><head>"
                    + "<style> body{color: #000 !important;text-align:left}"
                    + "</style></head>"
                    + "<body>"
                    + Constant.itemAbout.getPrivacy()
                    + "</body></html>";

            webview.loadData(text, mimeType, encoding);
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void loadAboutTask(){
        loadAbout = new LoadAbout(MainActivity.this, new LoginListener() {
            @Override
            public void onStart() {
                pbar.show();
            }

            @Override
            public void onEnd(String success, String message) {
                if(pbar.isShowing()) {
                    pbar.hide();
                }
                if(success.equals("true")) {
                    openPrivacyDialog();
                }
            }
        });
        loadAbout.execute(Constant.URL_ABOUT);
    }

    public void checkPer() {
        if ((ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        boolean canUseExternalStorage = false;

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                }

                if (!canUseExternalStorage) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.cannot_use_save), Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        }
    }

    Runnable mExitRunnable = new Runnable() {
        @Override
        public void run() {
            mRecentlyBackPressed=false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(selectedFragment.equals("home")) {
//                super.onBackPressed();

//                if (mRecentlyBackPressed) {
//                    mExitHandler.removeCallbacks(mExitRunnable);
//                    mRecentlyBackPressed = false;
//                    moveTaskToBack(true);
//                } else {
//                    mRecentlyBackPressed = true;
//                    Toast.makeText(this, getResources().getString(R.string.press_again_exit), Toast.LENGTH_SHORT).show();
//                    mExitHandler.postDelayed(mExitRunnable, 2000L);
//                }
                exitDialog();

            } else {
                FragmentHome f1 = new FragmentHome();
                loadFrag(f1,"home",fm);
                navigationView.setCheckedItem(R.id.nav_home);
                getSupportActionBar().setTitle(getResources().getString(R.string.home));
            }
        }
    }

    private void exitDialog() {
        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(MainActivity.this, R.style.ThemeDialog);
        } else {
            alert = new AlertDialog.Builder(MainActivity.this);
        }

        alert.setTitle(getString(R.string.exit));
        alert.setMessage(getString(R.string.sure_exit));
        alert.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    @Override
    protected void onResume() {
        if(toolbar!=null && menu != null && menu.findItem(R.id.menu_cart) != null) {
            methods.changeCart(menu);
            changeLoginTitle();
        }
        super.onResume();
    }
}
