package com.apps.fooddelivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.asyncTask.LoadCheckOut;
import com.apps.interfaces.LoginListener;
import com.apps.utils.Constant;
import com.apps.utils.Methods;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CheckOut extends AppCompatActivity {

    Toolbar toolbar;
    LoadCheckOut loadCheckOut;
    private EditText editText_comment,editText_address;
    private AppCompatButton button_checkout;
    private TextView textView_total;
    Methods methods;
    private String comment,address, cart_ids, total;
    ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        methods = new Methods(this);
        progressDialog = new ProgressDialog(CheckOut.this);
        progressDialog.setMessage(getString(R.string.loading));

        toolbar = (Toolbar)findViewById(R.id.toolbar_checkout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        methods.setStatusColor(getWindow(),toolbar);

        cart_ids = getIntent().getStringExtra("cart_ids");
        total = getIntent().getStringExtra("total");

        textView_total = (TextView)findViewById(R.id.tv_checkout_total);
        editText_address = (EditText)findViewById(R.id.editText_checkout_address);
        editText_comment = (EditText)findViewById(R.id.editText_checkout_comment);
        button_checkout = (AppCompatButton)findViewById(R.id.button_checkout);

        textView_total.setText(total);
        editText_address.setText(Constant.itemUser.getAddress());

//        if (methods.isNetworkAvailable()) {
//            new LoadUserDetails().execute();
//        } else {
//            Toast.makeText(CheckOut.this, getResources().getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
//        }

        button_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    address = editText_address.getText().toString();
                    comment = editText_comment.getText().toString();
                    loadCheckOutApi();
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

    private Boolean validate() {
        if(editText_address.getText().toString().trim().isEmpty()) {
            Toast.makeText(CheckOut.this, getResources().getString(R.string.address_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void loadCheckOutApi() {
        loadCheckOut = new LoadCheckOut(CheckOut.this, new LoginListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                Toast.makeText(CheckOut.this, message, Toast.LENGTH_SHORT).show();
                if(success.equals("0")) {
                    Toast.makeText(CheckOut.this, getString(R.string.error_order), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckOut.this, message, Toast.LENGTH_SHORT).show();
                    Constant.isCartRefresh = true;
                    Constant.menuCount = 0;
//                    finish();
                    Constant.isFromCheckOut = true;
                    Intent intent = new Intent(CheckOut.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        loadCheckOut.execute(Constant.URL_CHECKOUT_1 + Constant.itemUser.getId() + Constant.URL_CHECKOUT_2 + address + Constant.URL_CHECKOUT_3 + comment + Constant.URL_CHECKOUT_4 + cart_ids);
    }
}