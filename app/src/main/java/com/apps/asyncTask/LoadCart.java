package com.apps.asyncTask;

import android.os.AsyncTask;

import com.apps.interfaces.CartListener;
import com.apps.items.ItemCart;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadCart extends AsyncTask<String,String,Boolean> {

    private String suc = "";
    private String msg = "";
    private CartListener cartListener;
    private ArrayList<ItemCart> arrayList;

    public LoadCart( CartListener cartListener) {
        this.cartListener = cartListener;
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        cartListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String json = JsonUtils.getJSONString(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);
            JSONObject c = null;

            try {

                for (int i = 0; i < jsonArray.length(); i++) {
                    c = jsonArray.getJSONObject(i);

                    String cartid = c.getString(Constant.TAG_CART_ID);
                    String rest_id = c.getString(Constant.TAG_MENU_REST_ID );
                    String menu_id = c.getString(Constant.TAG_CART_MENU_ID);
                    String menu_name = c.getString(Constant.TAG_MENU_NAME);
                    String menu_image = c.getString(Constant.TAG_MENU_IMAGE);
                    String menu_qty = c.getString(Constant.TAG_MENU_QYT);
                    String menu_price = c.getString(Constant.TAG_MENU_PRICE);

                    ItemCart itemCart = new ItemCart(cartid, rest_id, menu_id, menu_name, menu_image, menu_qty, menu_price,menu_qty);
                    arrayList.add(itemCart);
                }
            } catch (Exception e) {
                msg = c.getString(Constant.TAG_MSG);
                e.printStackTrace();
                return true;
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
        cartListener.onEnd(String.valueOf(s),msg,arrayList);
        super.onPostExecute(s);
    }
}
