package com.apps.fooddelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.adapter.AdapterCart;
import com.apps.asyncTask.LoadCart;
import com.apps.interfaces.CartListener;
import com.apps.items.ItemCart;
import com.apps.utils.Constant;
import com.apps.utils.Methods;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    Toolbar toolbar;
    LoadCart loadCart;
    RecyclerView recyclerView;
    AdapterCart adapterCart;
    Methods methods;
    ArrayList<ItemCart> arrayList;
    TextView textView_empty;
    public static TextView textView_total;
    ProgressDialog progressDialog;
    float total = 0;
    View view;
    AppCompatButton button_checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        methods = new Methods(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar_cart);
        toolbar.setTitle(getString(R.string.cart));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));

        arrayList = new ArrayList<>();
        LinearLayoutManager llm = new LinearLayoutManager(this);

        view = (View)findViewById(R.id.view_cart);
        button_checkout = (AppCompatButton)findViewById(R.id.button_confirm_order);
        textView_total = (TextView)findViewById(R.id.tv_cart_total);
        textView_empty = (TextView)findViewById(R.id.tv_empty_cart);
        recyclerView = (RecyclerView)findViewById(R.id.rv_cart);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if(Constant.isLogged) {
            if (methods.isNetworkAvailable()) {
                loadCartApi();
            } else {
                Toast.makeText(this, getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            textView_empty.setText(getString(R.string.not_log));
        }

        button_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayList.size() > 0) {
                    Intent intent = new Intent(CartActivity.this, CheckOut.class);
                    intent.putExtra("cart_ids", getCartIds());
                    intent.putExtra("total", textView_total.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(CartActivity.this, getString(R.string.no_items_cart), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void loadCartApi() {
        arrayList.clear();
        loadCart = new LoadCart(new CartListener() {
            @Override
            public void onStart() {
                total = 0;
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message, ArrayList<ItemCart> array) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if(success.equals("true")) {
                    arrayList.addAll(array);
                    adapterCart = new AdapterCart(CartActivity.this, arrayList);
                    recyclerView.setAdapter(adapterCart);

                    if(array.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                        textView_total.setVisibility(View.GONE);
                        button_checkout.setVisibility(View.INVISIBLE);
                        textView_empty.setVisibility(View.VISIBLE);
                    } else {
                        textView_empty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        view.setVisibility(View.VISIBLE);

                        for(int i=0; i<arrayList.size();i++) {
                            total = total + (Float.parseFloat(arrayList.get(i).getMenuPrice()) * Float.parseFloat(arrayList.get(i).getMenuQty()));
                        }
                    }
                    textView_total.setText(getString(R.string.currency)+ " " +total);
                } else {
                    Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadCart.execute(Constant.URL_CART + Constant.itemUser.getId());
    }

    public void hideView() {
        view.setVisibility(View.GONE);
    }

    private String getCartIds() {
        String ids = "";

        if(arrayList.size() > 0) {
            ids = arrayList.get(0).getId();
            for (int i = 1; i < arrayList.size(); i++) {
                ids = ids + "," + arrayList.get(i).getId();
            }
        }
        return ids;
    }

    @Override
    protected void onResume() {
        if(Constant.isCartRefresh) {
            Constant.isCartRefresh = false;
            if (methods.isNetworkAvailable()) {
                loadCartApi();
            } else {
                Toast.makeText(this, getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
            }
        }
        super.onResume();
    }
}
