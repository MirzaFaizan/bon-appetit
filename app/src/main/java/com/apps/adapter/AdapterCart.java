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
import com.apps.utils.Constant;
import com.apps.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterCart extends RecyclerView.Adapter<AdapterCart.MyViewHolder> {

    private Context context;
    private ArrayList<ItemCart> arrayList;
    private LoadAddMenu loadAddMenu;
    private LoadDeleteMenu loadDeleteMenu;
    private ProgressDialog progressDialog;
    private Methods method;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_title, textView_quantity, textView_price, textView_plus, textView_minus;
        private LinearLayout linearLayout;
        private ImageView imageView, imageView_delete_menu, imageView_update;

        MyViewHolder(View view) {
            super(view);
            textView_title = (TextView) view.findViewById(R.id.tv_cartlist_menu_name);
            textView_quantity = (TextView) view.findViewById(R.id.tv_menu_qty);
            textView_price = (TextView) view.findViewById(R.id.tv_cartlist_menu_price);
            textView_minus = (TextView) view.findViewById(R.id.tv_cartlist_minus);
            textView_plus = (TextView) view.findViewById(R.id.tv_cartlist_plus);
            linearLayout = (LinearLayout) view.findViewById(R.id.ll_cart);
            imageView = (ImageView) view.findViewById(R.id.iv_cartlist_image);
            imageView_delete_menu = (ImageView) view.findViewById(R.id.iv_cart_delete_menu);
            imageView_update = (ImageView) view.findViewById(R.id.iv_update_cartlist);
        }
    }

    public AdapterCart(Context context, ArrayList<ItemCart> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        method = new Methods(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.loading));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cart, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        float tot = Float.parseFloat(arrayList.get(position).getMenuPrice()) * Integer.parseInt(arrayList.get(position).getMenuQty());

        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_30));
        } else {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_10));
        }


        if (arrayList.get(holder.getAdapterPosition()).getMenuQty().equals(arrayList.get(holder.getAdapterPosition()).getMemuTempQty())) {
            holder.imageView_update.setVisibility(View.GONE);
        } else {
            holder.imageView_update.setVisibility(View.VISIBLE);
        }

        holder.textView_title.setText(arrayList.get(position).getMenuName());
        holder.textView_quantity.setText(arrayList.get(position).getMemuTempQty());
//        holder.textView_price.setText(context.getString(R.string.currency) + " " + arrayList.get(position).getMenuPrice());
        holder.textView_price.setText(context.getString(R.string.currency) + " " + tot);
        Picasso.with(context)
                .load(arrayList.get(position).getMenuImage())
                .placeholder(R.drawable.placeholder_menu)
                .into(holder.imageView);

//        holder.imageView_delete_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadAddMenuApi(holder.getAdapterPosition());
//            }
//        });

        holder.textView_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(arrayList.get(holder.getAdapterPosition()).getMemuTempQty());
                count = count + 1;
                holder.textView_quantity.setText("" + count);
                arrayList.get(holder.getAdapterPosition()).setMemuTempQty(holder.textView_quantity.getText().toString());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.textView_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(arrayList.get(holder.getAdapterPosition()).getMemuTempQty());
                if (count > 1) {
                    count = count - 1;
                    holder.textView_quantity.setText("" + count);
                    arrayList.get(holder.getAdapterPosition()).setMemuTempQty(holder.textView_quantity.getText().toString());
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });

        holder.imageView_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (method.isNetworkAvailable()) {
                    loadAddMenu = new LoadAddMenu(context, new LoginListener() {
                        @Override
                        public void onStart() {
                            progressDialog.show();
                        }

                        @Override
                        public void onEnd(String success, String message) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            if (success.equals("1")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, context.getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                            }

                            arrayList.get(holder.getAdapterPosition()).setMenuQty(arrayList.get(holder.getAdapterPosition()).getMemuTempQty());
                            //                        notifyDataSetChanged();
                            notifyItemChanged(holder.getAdapterPosition());
                            float total = 0;
                            for (int i = 0; i < arrayList.size(); i++) {
                                total = total + (Float.parseFloat(arrayList.get(i).getMenuPrice()) * Float.parseFloat(arrayList.get(i).getMenuQty()));
                            }
                            CartActivity.textView_total.setText(context.getString(R.string.currency) + " " + total);
                            if(arrayList.size() == 0) {
                                ((CartActivity) context).hideView();
                            }
                        }
                    });
                    loadAddMenu.execute(Constant.URL_ADD_MENU_1 + Constant.itemUser.getId() + Constant.URL_ADD_MENU_2 + arrayList.get(holder.getAdapterPosition()).getRestId() + Constant.URL_ADD_MENU_3 + arrayList.get(holder.getAdapterPosition()).getMenuId() + Constant.URL_ADD_MENU_4 + arrayList.get(holder.getAdapterPosition()).getMenuName() + Constant.URL_ADD_MENU_5 + arrayList.get(holder.getAdapterPosition()).getMemuTempQty() + Constant.URL_ADD_MENU_6 + arrayList.get(holder.getAdapterPosition()).getMenuPrice());
                } else {
                    Toast.makeText(context, context.getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
                }
            }
        });

            holder.imageView_delete_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (method.isNetworkAvailable()) {
                    loadDeleteMenu = new LoadDeleteMenu(context, new LoginListener() {
                        @Override
                        public void onStart() {
                            progressDialog.show();
                        }

                        @Override
                        public void onEnd(String success, String message) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            if (success.equals("1")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                arrayList.remove(holder.getAdapterPosition());
                                //                        notifyDataSetChanged();
                                notifyItemRemoved(holder.getAdapterPosition());

                                float total = 0;
                                for (int i = 0; i < arrayList.size(); i++) {
                                    total = total + (Float.parseFloat(arrayList.get(i).getMenuPrice()) * Float.parseFloat(arrayList.get(i).getMenuQty()));
                                    CartActivity.textView_total.setText("" + total);
                                }
                                Constant.menuCount = Constant.menuCount - 1;
                                CartActivity.textView_total.setText(context.getString(R.string.currency) + " " + total);

                            } else {
                                Toast.makeText(context, context.getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    loadDeleteMenu.execute(Constant.URL_DELETE_ITEM_CART + arrayList.get(holder.getAdapterPosition()).getId());
                } else {
                    Toast.makeText(context, context.getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
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
}