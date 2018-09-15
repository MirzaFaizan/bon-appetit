package com.apps.fooddelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.adapter.AdapterHotelList;
import com.apps.asyncTask.LoadHotel;
import com.apps.interfaces.HomeListener;
import com.apps.items.ItemRestaurant;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.apps.utils.RecyclerItemClickListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class HotelByCatActivity extends AppCompatActivity {

    Toolbar toolbar;
    private Methods methods;
    private LoadHotel loadHotel;
    private AdapterHotelList adapterHotelList;
    private RecyclerView recyclerView;
    private ArrayList<ItemRestaurant> arrayList_hotel;
    private TextView textView_empty;
    private ProgressDialog progressDialog;
    private String cid = "", cname = "";
    private SearchView searchView;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_by_cat);

        AdView mAdView = (AdView) findViewById(R.id.adView_bycat);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        cid = getIntent().getStringExtra("cid");
        cname = getIntent().getStringExtra("cname");

        toolbar = (Toolbar) findViewById(R.id.toolbar_bycat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(cname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        methods = new Methods(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));

        arrayList_hotel = new ArrayList<>();
        LinearLayoutManager llm_latest = new LinearLayoutManager(this);

        textView_empty = (TextView) findViewById(R.id.tv_empty_bycat);
        recyclerView = (RecyclerView) findViewById(R.id.rv_hotel_bycat);
        recyclerView.setLayoutManager(llm_latest);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if (methods.isNetworkAvailable()) {
            loadHotelApi();
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(HotelByCatActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HotelByCatActivity.this, HotelDetailsActivity.class);
                Constant.itemRestaurant = arrayList_hotel.get(getPosition(Integer.parseInt(adapterHotelList.getID(position))));
                startActivity(intent);
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        this.menu = menu;
        methods.changeCartFrag(menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_cart:
                Intent intent = new Intent(HotelByCatActivity.this, CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (adapterHotelList != null) {
                if (searchView.isIconified()) {
                    recyclerView.setAdapter(adapterHotelList);
                    adapterHotelList.notifyDataSetChanged();
                } else {
                    adapterHotelList.getFilter().filter(s);
                    adapterHotelList.notifyDataSetChanged();
                }
            }
            return true;
        }
    };

    private void loadHotelApi() {
        loadHotel = new LoadHotel(HotelByCatActivity.this, new HomeListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, ArrayList<ItemRestaurant> arrayList_latest, ArrayList<ItemRestaurant> arrayList_featured) {
                if (HotelByCatActivity.this != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (success.equals("true")) {
                        arrayList_hotel.addAll(arrayList_latest);
                        adapterHotelList = new AdapterHotelList(HotelByCatActivity.this, arrayList_hotel);

                        recyclerView.setAdapter(adapterHotelList);
                    } else {
                        Toast.makeText(HotelByCatActivity.this, getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                    }

                    if (arrayList_latest.size() > 0) {
                        textView_empty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        textView_empty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }
        });

        loadHotel.execute(Constant.URL_HOTEL_BY_CAT + cid);
    }

    public int getPosition(int id) {
        int count = 0;
        for (int i = 0; i < arrayList_hotel.size(); i++) {
            if (id == Integer.parseInt(arrayList_hotel.get(i).getId())) {
                count = i;
                break;
            }
        }
        return count;
    }

    @Override
    protected void onResume() {
        if (toolbar != null && menu != null && menu.findItem(R.id.menu_cart_search) != null) {
            methods.changeCartFrag(menu);
        }
        super.onResume();
    }
}
