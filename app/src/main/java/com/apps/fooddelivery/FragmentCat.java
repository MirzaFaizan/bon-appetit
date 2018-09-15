package com.apps.fooddelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.adapter.AdapterCat;
import com.apps.asyncTask.LoadCat;
import com.apps.interfaces.CategoryListener;
import com.apps.items.ItemCat;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;
import com.apps.utils.Methods;
import com.apps.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentCat extends Fragment {

    Methods methods;
    LoadCat loadCat;
    RecyclerView recyclerView;
    ArrayList<ItemCat> arrayList;
    AdapterCat adapter;
    GridLayoutManager gridLayoutManager;
    TextView textView_empty_latest;
    ProgressDialog progressDialog;
    SearchView searchView;
    Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cat, container, false);
        setHasOptionsMenu(true);

        methods = new Methods(getActivity());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        arrayList = new ArrayList<>();

        textView_empty_latest = (TextView)rootView.findViewById(R.id.tv_cat_empty);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.rv_cat);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if (methods.isNetworkAvailable()) {
            loadCatApi();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(methods.isNetworkAvailable()) {
//                    showInter(position);
                    int real_pos = getPosition(adapter.getID(position));
                    Intent intent = new Intent(getActivity(),HotelByCatActivity.class);
                    intent.putExtra("cid",arrayList.get(real_pos).getId());
                    intent.putExtra("cname",arrayList.get(real_pos).getName());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
                }
            }
        }));

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        this.menu = menu;
        methods.changeCartFrag(menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if(adapter != null) {
                if (searchView.isIconified()) {
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.getFilter().filter(s);
                    adapter.notifyDataSetChanged();
                }
            }
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart_search:
                Intent intent = new Intent(getActivity(),CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCatApi(){
        loadCat = new LoadCat(getActivity(), new CategoryListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message, ArrayList<ItemCat> arrayListCat) {
                if(getActivity() != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    arrayList.addAll(arrayListCat);

                    if (success.equals("true")) {
                        adapter = new AdapterCat(getActivity(), arrayList);
                        recyclerView.setAdapter(adapter);

                        if (arrayList.size() > 0) {
                            textView_empty_latest.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            textView_empty_latest.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        loadCat.execute(Constant.URL_CAT);
    }

    private int getPosition(String id) {
        int count=0;
        for(int i=0;i<arrayList.size();i++)
        {
            if(id.equals(arrayList.get(i).getId()))
            {
                count = i;
                break;
            }
        }
        return count;
    }

//    private void showInter(final int poss) {
//        Constant.adCount = Constant.adCount + 1;
//        if(Constant.adCount % Constant.adDisplay == 0) {
//            ((MainActivity)getActivity()).mInterstitial.setAdListener(new AdListener() {
//
//                @Override
//                public void onAdClosed() {
//                    playIntent(poss);
//                    super.onAdClosed();
//                }
//            });
//            if(((MainActivity)getActivity()).mInterstitial.isLoaded()) {
//                ((MainActivity)getActivity()).mInterstitial.show();
//                ((MainActivity)getActivity()).loadInter();
//            } else {
//                playIntent(poss);
//            }
//        } else {
//            playIntent(poss);
//        }
//    }

//    private void playIntent(int posss) {
//        Constant.isOnline = true;
//        Constant.arrayList_play.clear();
//        Constant.arrayList_play.addAll(arrayList);
//        Constant.playPos = posss;
//        ((MainActivity)getActivity()).changeText(arrayList.get(posss).getMp3Name(),arrayList.get(posss).getArtist(),posss+1,arrayList.size(),arrayList.get(posss).getDuration(),arrayList.get(posss).getImageBig(),arrayList.get(posss).getIsDownload(),"home");
//
//        Constant.context = getActivity();
//        if(posss == 0) {
//            Intent intent = new Intent(getActivity(), PlayerService.class);
//            intent.setAction(PlayerService.ACTION_FIRST_PLAY);
//            getActivity().startService(intent);
//        }
//    }


    @Override
    public void onResume() {
        if(((MainActivity)getActivity()).toolbar!=null && menu != null && menu.findItem(R.id.menu_cart_search) != null) {
            methods.changeCartFrag(menu);
        }
        super.onResume();
    }
}