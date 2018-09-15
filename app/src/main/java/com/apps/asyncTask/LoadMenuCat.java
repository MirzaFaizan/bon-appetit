package com.apps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.apps.interfaces.HomeListener;
import com.apps.interfaces.MenuCatListener;
import com.apps.items.ItemMenuCat;
import com.apps.items.ItemRestaurant;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadMenuCat extends AsyncTask<String,String,Boolean> {

    private Context context;
    private String suc = "";
    private String msg = "";
    private MenuCatListener menuCatListener;
    private ArrayList<ItemMenuCat> arrayList;

    public LoadMenuCat(Context context, MenuCatListener menuCatListener) {
        this.context = context;
        this.menuCatListener = menuCatListener;
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        menuCatListener.onStart();
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
                String hotel_id = c.getString(Constant.TAG_REST_ID);

                ItemMenuCat itemMenuCat = new ItemMenuCat(id,name,hotel_id,null);
                arrayList.add(itemMenuCat);
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
        menuCatListener.onEnd(String.valueOf(s),arrayList);
        super.onPostExecute(s);
    }
}
