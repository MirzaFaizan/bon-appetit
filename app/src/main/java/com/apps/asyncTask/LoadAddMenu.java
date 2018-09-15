package com.apps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.webkit.URLUtil;

import com.apps.interfaces.LoginListener;
import com.apps.interfaces.MenuListener;
import com.apps.items.ItemMenu;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadAddMenu extends AsyncTask<String,String,Boolean> {

    private Context context;
    private String suc = "";
    private String msg = "";
    private LoginListener loginListener;

    public LoadAddMenu(Context context, LoginListener loginListener) {
        this.context = context;
        this.loginListener = loginListener;
    }

    @Override
    protected void onPreExecute() {
        loginListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0].replace(" ","%20");
        String json = JsonUtils.getJSONString(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                suc = c.getString(Constant.TAG_SUCCESS);
                msg = c.getString(Constant.TAG_MSG);
                if(c.has(Constant.TAG_CART_COUNT)) {
                    Constant.menuCount = Integer.parseInt(c.getString(Constant.TAG_CART_COUNT));
                }
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
        loginListener.onEnd(suc,msg);
        super.onPostExecute(s);
    }
}
