package com.apps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.apps.interfaces.LoginListener;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadRegister extends AsyncTask<String,String,String> {

    private String msg = "";
    private String suc = "";
    private LoginListener loginListener;

    public LoadRegister(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Override
    protected void onPreExecute() {
        loginListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String name = strings[0];
        String email = strings[1];
        String pass = strings[2];
        String phone = strings[3];

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("name",name));
        nameValuePairs.add(new BasicNameValuePair("email",email));
        nameValuePairs.add(new BasicNameValuePair("password",pass));
        nameValuePairs.add(new BasicNameValuePair("phone",phone));
        nameValuePairs.add(new BasicNameValuePair("user_image",""));

        try {
            JSONObject jsonObject = JsonUtils.makeHttpRequest(Constant.URL_REGISTER,"POST",nameValuePairs);
//            JSONObject jsonObject = new JSONObject(json_strng);
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.TAG_ROOT);
            JSONObject obj = jsonArray.getJSONObject(0);

            msg = obj.getString(Constant.TAG_MSG);
            suc = obj.getString(Constant.TAG_SUCCESS);

            return "1";

        } catch (JSONException e) {
            e.printStackTrace();
            return "0";
        } catch (Exception ee) {
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        loginListener.onEnd(s,msg);
        super.onPostExecute(s);
    }
}
