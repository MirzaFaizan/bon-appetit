package com.apps.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.asyncTask.LoadAddMenu;
import com.apps.fooddelivery.R;
import com.apps.interfaces.LoginListener;
import com.apps.items.ItemMenu;
import com.apps.items.ItemMenuCat;
import com.apps.items.ItemReview;
import com.apps.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterReview extends RecyclerView.Adapter<AdapterReview.MyViewHolder> {

    private Context context;
    private ArrayList<ItemReview> arrayList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_username, textView_msg;
        private LinearLayout linearLayout;
        private RatingBar ratingBar;

        MyViewHolder(View view) {
            super(view);
            textView_username = (TextView) view.findViewById(R.id.tv_reviewlist_username);
            textView_msg = (TextView) view.findViewById(R.id.tv_reviewlist_msg);
            linearLayout = (LinearLayout)view.findViewById(R.id.ll_reviewlist);
            ratingBar = (RatingBar) view.findViewById(R.id.rb_reviewlist);
        }
    }

    public AdapterReview(Context context, ArrayList<ItemReview> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_review_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_30));
        } else {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_10));
        }

        holder.textView_username.setText(arrayList.get(position).getUserName());
        holder.textView_msg.setText(arrayList.get(position).getMessage());
        holder.ratingBar.setRating(Float.parseFloat(arrayList.get(position).getRating()));
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