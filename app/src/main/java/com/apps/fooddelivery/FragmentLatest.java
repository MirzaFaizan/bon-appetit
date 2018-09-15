package com.apps.fooddelivery;

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

import com.apps.adapter.AdapterHotelList;
import com.apps.items.ItemRestaurant;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.apps.utils.RecyclerItemClickListener;

import java.util.ArrayList;

public class FragmentLatest extends Fragment {

    private Methods methods;
    private AdapterHotelList adapterHotelList;
    private RecyclerView recyclerView;
    private ArrayList<ItemRestaurant> arrayList_hotel;
    private SearchView searchView;
    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hotel_list, container, false);

        methods = new Methods(getActivity());

        arrayList_hotel = new ArrayList<>();
        LinearLayoutManager llm_latest = new LinearLayoutManager(getActivity());

        TextView textView_empty = (TextView) rootView.findViewById(R.id.tv_list_empty);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_hotel_list);
        recyclerView.setLayoutManager(llm_latest);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), HotelDetailsActivity.class);
                Constant.itemRestaurant = arrayList_hotel.get(getPosition(Integer.parseInt(adapterHotelList.getID(position))));
                startActivity(intent);
            }
        }));


        arrayList_hotel.addAll(Constant.arrayList_latest);
        adapterHotelList = new AdapterHotelList(getActivity(), arrayList_hotel);

        recyclerView.setAdapter(adapterHotelList);

        if (arrayList_hotel.size() > 0) {
            textView_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            textView_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

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
        super.onCreateOptionsMenu(menu, inflater);
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
        int count = 0;
        for (int i = 0; i < arrayList_hotel.size(); i++) {
            if (id == Integer.parseInt(arrayList_hotel.get(i).getId())) {
                count = i;
                break;
            }
        }
        return count;
    }

    @Override
    public void onResume() {
        if(((MainActivity)getActivity()).toolbar!=null && menu != null && menu.findItem(R.id.menu_cart_search) != null) {
            methods.changeCartFrag(menu);
        }
        super.onResume();
    }
}