package com.apps.fooddelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.asyncTask.LoadProfile;
import com.apps.interfaces.LoginListener;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class FragmentProfile extends Fragment {

    Methods methods;
    RoundedImageView imageView_profile;
    TextView textView_name, textView_email, textView_mobile, textView_notlog, textView_address;
    LinearLayout ll_mobile, ll_address;
    View view_phone, view_address;
    ProgressDialog progressDialog;
    LoadProfile loadProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        methods = new Methods(getActivity());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getActivity().getResources().getString(R.string.loading));

        imageView_profile = (RoundedImageView) rootView.findViewById(R.id.iv_profile);
        textView_name = (TextView) rootView.findViewById(R.id.tv_prof_fname);
        textView_email = (TextView) rootView.findViewById(R.id.tv_prof_email);
        textView_mobile = (TextView) rootView.findViewById(R.id.tv_prof_mobile);
        textView_address = (TextView) rootView.findViewById(R.id.tv_prof_address);
        textView_notlog = (TextView) rootView.findViewById(R.id.textView_notlog);

        ll_mobile = (LinearLayout) rootView.findViewById(R.id.ll_prof_phone);
        ll_address = (LinearLayout) rootView.findViewById(R.id.ll_prof_address);

        view_phone = rootView.findViewById(R.id.view_prof_phone);
        view_address = rootView.findViewById(R.id.view_prof_address);

        if (Constant.isLogged) {
            if (methods.isNetworkAvailable()) {
                loadProfileMethod();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
            }
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_profile_edit:
                if (Constant.isLogged) {
                    Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.not_log), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadProfileMethod() {
        loadProfile = new LoadProfile(getActivity(), new LoginListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message) {
                if (getActivity() != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (success.equals("1")) {
                        if (message.equals("0")) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_user_found), Toast.LENGTH_SHORT).show();
                        }
                    }
                    setVariables();
                }
            }
        });

        loadProfile.execute(Constant.URL_PROFILE + Constant.itemUser.getId());
    }

    public void setVariables() {

        textView_name.setText(Constant.itemUser.getName());
        textView_mobile.setText(Constant.itemUser.getMobile());

        textView_email.setText(Constant.itemUser.getEmail());
        textView_address.setText(Constant.itemUser.getAddress());

        if (!Constant.itemUser.getMobile().trim().isEmpty()) {
            ll_mobile.setVisibility(View.VISIBLE);
            view_phone.setVisibility(View.VISIBLE);
        }

        if (!Constant.itemUser.getAddress().trim().isEmpty()) {
            ll_address.setVisibility(View.VISIBLE);
            view_address.setVisibility(View.VISIBLE);
        }

        try {
            Picasso.with(getActivity())
                    .load(Constant.itemUser.getImage())
                    .placeholder(R.drawable.placeholder_profile)
                    .into(imageView_profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        if (Constant.isLogged) {
            if (Constant.isUpdate) {
                Constant.isUpdate = false;
                setVariables();
            }
            textView_notlog.setVisibility(View.GONE);
        } else {
            textView_notlog.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }
}
