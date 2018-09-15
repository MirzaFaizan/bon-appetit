package com.apps.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.fooddelivery.R;
import com.apps.items.ItemCat;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterCat extends RecyclerView.Adapter<AdapterCat.MyViewHolder> {

    private Context context;
    private Methods methods;
    private ArrayList<ItemCat> arrayList;
    int columnWidth;
    private ArrayList<ItemCat> filteredArrayList;
    private NameFilter filter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_title;
        RoundedImageView imageView;
        View views;

        public MyViewHolder(View view) {
            super(view);
            textView_title = (TextView)view.findViewById(R.id.tv_cat);
            imageView = (RoundedImageView) view.findViewById(R.id.iv_cat);
            views = (View)view.findViewById(R.id.view_cat);
        }
    }

    public AdapterCat(Context context, ArrayList<ItemCat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        methods = new Methods(context);
        this.filteredArrayList = arrayList;

        Resources r = context.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constant.GRID_PADDING, r.getDisplayMetrics());
        columnWidth = (int) ((methods.getScreenWidth() - ((Constant.NUM_OF_COLUMNS + 1) * padding)) / Constant.NUM_OF_COLUMNS);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cat, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        RelativeLayout.LayoutParams params_image = new RelativeLayout.LayoutParams(columnWidth, (columnWidth/2)-8);
//        params_image.setMargins(30,30,30,30);

//        ((MyViewHolder) holder).imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.imageView.setLayoutParams(params_image);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(columnWidth, (columnWidth/2)-6);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        holder.views.setLayoutParams(params);

        holder.textView_title.setText(arrayList.get(position).getName());
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
                ArrayList<ItemCat> filteredItems = new ArrayList<>();

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

            arrayList = (ArrayList<ItemCat>) results.values;
            notifyDataSetChanged();
        }
    }
}