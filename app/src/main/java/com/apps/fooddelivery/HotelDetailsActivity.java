package com.apps.fooddelivery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.asyncTask.LoadRating;
import com.apps.asyncTask.LoadSingleHotel;
import com.apps.interfaces.RatingListener;
import com.apps.interfaces.SingleHotelListener;
import com.apps.items.ItemRestaurant;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HotelDetailsActivity extends AppCompatActivity {

    private LoadRating loadRating;
    private Toolbar toolbar;
//    ItemRestaurant itemRestaurant;
    RatingBar ratingBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView textView_name, testView_address, textView_tot_rate;
//    ImageView imageView_rate;
    ImageView imageView_Rest;
    ViewPagerAdapter  viewPagerAdapter;
//    ArrayList<ReviewItem> arrayList_review;
    LinearLayout ll_rating;
//    ReviewItem reviewItem;
    Methods methods;
    ProgressDialog progressDialog;
    Dialog dialog;
    EditText editText_review_msg;
    Button button_submit;
    String msg="",food="", quality="",punctuality="",courtsey="";
    Spinner spinner_food,spinner_price,spinner_punctuality,spinner_courtesy;
    Boolean review = false, isFirstLoad = true;
    Boolean isFirst = true;
    Menu menu;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details);

//        itemRestaurant = (ItemRestaurant) getIntent().getSerializableExtra("hotel");
        methods = new Methods(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar_rest_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Constant.itemRestaurant.getName());

        methods.setStatusColor(getWindow(),toolbar);

        progressDialog = new ProgressDialog(HotelDetailsActivity.this);
        progressDialog.setMessage(getString(R.string.loading));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        LinearLayout linearLayout = (LinearLayout)tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(ContextCompat.getColor(HotelDetailsActivity.this,R.color.tab_divider));
        drawable.setSize(2, 2);
        linearLayout.setDividerPadding(20);
        linearLayout.setDividerDrawable(drawable);
//        viewPager.setAdapter(viewPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);

        ll_rating = (LinearLayout)findViewById(R.id.ll_3);
        textView_tot_rate = (TextView)findViewById(R.id.tv_latest_details_tot_rating);
        textView_name = (TextView)findViewById(R.id.tv_details_name);
        imageView_Rest = (ImageView)findViewById(R.id.iv_details);
//        imageView_rate = (ImageView)findViewById(R.id.iv_add_review);
        testView_address = (TextView) findViewById(R.id.tv_details_address);
        ratingBar = (RatingBar)findViewById(R.id.rating_details);
        ratingBar.setMax(5);

        textView_tot_rate.setText("("+Constant.itemRestaurant.getTotalRating()+")");
        textView_name.setText(Constant.itemRestaurant.getName());
        testView_address.setText(Constant.itemRestaurant.getAddress());
        ratingBar.setRating(Constant.itemRestaurant.getAvgRatings());

        Picasso.with(this)
                .load(Constant.itemRestaurant.getImage())
                .fit().centerInside()
                .into(imageView_Rest);

        ll_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constant.isLogged) {
                    if (methods.isNetworkAvailable()) {
                        openRateDialog();
                    } else {
                        Toast.makeText(HotelDetailsActivity.this, getResources().getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HotelDetailsActivity.this, getResources().getString(R.string.not_log), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(methods.isNetworkAvailable()) {
            loadMenuCatApi();
        } else {
            Toast.makeText(this, getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        methods.changeCart(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_cart:
                Intent intent = new Intent(HotelDetailsActivity.this,CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openRateDialog(){
        dialog = new Dialog(HotelDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_review);

        final RatingBar ratingBar = (RatingBar)dialog.findViewById(R.id.rating_addreview);
        final EditText editText = (EditText)dialog.findViewById(R.id.et_add_review_msg);
        final Button button = (Button)dialog.findViewById(R.id.button_submit_rating);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingBar.getRating() > 0 && !editText.getText().toString().trim().isEmpty()) {
                    if(methods.isNetworkAvailable()) {
                        loadRatingApi(String.valueOf(ratingBar.getRating()), editText.getText().toString().replace(" ","%20"));
                    } else {
                        Toast.makeText(HotelDetailsActivity.this, getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HotelDetailsActivity.this, R.string.select_rating_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void loadRatingApi(String rate, String message) {
        loadRating = new LoadRating(new RatingListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message, float rating) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                dialog.dismiss();

                if(success.equals("true")) {
                    Toast.makeText(HotelDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

                    if(!message.contains("already")) {
                        Constant.itemRestaurant.setAvgRatings(rating);
                        Constant.itemRestaurant.setTotalRating(Constant.itemRestaurant.getTotalRating() + 1);
                        textView_tot_rate.setText("("+Constant.itemRestaurant.getTotalRating()+")");
                        ratingBar.setRating(rating);
                    }
                }
            }
        });

        loadRating.execute(Constant.URL_RATING_1 + Constant.itemUser.getId() + Constant.URL_RATING_2 + rate + Constant.URL_RATING_3 + message + Constant.URL_RATING_4 + Constant.itemRestaurant.getId());
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentMenuCat().newInstance(Constant.itemRestaurant.getId());
                case 1:
                    return new FragmentInfo().newInstance(Constant.itemRestaurant);
                case 2:
                    return new FragmentReviews();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.menu);
                case 1:
                    return getString(R.string.info);
                case 2:
                    return getString(R.string.reviews);
                default:
                    return getString(R.string.menu);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem()==1 || viewPager.getCurrentItem()==2) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void loadMenuCatApi() {
        LoadSingleHotel loadSingleHotel = new LoadSingleHotel(HotelDetailsActivity.this, Constant.itemRestaurant, new SingleHotelListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, ItemRestaurant itemRest) {
                if(HotelDetailsActivity.this != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    //                textView_name.setText(itemRestaurant.getName());
                    //                textView_address.setText(itemRestaurant.getAddress());
                    //                textView_type.setText(itemRestaurant.getCname());
                    //                setOpenTime();
                    Constant.itemRestaurant = itemRest;
                    viewPager.setAdapter(viewPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                }
            }
        });

        loadSingleHotel.execute(Constant.URL_SINGLE_HOTEL + Constant.itemRestaurant.getId());
    }

    @Override
    protected void onResume() {
        changeMenu();
        super.onResume();
    }

    public void changeMenu() {
        if(toolbar!=null && menu != null && menu.findItem(R.id.menu_cart) != null) {
            methods.changeCart(menu);
        }
    }
}
