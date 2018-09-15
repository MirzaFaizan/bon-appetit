package com.apps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.apps.interfaces.LoginListener;
import com.apps.items.ItemAbout;
import com.apps.items.ItemUser;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadAbout extends AsyncTask<String,String,Boolean> {

    private Context context;
    private String suc = "";
    private String msg = "";
    private LoginListener loginListener;

    public LoadAbout(Context context, LoginListener loginListener) {
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
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.TAG_ROOT);
            JSONObject  c = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                c = jsonArray.getJSONObject(i);

                String appname = c.getString("app_name");
                String applogo = c.getString("app_logo");
                String desc = c.getString("app_description");
                String appversion = c.getString("app_version");
                String appauthor = c.getString("app_author");
                String appcontact = c.getString("app_contact");
                String email = c.getString("app_email");
                String website = c.getString("app_website");
                String privacy = c.getString("app_privacy_policy");
                String developedby = c.getString("app_developed_by");

                Constant.itemAbout = new ItemAbout(appname, applogo, desc, appversion, appauthor, appcontact, email, website, privacy, developedby);
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
        loginListener.onEnd(String.valueOf(s),suc);
        super.onPostExecute(s);
    }
}
