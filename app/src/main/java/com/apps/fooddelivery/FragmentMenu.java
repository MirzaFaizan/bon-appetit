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
import com.apps.asyncTask.LoadMenu;
import com.apps.interfaces.MenuListener;
import com.apps.items.ItemMenu;
import com.apps.utils.Constant;
import com.apps.utils.Methods;

import java.util.ArrayList;


public class FragmentMenu extends Fragment {

    LoadMenu loadMenu;
    RecyclerView recyclerView;
    Methods methods;
    ArrayList<ItemMenu> arrayList;
    AdapterMenu adapterMenu;
    TextView textView_empty;
    ProgressDialog progressDialog;
    int pos = 0;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_menu_bycat,container,false);

        methods = new Methods(getActivity());
        pos = getArguments().getInt("pos",0);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));

        arrayList = new ArrayList<>();
        LinearLayoutManager llm_latest = new LinearLayoutManager(getActivity());

        view = (View)v.findViewById(R.id.view_menu);
        textView_empty = (TextView)v.findViewById(R.id.tv_empty_menu);
        recyclerView = (RecyclerView)v.findViewById(R.id.rv_hotel_menu);
        recyclerView.setLayoutManager(llm_latest);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if (methods.isNetworkAvailable()) {
            loadMenuApi();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private void loadMenuApi() {
        loadMenu = new LoadMenu(getActivity(), new MenuListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, ArrayList<ItemMenu> arrayList_menu) {
                if(getActivity() != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    Constant.arrayList_menuCat.get(pos).setMenuArrayList(arrayList_menu);
                    adapterMenu = new AdapterMenu(getActivity(), Constant.arrayList_menuCat.get(pos).getMenuArrayList());
                    recyclerView.setAdapter(adapterMenu);

                    if (Constant.arrayList_menuCat.get(pos).getMenuArrayList().size() == 0) {
                        //                    recyclerView.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                        textView_empty.setVisibility(View.VISIBLE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                        //                    recyclerView.setVisibility(View.VISIBLE);
                        textView_empty.setVisibility(View.GONE);
                    }
                }
            }
        });

        loadMenu.execute(Constant.URL_MENU_BY_CAT + Constant.arrayList_menuCat.get(pos).getId());
    }
}
