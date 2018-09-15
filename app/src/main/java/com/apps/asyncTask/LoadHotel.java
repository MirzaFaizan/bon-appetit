package com.apps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.apps.interfaces.HomeListener;
import com.apps.items.ItemRestaurant;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadHotel extends AsyncTask<String,String,Boolean> {

    private Context context;
    private String suc = "";
    private String msg = "";
    private HomeListener homeListener;
    private ArrayList<ItemRestaurant> arrayList;

    public LoadHotel(Context context, HomeListener homeListener) {
        this.context = context;
        this.homeListener = homeListener;
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        homeListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String json = JsonUtils.getJSONString(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                String id = c.getString(Constant.TAG_ID);
                String name = c.getString(Constant.TAG_REST_NAME);
                String address = c.getString(Constant.TAG_REST_ADDRESS);
                String image = c.getString(Constant.TAG_REST_IMAGE);
                float avg_Rate = Float.parseFloat(c.getString(Constant.TAG_REST_AVG_RATE));
                int total_rate = Integer.parseInt(c.getString(Constant.TAG_REST_TOTAL_RATE));

                ItemRestaurant itemRestaurant = new ItemRestaurant(id,name,image,address,avg_Rate,total_rate);
                arrayList.add(itemRestaurant);
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
        homeListener.onEnd(String.valueOf(s),arrayList, null);
        super.onPostExecute(s);
    }
}
