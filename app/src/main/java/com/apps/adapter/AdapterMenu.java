package com.apps.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
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
import com.apps.fooddelivery.HotelDetailsActivity;
import com.apps.fooddelivery.R;
import com.apps.interfaces.LoginListener;
import com.apps.items.ItemMenu;
import com.apps.items.ItemMenuCat;
import com.apps.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.MyViewHolder> {

    private LoadAddMenu loadAddMenu;
    private Context context;
    private ArrayList<ItemMenu> arrayList;
    private ArrayList<ItemMenu> filteredArrayList;
    private NameFilter filter;
    private ProgressDialog progressDialog;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_title, textView_desc, textView_price;
        private LinearLayout linearLayout;
        private ImageView imageView, imageView_add_menu;

        MyViewHolder(View view) {
            super(view);
            textView_title = (TextView) view.findViewById(R.id.tv_menu_name);
            textView_desc = (TextView) view.findViewById(R.id.tv_menu_desc);
            textView_price = (TextView) view.findViewById(R.id.tv_menu_price);
            linearLayout = (LinearLayout)view.findViewById(R.id.ll_menu);
            imageView = (ImageView)view.findViewById(R.id.iv_menu_image);
            imageView_add_menu = (ImageView)view.findViewById(R.id.iv_add_menu);
        }
    }

    public AdapterMenu(Context context, ArrayList<ItemMenu> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.filteredArrayList = arrayList;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.loading));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_30));
        } else {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_10));
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextViewCompat.getMaxLines(holder.textView_desc) == 2) {
                    holder.textView_desc.setMaxLines(500);
                } else {
                    holder.textView_desc.setMaxLines(2);
                }
//                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.textView_title.setText(arrayList.get(position).getName());
        holder.textView_desc.setText(arrayList.get(position).getDesc());
        holder.textView_price.setText(context.getString(R.string.currency) + " " + arrayList.get(position).getPrice());
        Picasso.with(context)
                .load(arrayList.get(position).getImage())
                .placeholder(R.drawable.placeholder_menu)
                .into(holder.imageView);

        holder.imageView_add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constant.isLogged) {
                    loadAddMenuApi(holder.getAdapterPosition());
                } else {
                    Toast.makeText(context, context.getString(R.string.not_log), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                ArrayList<ItemMenu> filteredItems = new ArrayList<>();

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

            arrayList = (ArrayList<ItemMenu>) results.values;
            notifyDataSetChanged();
        }
    }

    private void loadAddMenuApi(int pos) {
        loadAddMenu = new LoadAddMenu(context, new LoginListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if(success.equals("1")) {

                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                }

                ((HotelDetailsActivity)context).changeMenu();
            }
        });
        loadAddMenu.execute(Constant.URL_ADD_MENU_1 + Constant.itemUser.getId() + Constant.URL_ADD_MENU_2 + arrayList.get(pos).getRestId() + Constant.URL_ADD_MENU_3 + arrayList.get(pos).getId() + Constant.URL_ADD_MENU_4 + arrayList.get(pos).getName() + Constant.URL_ADD_MENU_5 + "1" + Constant.URL_ADD_MENU_6 + arrayList.get(pos).getPrice());
    }
}