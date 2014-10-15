package com.example.hcp.home_control_prototype.Spark;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.example.hcp.home_control_prototype.Spark.Spark;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by garygraham on 2014-10-14.
 */
public class LoginTask extends AsyncTask<String, Void, ArrayList<Token>> {
    private static final String URL = "https://api.spark.io/v1/access_tokens";
    private static final String TAG = "LoginTask";
    private Spark listener;

    public LoginTask(Spark listener) {
        this.listener = listener;
    }


        @Override
        protected ArrayList<Token> doInBackground(String... credentials) {
            String username = credentials[0];
            String password = credentials[1];
            ArrayList<Token> resultantTokens= new ArrayList<Token>();

            HttpClient httpclient = new DefaultHttpClient();
            HttpUriRequest authRequest = new HttpGet(URL);

            String header = username + ":" + password;
            String base64EncodedCredentials = Base64.encodeToString(header.getBytes(), Base64.NO_WRAP);
            authRequest.addHeader("Authorization", "Basic " + base64EncodedCredentials);

            String result;
            try {
                HttpResponse response = httpclient.execute(authRequest);
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    HttpEntity entity = response.getEntity();

                    //Static parsing class
                    JSONArray jsonResult = ResponseParser.parseEntityToJSON(entity);

                    for(int i = 0; i < jsonResult.length(); i++){

                        try {
                            JSONObject jObject = jsonResult.getJSONObject(i);
                            String token_value = jObject.getString("token");
                            String expiry_date = jObject.getString("expires_at");
                            resultantTokens.add(new Token(token_value, expiry_date));
                            Log.i(TAG, "login() -> " + "Found new token! -> " + token_value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultantTokens;

    }

    @Override
    public void onPostExecute(ArrayList<Token> result) {
        listener.replaceAllTokens(result);
        if(listener.getTokens().size() > 0){
            listener.rotateToken();
        }
        listener.findDevices();
    }

}
