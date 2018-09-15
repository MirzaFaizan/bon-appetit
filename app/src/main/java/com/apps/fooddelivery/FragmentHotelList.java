package com.apps.fooddelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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

import com.apps.adapter.AdapterHotelList;
import com.apps.asyncTask.LoadHotel;
import com.apps.interfaces.HomeListener;
import com.apps.items.ItemRestaurant;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.apps.utils.RecyclerItemClickListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class FragmentHotelList extends Fragment {

    private Methods methods;
    private LoadHotel loadHotel;
    private AdapterHotelList adapterHotelList;
    private RecyclerView recyclerView;
    private ArrayList<ItemRestaurant> arrayList_hotel;
    private TextView textView_empty;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private InterstitialAd interstitialAd;
    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hotel_list, container, false);

        methods = new Methods(getActivity());
        interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId(getString(R.string.admob_intertestial_id));
        loadInterAd();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));

        arrayList_hotel = new ArrayList<>();
        LinearLayoutManager llm_latest = new LinearLayoutManager(getActivity());

        textView_empty = (TextView)rootView.findViewById(R.id.tv_list_empty);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.rv_hotel_list);
        recyclerView.setLayoutManager(llm_latest);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if (methods.isNetworkAvailable()) {
            loadHotelApi();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showInterAd(position);
            }
        }));

        setHasOptionsMenu(true);
        return rootView;
    }

    private void loadHotelApi() {
        loadHotel = new LoadHotel(getActivity(), new HomeListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, ArrayList<ItemRestaurant> arrayList_latest, ArrayList<ItemRestaurant> arrayList_featured) {
                if(getActivity() != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (success.equals("true")) {
                        arrayList_hotel.addAll(arrayList_latest);
                        adapterHotelList = new AdapterHotelList(getActivity(), arrayList_hotel);

                        recyclerView.setAdapter(adapterHotelList);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                    }

                    if (arrayList_latest.size() > 0) {
                        textView_empty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        textView_empty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }
        });

        loadHotel.execute(Constant.URL_HOTEL_LIST);
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
        super.onCreateOptionsMenu(menu, inflater);
    }

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

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (adapterHotelList != null) {
                if (searchView.isIconified()) {
                    recyclerView.setAdapter(adapterHotelList);
                    adapterHotelList.notifyDataSetChanged();
                } else {
                    adapterHotelList.getFilter().filter(s);
                    adapterHotelList.notifyDataSetChanged();
                }
            }
            return true;
        }
    };

    public int getPosition(int id) {
        int count=0;
        for(int i=0;i<arrayList_hotel.size();i++)
        {
            if(id == Integer.parseInt(arrayList_hotel.get(i).getId()))
            {
                count = i;
                break;
            }
        }
        return count;
    }

    private void loadInterAd() {
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void showInterAd(final int position) {
        Constant.adCount = Constant.adCount + 1;
        if(Constant.adCount % Constant.adShow == 0) {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        click(position);
                        loadInterAd();
                        super.onAdClosed();
                    }
                });
            } else {
                click(position);
            }
        } else {
            click(position);
        }
    }

    private void click(int position) {
        Intent intent = new Intent(getActivity(),HotelDetailsActivity.class);
        Constant.itemRestaurant = arrayList_hotel.get(getPosition(Integer.parseInt(adapterHotelList.getID(position))));
        startActivity(intent);
    }

    @Override
    public void onResume() {
        if(((MainActivity)getActivity()).toolbar!=null && menu != null && menu.findItem(R.id.menu_cart_search) != null) {
            methods.changeCartFrag(menu);
        }
        super.onResume();
    }
}