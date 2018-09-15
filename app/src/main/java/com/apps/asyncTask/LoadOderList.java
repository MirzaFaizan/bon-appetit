package com.apps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.apps.interfaces.MenuListener;
import com.apps.interfaces.OrderListListener;
import com.apps.items.ItemMenu;
import com.apps.items.ItemOrderList;
import com.apps.items.ItemOrderMenu;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadOderList extends AsyncTask<String,String,Boolean> {

    private OrderListListener orderListListener;
    private ArrayList<ItemOrderList> arrayList;

    public LoadOderList(OrderListListener orderListListener) {
        this.orderListListener = orderListListener;
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        orderListListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String json = JsonUtils.getJSONString(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject c = jsonArray.getJSONObject(j);

                String id = c.getString(Constant.TAG_ORDER_ID);
                String unique_id = c.getString(Constant.TAG_ORDER_UNIQUE_ID);
                String address = c.getString(Constant.TAG_ORDER_ADDRESS);
                String comment = c.getString(Constant.TAG_ORDER_COMMENT);
                String date = c.getString(Constant.TAG_ORDER_DATE);
                String status = c.getString(Constant.TAG_ORDER_STATUS);

                JSONArray jA = c.getJSONArray(Constant.TAG_ORDER_ITEMS);

                ArrayList<ItemOrderMenu> arrayList_ordermenu = new ArrayList<>();
                int totalQnt = 0;
                float totalPrice = 0;

                for (int i = 0; i < jA.length(); i++) {
                    JSONObject jO = jA.getJSONObject(i);

                    String rest_id = jO.getString(Constant.TAG_MENU_REST_ID);
                    String rest_name = jO.getString(Constant.TAG_ORDER_REST_NAME);
                    String menu_id = jO.getString(Constant.TAG_CART_MENU_ID);
                    String menu_name = jO.getString(Constant.TAG_MENU_NAME);
                    String menu_image = jO.getString(Constant.TAG_MENU_IMAGE);
                    String menu_qty = jO.getString(Constant.TAG_MENU_QYT);
                    String menu_price = jO.getString(Constant.TAG_MENU_PRICE);
                    String menu_total_price = jO.getString(Constant.TAG_MENU_TOTAL_PRICE);

                    totalPrice = totalPrice + Float.parseFloat(menu_total_price);
                    totalQnt = totalQnt + Integer.parseInt(menu_qty);

                    ItemOrderMenu itemOrderMenu = new ItemOrderMenu(rest_id,rest_name,menu_id,menu_name,menu_image,menu_qty,menu_price,menu_total_price);
                    arrayList_ordermenu.add(itemOrderMenu);
                }

                ItemOrderList itemOrderList = new ItemOrderList(id,unique_id,address,comment,date,String.valueOf(totalQnt),String.valueOf(totalPrice),status,arrayList_ordermenu);
                arrayList.add(itemOrderList);
            }

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        orderListListener.onEnd(String.valueOf(s),arrayList);
        super.onPostExecute(s);
    }
}
