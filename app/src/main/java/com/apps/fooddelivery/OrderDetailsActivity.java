package com.apps.fooddelivery;

import android.content.Context;
import android.net.ParseException;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.adapter.AdapterOrderDetailsMenu;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderDetailsActivity extends AppCompatActivity {

    Methods methods;
    Toolbar toolbar;
    RecyclerView recyclerView;
    AdapterOrderDetailsMenu adapter;
    TextView textView_unqid, textView_date, textView_address, textView_comment, textView_qty, textView_price, textView_status,textView_time;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        AdView mAdView = (AdView) findViewById(R.id.adView_order_details);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toolbar = (Toolbar) findViewById(R.id.toolbar_orderdetails);
        toolbar.setTitle(Constant.itemOrderList.getUniqueId());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        methods = new Methods(this);

        textView_unqid = (TextView) findViewById(R.id.tv_orderdetails_uniqueid);
        textView_date = (TextView) findViewById(R.id.tv_orderdetails_date);
        textView_time = (TextView) findViewById(R.id.tv_orderdetails_time);
        textView_address = (TextView) findViewById(R.id.tv_orderdetails_address);
        textView_comment = (TextView) findViewById(R.id.tv_orderdetails_comment);
        textView_qty = (TextView) findViewById(R.id.tv_orderdetails_qty);
        textView_price = (TextView) findViewById(R.id.tv_orderdetails_totalprice);
        textView_status = (TextView) findViewById(R.id.tv_orderlist_status);

        LinearLayoutManager llm_latest = new LinearLayoutManager(OrderDetailsActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_orderdetails_menu);
        recyclerView.setLayoutManager(llm_latest);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        textView_unqid.setText(Constant.itemOrderList.getUniqueId());
        textView_status.setText(Constant.itemOrderList.getStatus());
        textView_address.setText(Constant.itemOrderList.getAddress());
        textView_comment.setText(Constant.itemOrderList.getComment());
        textView_qty.setText(getString(R.string.qty) + " " + Constant.itemOrderList.getTotalQuantity());
        textView_price.setText(getString(R.string.price) + " " + getString(R.string.currency) + " " + Constant.itemOrderList.getTotalBill());


        textView_date.setText(Constant.itemOrderList.getDate().split(" ")[0]);
        textView_time.setText(Constant.itemOrderList.getDate().split(" ")[1]);

        adapter = new AdapterOrderDetailsMenu(OrderDetailsActivity.this,Constant.itemOrderList.getArrayListOrderMenu());
        recyclerView.setAdapter(adapter);

        switch (Constant.itemOrderList.getStatus()) {
            case "Pending":
                textView_status.setBackgroundDrawable(ContextCompat.getDrawable(OrderDetailsActivity.this, R.drawable.bg_round_red));
                break;
            case "Process":
                textView_status.setBackgroundDrawable(ContextCompat.getDrawable(OrderDetailsActivity.this, R.drawable.bg_round_orange));
                break;
            default:
                textView_status.setBackgroundDrawable(ContextCompat.getDrawable(OrderDetailsActivity.this, R.drawable.bg_round_green));
                break;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
