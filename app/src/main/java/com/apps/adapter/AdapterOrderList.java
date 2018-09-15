package com.apps.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.asyncTask.LoadAddMenu;
import com.apps.fooddelivery.R;
import com.apps.interfaces.LoginListener;
import com.apps.items.ItemMenu;
import com.apps.items.ItemOrderList;
import com.apps.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterOrderList extends RecyclerView.Adapter<AdapterOrderList.MyViewHolder> {

    private Context context;
    private ArrayList<ItemOrderList> arrayList;
    private ArrayList<ItemOrderList> filteredArrayList;
    private NameFilter filter;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_unique_id, textView_qty, textView_totalprice, textView_date, textView_address, textView_status, textView_time;
        private LinearLayout ll;

        MyViewHolder(View view) {
            super(view);
            textView_unique_id = (TextView) view.findViewById(R.id.tv_orderlist_uniqueid);
            textView_qty = (TextView) view.findViewById(R.id.tv_orderlist_quantity);
            textView_totalprice = (TextView) view.findViewById(R.id.tv_orderlist_total);
            textView_date = (TextView) view.findViewById(R.id.tv_orderlist_date);
            textView_time = (TextView) view.findViewById(R.id.tv_orderlist_time);
            textView_address = (TextView) view.findViewById(R.id.tv_orderlist_address);
            textView_status = (TextView) view.findViewById(R.id.tv_orderlist_status);
            ll = (LinearLayout)view.findViewById(R.id.ll_orderlist);
        }
    }

    public AdapterOrderList(Context context, ArrayList<ItemOrderList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.filteredArrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_orderlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (position % 2 == 0) {
            holder.ll.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_30));
        } else {
            holder.ll.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_10));
        }

        holder.textView_unique_id.setText(arrayList.get(position).getUniqueId());
        holder.textView_qty.setText(context.getString(R.string.qty) + " " + arrayList.get(position).getTotalQuantity());
        holder.textView_totalprice.setText(context.getString(R.string.price) + " " + context.getString(R.string.currency) + " " + arrayList.get(position).getTotalBill());
        holder.textView_address.setText(arrayList.get(position).getAddress());
        holder.textView_status.setText(arrayList.get(position).getStatus());

        holder.textView_date.setText(arrayList.get(position).getDate().split(" ")[0]);
        holder.textView_time.setText(arrayList.get(position).getDate().split(" ")[1]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (arrayList.get(position).getStatus().equals("Pending")) {
                holder.textView_status.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_round_red));
            } else if (arrayList.get(position).getStatus().equals("Process")) {
                holder.textView_status.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_round_orange));
            } else {
                holder.textView_status.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_round_green));
            }
        } else {
            if (arrayList.get(position).getStatus().equals("Pending")) {
                holder.textView_status.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_round_red));
            } else if (arrayList.get(position).getStatus().equals("Process")) {
                holder.textView_status.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_round_orange));
            } else {
                holder.textView_status.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_round_green));
            }
        }
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
        if (filter == null) {
            filter = new NameFilter();
        }
        return filter;
    }

    private class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<ItemOrderList> filteredItems = new ArrayList<>();

                for (int i = 0, l = filteredArrayList.size(); i < l; i++) {
                    String nameList = filteredArrayList.get(i).getId();
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

            arrayList = (ArrayList<ItemOrderList>) results.values;
            notifyDataSetChanged();
        }
    }
}