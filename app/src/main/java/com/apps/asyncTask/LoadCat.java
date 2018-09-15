package com.apps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.apps.interfaces.CategoryListener;
import com.apps.interfaces.HomeListener;
import com.apps.items.ItemCat;
import com.apps.items.ItemRestaurant;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadCat extends AsyncTask<String,String,Boolean> {

    private Context context;
    private String suc = "";
    private String msg = "";
    CategoryListener categoryListener;
    private ArrayList<ItemCat> arrayList_cat;

    public LoadCat(Context context, CategoryListener categoryListener) {
        this.context = context;
        this.categoryListener = categoryListener;
        arrayList_cat = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        categoryListener.onStart();
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

                String id = c.getString(Constant.TAG_CAT_ID);
                String name = c.getString(Constant.TAG_CAT_NAME);
                String image = c.getString(Constant.TAG_CAT_IMAGE);

                ItemCat itemCat = new ItemCat(id,name,image);
                arrayList_cat.add(itemCat);
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
        categoryListener.onEnd(String.valueOf(s),msg,arrayList_cat);
        super.onPostExecute(s);
    }
}
