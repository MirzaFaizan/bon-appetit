package com.apps.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.apps.fooddelivery.R;
import com.apps.items.ItemRestaurant;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterHotelList extends RecyclerView.Adapter<AdapterHotelList.MyViewHolder> {

    private Context context;
    private ArrayList<ItemRestaurant> arrayList;
    private ArrayList<ItemRestaurant> filteredArrayList;
    private NameFilter filter;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_title, textView_address, textView_tot_rating;
        RoundedImageView imageView;
        RatingBar rating;

        MyViewHolder(View view) {
            super(view);
            textView_title = (TextView)view.findViewById(R.id.tv_list_name);
            textView_address = (TextView)view.findViewById(R.id.tv_list_address);
            textView_tot_rating = (TextView)view.findViewById(R.id.tv_latest_list_tot_rating);
            imageView = (RoundedImageView) view.findViewById(R.id.iv_list);
            rating = (RatingBar) view.findViewById(R.id.rating_list_latest);
        }
    }

    public AdapterHotelList(Context context, ArrayList<ItemRestaurant> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.filteredArrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_home, parent, false);

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

    public String getID(int pos) {
        return arrayList.get(pos).getId();
    }

    public Filter getFilter() {
        if (filter == null){
            filter  = new NameFilter();
        }
        return filter;
    }

    private class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<ItemRestaurant> filteredItems = new ArrayList<>();

                for (int i = 0, l = filteredArrayList.size(); i < l; i++) {
                    String nameList = filteredArrayList.get(i).getName();
                    if (nameList.toLowerCase().contains(constraint))
                        filteredItems.add(filteredArrayList.get(i));
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = filteredArrayList;
                    result.count = filteredArrayList.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            arrayList = (ArrayList<ItemRestaurant>) results.values;
            notifyDataSetChanged();
        }
    }
}