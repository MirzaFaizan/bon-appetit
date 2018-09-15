package com.apps.fooddelivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.adapter.AdapterLatestHome;
import com.apps.asyncTask.LoadHome;
import com.apps.asyncTask.LoadHotel;
import com.apps.interfaces.HomeListener;
import com.apps.items.ItemRestaurant;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.apps.utils.RecyclerItemClickListener;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    private Methods methods;
    private LoadHome loadHome;
    private LoadHotel loadHotel;
    private AdapterLatestHome adapterLatestHome, adapterTopRatedHome;
    private ImagePagerAdapter pagerAdapter;
    private EnchantedViewPager viewPager_home;
    private RecyclerView recyclerView_latest, recyclerView_toprated;
    private ArrayList<ItemRestaurant> arrayList_lat,arrayList_toprated,arrayList_feat;
    private TextView textView_latest_empty, textView_toprated_empty;
    private AppCompatButton button_more_latest, button_more_toprated;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        methods = new Methods(getActivity());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));

        arrayList_feat = new ArrayList<>();
        arrayList_lat = new ArrayList<>();
        arrayList_toprated = new ArrayList<>();

        pagerAdapter = new ImagePagerAdapter();

        viewPager_home = (EnchantedViewPager)rootView.findViewById(R.id.vp_home);
        viewPager_home.useScale();

        LinearLayoutManager llm_latest = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        button_more_latest = (AppCompatButton) rootView.findViewById(R.id.button_home_latest);
        button_more_toprated = (AppCompatButton) rootView.findViewById(R.id.button_home_toprated);
        textView_latest_empty = (TextView)rootView.findViewById(R.id.textView_latest_empty);
        textView_toprated_empty = (TextView)rootView.findViewById(R.id.textView_toprated_empty);

        recyclerView_latest = (RecyclerView)rootView.findViewById(R.id.rv_home_latest);
        recyclerView_latest.setLayoutManager(llm_latest);
        recyclerView_latest.setItemAnimator(new DefaultItemAnimator());
        recyclerView_latest.setHasFixedSize(true);

        LinearLayoutManager llm_toprated = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_toprated = (RecyclerView)rootView.findViewById(R.id.rv_home_toprated);
        recyclerView_toprated.setLayoutManager(llm_toprated);
        recyclerView_toprated.setItemAnimator(new DefaultItemAnimator());
        recyclerView_toprated.setHasFixedSize(true);

        if (methods.isNetworkAvailable()) {
            loadHomeApi();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
        }

        button_more_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.arrayList_latest.clear();
                Constant.arrayList_latest.addAll(arrayList_lat);
                loadFrag(getString(R.string.latest),"latest");
            }
        });

        button_more_toprated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.arrayList_latest.clear();
                Constant.arrayList_latest.addAll(arrayList_toprated);
                loadFrag(getString(R.string.top_rated),"top");
            }
        });

        recyclerView_latest.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),HotelDetailsActivity.class);
                Constant.itemRestaurant = arrayList_lat.get(position);
                startActivity(intent);
            }
        }));

        recyclerView_toprated.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),HotelDetailsActivity.class);
                Constant.itemRestaurant = arrayList_toprated.get(position);
                startActivity(intent);
            }
        }));

        return rootView;
    }

    private void loadHomeApi() {
        loadHome = new LoadHome(getActivity(), new HomeListener() {
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
                        arrayList_lat.addAll(arrayList_latest);
                        arrayList_feat.addAll(arrayList_featured);
                        adapterLatestHome = new AdapterLatestHome(getActivity(), arrayList_lat);

                        recyclerView_latest.setAdapter(adapterLatestHome);
                        viewPager_home.setAdapter(pagerAdapter);
                        if (arrayList_lat.size() >= 3) {
                            viewPager_home.setCurrentItem(1);
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                    }

                    if (arrayList_latest.size() > 0) {
                        textView_latest_empty.setVisibility(View.GONE);
                    } else {
                        textView_latest_empty.setVisibility(View.VISIBLE);
                    }

                    loadTopRatedApi();
                }
            }
        });

        loadHome.execute(Constant.URL_HOME);
    }

    private void loadTopRatedApi() {
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
                        arrayList_toprated.addAll(arrayList_latest);
                        adapterTopRatedHome = new AdapterLatestHome(getActivity(), arrayList_toprated);
                        recyclerView_toprated.setAdapter(adapterTopRatedHome);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                    }

                    if (arrayList_toprated.size() > 0) {
                        textView_toprated_empty.setVisibility(View.GONE);
                    } else {
                        textView_toprated_empty.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        loadHotel.execute(Constant.URL_TOP_RATED);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        ImagePagerAdapter() {
            // TODO Auto-generated constructor stub

            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrayList_feat.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View imageLayout = inflater.inflate(R.layout.viewpager_home, container, false);
            assert imageLayout != null;
            RoundedImageView imageView = (RoundedImageView) imageLayout.findViewById(R.id.iv_pager_home);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading_home);
            TextView title = (TextView) imageLayout.findViewById(R.id.tv_pager_home_title);
            TextView address = (TextView) imageLayout.findViewById(R.id.tv_pager_home_address);
            RelativeLayout rl = (RelativeLayout)imageLayout.findViewById(R.id.rl_homepager);

            title.setText(arrayList_feat.get(position).getName());
            address.setText(arrayList_feat.get(position).getAddress());

            Picasso.with(getActivity())
                    .load(arrayList_feat.get(position).getImage())
                    .placeholder(R.mipmap.app_icon)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            spinner.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            spinner.setVisibility(View.GONE);
                        }
                    });

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(methods.isNetworkAvailable()) {
//                    showInter(position);
                        Intent intent = new Intent(getActivity(),HotelDetailsActivity.class);
                        Constant.itemRestaurant = arrayList_feat.get(position);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_conn), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            imageLayout.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);

            container.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void loadFrag(String title, String tag) {
        FragmentManager fm = getActivity().getSupportFragmentManager();;
        FragmentLatest fh = new FragmentLatest();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.frame_nav, fh, tag);
        ft.commit();
        ((MainActivity)getActivity()).toolbar.setTitle(title);
    }
}