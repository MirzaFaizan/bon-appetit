package com.apps.fooddelivery;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.apps.adapter.AdapterOrderList;
import com.apps.asyncTask.LoadOderList;
import com.apps.interfaces.OrderListListener;
import com.apps.items.ItemOrderList;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.apps.utils.RecyclerItemClickListener;

import java.util.ArrayList;


public class FragmentOrderList extends Fragment {

    LoadOderList loadOderList;
    RecyclerView recyclerView;
    AdapterOrderList adapterOrderList;
    Methods methods;
    ArrayList<ItemOrderList> arrayList;
    TextView textView_empty;
    ProgressDialog progressDialog;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_orderlist, container, false);

        methods = new Methods(getActivity());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));

        arrayList = new ArrayList<>();
        LinearLayoutManager llm_latest = new LinearLayoutManager(getActivity());

        view = (View) v.findViewById(R.id.view_orderlist);
        textView_empty = (TextView) v.findViewById(R.id.tv_empty_orderlist);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_orderlist);
        recyclerView.setLayoutManager(llm_latest);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                Constant.itemOrderList = arrayList.get(position);
                startActivity(intent);
            }
        }));

        if (Constant.isLogged) {
            if (methods.isNetworkAvailable()) {
                loadOrderListApi();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
            }
        } else {
            textView_empty.setText(getString(R.string.not_log));
            textView_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        return v;
    }

    private void loadOrderListApi() {
        loadOderList = new LoadOderList(new OrderListListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, ArrayList<ItemOrderList> arrayListorderLists) {
                if (getActivity() != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (success.equals("true")) {
                        arrayList.addAll(arrayListorderLists);
                        adapterOrderList = new AdapterOrderList(getActivity(), arrayList);
                        recyclerView.setAdapter(adapterOrderList);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                    }

                    if (arrayList.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                        textView_empty.setVisibility(View.VISIBLE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        textView_empty.setVisibility(View.GONE);
                    }
                }
            }
        });

        loadOderList.execute(Constant.URL_ORDER_LIST + Constant.itemUser.getId());
    }
}
