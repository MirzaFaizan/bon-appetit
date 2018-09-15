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

import com.apps.adapter.AdapterMenuCat;
import com.apps.asyncTask.LoadMenuCat;
import com.apps.interfaces.MenuCatListener;
import com.apps.items.ItemMenuCat;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.apps.utils.RecyclerItemClickListener;

import java.util.ArrayList;


public class FragmentMenuCat extends Fragment {

    private Methods methods;
    private LoadMenuCat loadMenuCat;
    private AdapterMenuCat adapterMenuCat;
    private RecyclerView recyclerView;
    private View view;
    private TextView textView_empty;
    private ProgressDialog progressDialog;
    private String rid = "";

    public FragmentMenuCat newInstance(String rid) {
        FragmentMenuCat fragment = new FragmentMenuCat();
        Bundle args = new Bundle();
        args.putString("rid", rid);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_menu_cat, container, false);

        methods = new Methods(getActivity());
        rid = getArguments().getString("rid");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));

//        arrayList = new ArrayList<>();
        LinearLayoutManager llm_latest = new LinearLayoutManager(getActivity());

        view = (View) v.findViewById(R.id.view_menucat);
        textView_empty = (TextView) v.findViewById(R.id.tv_empty_menucat);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_hotel_menubycat);
        recyclerView.setLayoutManager(llm_latest);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if (methods.isNetworkAvailable()) {
            loadMenuCatApi();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FragmentMenu nextFrag = new FragmentMenu();
                Bundle b = new Bundle();
                b.putInt("pos", position);
                nextFrag.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, nextFrag, "fmenu")
                        .addToBackStack(null)
                        .commit();
            }
        }));

        return v;
    }

    private void loadMenuCatApi() {
        Constant.arrayList_menuCat.clear();
        loadMenuCat = new LoadMenuCat(getActivity(), new MenuCatListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, ArrayList<ItemMenuCat> arrayList_menucat) {
                if (getActivity() != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    Constant.arrayList_menuCat.addAll(arrayList_menucat);
                    adapterMenuCat = new AdapterMenuCat(getActivity(), arrayList_menucat);
                    recyclerView.setAdapter(adapterMenuCat);

                    if (Constant.arrayList_menuCat.size() == 0) {
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

        loadMenuCat.execute(Constant.URL_MENU_CAT_BY_HOTEL + rid);
    }
}
