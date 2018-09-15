package com.apps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.apps.interfaces.MenuCatListener;
import com.apps.interfaces.MenuListener;
import com.apps.items.ItemMenu;
import com.apps.items.ItemMenuCat;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadMenu extends AsyncTask<String,String,Boolean> {

    private Context context;
    private String suc = "";
    private String msg = "";
    private MenuListener menuListener;
    private ArrayList<ItemMenu> arrayList;

    public LoadMenu(Context context, MenuListener menuListener) {
        this.context = context;
        this.menuListener = menuListener;
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        menuListener.onStart();
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

                String id = c.getString(Constant.TAG_MENU_ID);
                String name = c.getString(Constant.TAG_MENU_NAME);
                String desc = c.getString(Constant.TAG_MENU_DESC);
                String price = c.getString(Constant.TAG_MENU_PRICE);
                String image = c.getString(Constant.TAG_MENU_IMAGE);
                String cat_id = c.getString(Constant.TAG_MENU_CAT);
                String res_id = c.getString(Constant.TAG_MENU_REST_ID);

                ItemMenu itemMenu = new ItemMenu(id,name,image,desc,price,res_id,cat_id);
                arrayList.add(itemMenu);
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
        menuListener.onEnd(String.valueOf(s),arrayList);
        super.onPostExecute(s);
    }
}
