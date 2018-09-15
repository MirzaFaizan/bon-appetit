package com.apps.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.apps.fooddelivery.R;
import com.apps.items.ItemRestaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterLatestHome extends RecyclerView.Adapter<AdapterLatestHome.MyViewHolder> {

    private Context context;
    private ArrayList<ItemRestaurant> arrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_title, textView_address, textView_tot_rating;
        ImageView imageView;
        RatingBar rating;

        public MyViewHolder(View view) {
            super(view);
            textView_title = (TextView)view.findViewById(R.id.tv_latest_home_name);
            textView_address = (TextView)view.findViewById(R.id.tv_latest_home_address);
            textView_tot_rating = (TextView)view.findViewById(R.id.tv_latest_home_tot_rating);
            imageView = (ImageView) view.findViewById(R.id.iv_latest_home);
            rating = (RatingBar) view.findViewById(R.id.rating_home_latest);
        }
    }

    public AdapterLatestHome(Context context, ArrayList<ItemRestaurant> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_latest_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textView_title.setText(arrayList.get(position).getName());
        holder.textView_address.setText(arrayList.get(position).getAddress());
        holder.textView_tot_rating.setText("("+arrayList.get(position).getTotalRating()+")");
        holder.rating.setRating(arrayList.get(position).getAvgRatings());
        Picasso.with(context)
                .load(arrayList.get(position).getImage())
                .into(holder.imageView);

    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}