package com.apps.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.fooddelivery.R;
import com.apps.items.ItemMenuCat;

import java.util.ArrayList;


public class AdapterMenuCat extends RecyclerView.Adapter<AdapterMenuCat.MyViewHolder> {

    private Context context;
    private ArrayList<ItemMenuCat> arrayList;
    private ArrayList<ItemMenuCat> filteredArrayList;
    private NameFilter filter;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_title;
        private LinearLayout linearLayout;

        MyViewHolder(View view) {
            super(view);
            textView_title = (TextView) view.findViewById(R.id.tv_menucat_name);
            linearLayout = (LinearLayout) view.findViewById(R.id.ll_menucat);
        }
    }

    public AdapterMenuCat(Context context, ArrayList<ItemMenuCat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.filteredArrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_menucat, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textView_title.setText(arrayList.get(position).getName());
        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_30));
        } else {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_10));
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
                ArrayList<ItemMenuCat> filteredItems = new ArrayList<>();

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

            arrayList = (ArrayList<ItemMenuCat>) results.values;
            notifyDataSetChanged();
        }
    }
}