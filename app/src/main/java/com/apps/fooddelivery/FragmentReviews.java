package com.apps.fooddelivery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.adapter.AdapterMenu;
import com.apps.adapter.AdapterReview;
import com.apps.asyncTask.LoadMenu;
import com.apps.interfaces.MenuListener;
import com.apps.items.ItemMenu;
import com.apps.items.ItemReview;
import com.apps.utils.Constant;
import com.apps.utils.Methods;

import java.util.ArrayList;


public class FragmentReviews extends Fragment {

    RecyclerView recyclerView;
    AdapterReview adapterReview;
    Methods methods;
    ArrayList<ItemReview> arrayList;
    TextView textView_empty;
    ProgressDialog progressDialog;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_review,container,false);

        methods = new Methods(getActivity());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));

        arrayList = new ArrayList<>();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        view = (View)v.findViewById(R.id.view_review);
        textView_empty = (TextView)v.findViewById(R.id.tv_empty_review);
        recyclerView = (RecyclerView)v.findViewById(R.id.rv_hotel_review);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        adapterReview = new AdapterReview(getActivity(),Constant.itemRestaurant.getArrayListReview());
        recyclerView.setAdapter(adapterReview);

        if (Constant.itemRestaurant.getArrayListReview().size() == 0) {
//            recyclerView.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            textView_empty.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.VISIBLE);
            textView_empty.setVisibility(View.GONE);
        }

        return v;
    }
}
