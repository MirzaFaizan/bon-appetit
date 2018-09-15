package com.apps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.apps.interfaces.LoginListener;
import com.apps.items.ItemAbout;
import com.apps.items.ItemUser;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadProfile extends AsyncTask<String,String,String> {

    private Context context;
    private String suc = "";
    private LoginListener loginListener;

    public LoadProfile(Context context, LoginListener loginListener) {
        this.context = context;
        this.loginListener = loginListener;
    }

    @Override
    protected void onPreExecute() {
        loginListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String json_strng = JsonUtils.getJSONString(url);
        try {
            JSONObject json = new JSONObject(json_strng);
            JSONArray c = json.getJSONArray(Constant.TAG_ROOT);
            JSONObject s = c.getJSONObject(0);
            String id = Constant.itemUser.getId();
            String name = s.getString(Constant.TAG_USER_NAME);
            String email = s.getString(Constant.TAG_USER_EMAIL);
            String mobile = s.getString(Constant.TAG_USER_PHONE);
            String address = s.getString(Constant.TAG_USER_ADDRESS);
            String image = s.getString(Constant.TAG_USER_IMAGE);
            suc = s.getString(Constant.TAG_SUCCESS);

            Constant.itemUser = new ItemUser(id,name,email,mobile,image,address);

            return "1";
        } catch (JSONException e) {
            e.printStackTrace();
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        loginListener.onEnd(s,suc);
        super.onPostExecute(s);
    }
}
