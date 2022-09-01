package com.ronnelrazo.fatteningfeedingapp.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSON_AsyncTask extends AsyncTask<Void,Void,String> {

    private Context context;
    private String murl;

    public JSON_AsyncTask(Context context,String murl){
        this.context = context;
        this.murl = murl;
    }

    @Override
    protected String doInBackground(Void... voids) {
            String current = null;
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try{
                    url = new URL(murl);
                    urlConnection = (HttpURLConnection)  url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestProperty("Accept", "*/*");

                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    int data = inputStreamReader.read();
                    while (data != 1){
                        current += (char) data;
                        data = inputStreamReader.read();
                    }
                    return current;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        return  current;


    }

    @Override
    protected void onPostExecute(String s) {
       try {
           JSONObject jsonResponse = new JSONObject(s);
            boolean success = jsonResponse.getBoolean("success");
            JSONArray array = jsonResponse.getJSONArray("data");
                if (success) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Toast.makeText(context, object.getString("ORG_CODE"), Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                     Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
}


