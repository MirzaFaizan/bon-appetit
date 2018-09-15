package com.apps.fooddelivery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.apps.asyncTask.LoadProfileUpdate;
import com.apps.interfaces.LoginListener;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileEditActivity extends AppCompatActivity {

    private LoadProfileUpdate loadProfileUpdate;
    private EditText editText_name,editText_email,editText_phone,editText_pass,editText_cpass, editText_address;
    private AppCompatButton button_update;
    private RoundedImageView imageView_profile;
    private ImageView  imageView_editpropic;
    private String name,email,phone,password="",cpass = "", address = "", imagePath = "";
    ProgressDialog progressDialog;
    Toolbar toolbar;
    Methods methods;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        methods = new Methods(this);
        methods.forceRTLIfSupported(getWindow());

        AdView adView = (AdView)findViewById(R.id.adView_profedit);
        adView.loadAd(new AdRequest.Builder().build());

        progressDialog = new ProgressDialog(ProfileEditActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.updating));

        toolbar = (Toolbar)findViewById(R.id.toolbar_proedit);
        toolbar.setTitle(getResources().getString(R.string.profile_edit));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_update = (AppCompatButton)findViewById(R.id.button_prof_update);
        imageView_profile = (RoundedImageView) findViewById(R.id.iv_profile_edit);
        imageView_editpropic = (ImageView) findViewById(R.id.iv_edit_pro_ic);
        editText_name = (EditText)findViewById(R.id.et_prof_edit_fname);
        editText_email = (EditText)findViewById(R.id.et_prof_edit_email);
        editText_phone = (EditText)findViewById(R.id.et_prof_edit_mobile);
        editText_address = (EditText)findViewById(R.id.et_prof_edit_address);
        editText_pass = (EditText)findViewById(R.id.et_prof_edit_pass);
        editText_cpass = (EditText)findViewById(R.id.et_prof_edit_cpass);

        setProfileVar();

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    if(methods.isNetworkAvailable()) {
                        setVariables();
//                        new UpdateUser().execute();
                        loadProfileEdit();
                    } else {
                        Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        imageView_editpropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        return super.onCreateOptionsMenu(menu);
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

    private void loadProfileEdit() {
        loadProfileUpdate = new LoadProfileUpdate(ProfileEditActivity.this, new LoginListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                setVariables();
                if(success.equals("1")) {
                    if (message.equals("1")) {
                        updateArray();
                        Constant.isUpdate = true;
                        finish();
                        Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.update_prof_succ), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.email_already_regis), Toast.LENGTH_SHORT).show();
                    }
                } else if(success.equals("0")) {
                    Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.update_prof_not_succ), Toast.LENGTH_SHORT).show();
                }
            }
        });

//        String nm = editText_name.getText().toString().replace(" ","%20");

        loadProfileUpdate.execute(name,email,password, phone, address, imagePath);
    }

    private Boolean validate() {
        editText_name.setError(null);
        editText_email.setError(null);
        editText_cpass.setError(null);
        View focusView;
        if(editText_name.getText().toString().trim().isEmpty()) {
            Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
            editText_name.setError(getString(R.string.error_invalid_password));
            focusView = editText_name;
            focusView.requestFocus();
            return false;
        } else if(editText_email.getText().toString().trim().isEmpty()) {
            Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.email_empty), Toast.LENGTH_SHORT).show();
            editText_email.setError(getString(R.string.error_field_required));
            focusView = editText_email;
            focusView.requestFocus();
            return false;
        } else if(!editText_pass.getText().toString().trim().equals(editText_cpass.getText().toString().trim())) {
            Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.pass_nomatch), Toast.LENGTH_SHORT).show();
            editText_cpass.setError(getString(R.string.pass_nomatch));
            focusView = editText_cpass;
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void setVariables() {
        name = editText_name.getText().toString();
        email = editText_email.getText().toString();
        phone = editText_phone.getText().toString();
        password = editText_pass.getText().toString();
        address = editText_address.getText().toString();

        if(!password.equals("")) {
            methods.changeRemPass();
        }
    }

    private void updateArray() {
        Constant.itemUser.setName(name);
        Constant.itemUser.setEmail(email);
        Constant.itemUser.setMobile(phone);
        Constant.itemUser.setAddress(address);
    }

//    class UpdateUser extends AsyncTask<String, String, String> {
//
//        String suc = "";
//        String msg = "";
//
//        @Override
//        protected void onPreExecute() {
//            progressHUD.show();
//            super.onPreExecute();
//        }
//
//        protected String doInBackground(String... args) {
//            String nm = name.replace(" ","%20");
//            String json_strng = JsonUtils.getJSONString(Constant.URL_PROFILE_EDIT_1+Constant.user_id+
//                    Constant.URL_PROFILE_EDIT_2 + nm +
//                    Constant.URL_PROFILE_EDIT_3 + email +
//                    Constant.URL_PROFILE_EDIT_4 + password +
//                    Constant.URL_PROFILE_EDIT_5 + phone);
//            try {
//                JSONObject json = new JSONObject(json_strng);
//                JSONArray c = json.getJSONArray(Constant.TAG_ROOT);
//                JSONObject s = c.getJSONObject(0);
//                suc = s.getString(Constant.TAG_SUCCESS);
//                msg = s.getString(Constant.TAG_MSG);
//
//                return "1";
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return "0";
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "0";
//            }
//        }
//
//        protected void onPostExecute(String success) {
//            setVariables();
//            if(success.equals("1")) {
//                if (suc.equals("1")) {
//                    progressHUD.dismissWithSuccess(getResources().getString(R.string.success));
//                    updateArray();
//                    Constant.isUpdate = true;
//                    finish();
//                    Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.update_prof_succ), Toast.LENGTH_SHORT).show();
//                } else {
//                    progressHUD.dismissWithSuccess(getResources().getString(R.string.error));
//                    Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.email_already_regis), Toast.LENGTH_SHORT).show();
//                }
//            } else if(success.equals("0")) {
//                progressHUD.dismissWithFailure(getResources().getString(R.string.error));
//                Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.update_prof_not_succ), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public void setProfileVar() {
        editText_name.setText(Constant.itemUser.getName());
        editText_phone.setText(Constant.itemUser.getMobile());
        editText_email.setText(Constant.itemUser.getEmail());
        editText_address.setText(Constant.itemUser.getAddress());

        try {
            Picasso.with(ProfileEditActivity.this)
                    .load(Constant.itemUser.getImage())
                    .placeholder(R.drawable.placeholder_profile)
                    .into(imageView_profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            imagePath = methods.getPathImage(uri);

            try {
                Bitmap bitmap_upload = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView_profile.setImageBitmap(bitmap_upload);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
