package com.apps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.apps.interfaces.LoginListener;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadDeleteMenu extends AsyncTask<String,String,Boolean> {

    private Context context;
    private String suc = "";
    private String msg = "";
    private LoginListener loginListener;

    public LoadDeleteMenu(Context context, LoginListener loginListener) {
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
        String url = strings[0];
        String json = JsonUtils.getJSONString(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                suc = c.getString(Constant.TAG_SUCCESS);
                msg = c.getString(Constant.TAG_MSG);
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
