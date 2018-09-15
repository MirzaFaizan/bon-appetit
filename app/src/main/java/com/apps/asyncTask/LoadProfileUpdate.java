package com.apps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apps.interfaces.LoginListener;
import com.apps.items.ItemUser;
import com.apps.utils.Constant;
import com.apps.utils.JsonUtils;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoadProfileUpdate extends AsyncTask<String,String,String> {

    private Context context;
    String suc = "";
    String msg = "", nm,email,password, phone, address, imagePath;
    private LoginListener loginListener;

    public LoadProfileUpdate(Context context, LoginListener loginListener) {
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
        nm = strings[0];
        email = strings[1];
        password = strings[2];
        phone = strings[3];
        address = strings[4];
        imagePath = strings[5];
//        String json_strng = JsonUtils.getJSONString(strings[0]);

        try {
            uploadFile(imagePath,0);

            return "1";
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

    public int uploadFile(String sourceFileUri, int poss) {
        final String upLoadServerUri = Constant.URL_PROFILE_EDIT;

        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        String as[] = null;

        as = new String[1];
        as[0] = sourceFile.toString();


//        if (!sourceFile.isFile()) {
//            Log.e("uploadFile", getResources().getString(R.string.source_file_not_found));
//            progressDialog.dismiss();
//            return 0;
//        }
        try { // open a URL connection to the Servlet

            URL url = new URL(upLoadServerUri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
//            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            //*********filename first is php base filename
//            conn.setRequestProperty("user_image", sourceFile.getAbsolutePath());

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=user_id;" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(Constant.itemUser.getId());
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=name;" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.write(nm.getBytes("UTF-8"), 0, nm.getBytes("UTF-8").length);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=email;" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(email);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=password;" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(password);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=phone;" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(phone);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=address;" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(address);
            dos.writeBytes(lineEnd);

            if(!imagePath.equals("")) {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                //********filename first is php base filename
                dos.writeBytes("Content-Disposition: form-data; name=user_image;filename=" + sourceFile.getAbsolutePath() + "" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            }

            // Responses from the server (code and message)
//            serverResponseCode = conn.getResponseCode();
//            String serverResponseMessage = conn.getResponseMessage();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + '\n');
            }
            String jsonString = stringBuilder.toString();
            Log.e("json", jsonString);
            JSONObject jsonObj = new JSONObject(jsonString);

            try {
                JSONArray jsonArray = jsonObj.getJSONArray(Constant.TAG_ROOT);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                String id = Constant.itemUser.getId();
                String name = jsonObject.getString(Constant.TAG_USER_NAME);
                String email = jsonObject.getString(Constant.TAG_USER_EMAIL);
                String mobile = jsonObject.getString(Constant.TAG_USER_PHONE);
                String address = jsonObject.getString(Constant.TAG_USER_ADDRESS);
                String image = jsonObject.getString(Constant.TAG_USER_IMAGE);
                suc = jsonObject.getString(Constant.TAG_SUCCESS);

                Constant.itemUser = new ItemUser(id,name,email,mobile,image,address);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
