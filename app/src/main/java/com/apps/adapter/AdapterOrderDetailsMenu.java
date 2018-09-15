package com.apps.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.asyncTask.LoadAddMenu;
import com.apps.asyncTask.LoadDeleteMenu;
import com.apps.fooddelivery.CartActivity;
import com.apps.fooddelivery.R;
import com.apps.interfaces.LoginListener;
import com.apps.items.ItemCart;
import com.apps.items.ItemMenu;
import com.apps.items.ItemOrderMenu;
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterOrderDetailsMenu extends RecyclerView.Adapter<AdapterOrderDetailsMenu.MyViewHolder> {

    private Context context;
    private ArrayList<ItemOrderMenu> arrayList;
    private LoadAddMenu loadAddMenu;
    private LoadDeleteMenu loadDeleteMenu;
    private ProgressDialog progressDialog;
    private Methods method;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_title, textView_quantity, textView_price, textView_plus, textView_minus;
        private LinearLayout linearLayout;
        private ImageView imageView;

        MyViewHolder(View view) {
            super(view);
            textView_title = (TextView) view.findViewById(R.id.tv_orderdetails_menu_name);
            textView_quantity = (TextView) view.findViewById(R.id.tv_orderdetails_menu_qty);
            textView_price = (TextView) view.findViewById(R.id.tv_orderdetails_menu_price);
            linearLayout = (LinearLayout) view.findViewById(R.id.ll_orderdetails_cart);
            imageView = (ImageView) view.findViewById(R.id.iv_orderdetails_image);
        }
    }

    public AdapterOrderDetailsMenu(Context context, ArrayList<ItemOrderMenu> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        method = new Methods(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.loading));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_orderdetails_menulist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

//        float tot = Float.parseFloat(arrayList.get(position).getMenuPrice()) * Integer.parseInt(arrayList.get(position).getMenuQty());

        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_30));
        } else {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_10));
        }

        holder.textView_title.setText(arrayList.get(position).getMenuName());
        holder.textView_quantity.setText(arrayList.get(position).getMenuQty());
        holder.textView_price.setText(context.getString(R.string.currency) + " " + arrayList.get(position).getMenuPrice());

        Picasso.with(context)
                .load(arrayList.get(position).getMenuImage())
                .placeholder(R.drawable.placeholder_menu)
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