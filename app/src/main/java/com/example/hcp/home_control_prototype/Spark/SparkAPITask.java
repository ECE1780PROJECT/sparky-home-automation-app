package com.example.hcp.home_control_prototype.Spark;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hcp.home_control_prototype.OnTaskCompleted;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by garygraham on 2014-09-22.
 * abstract base class for any tasks requiring asynchronous calls to the API. Subclass and
 * give it the API_PATH it needs, as well as the listener that implements OnTaskCompleted for
 * results handling.
 */
public  class SparkAPITask extends AsyncTask<NameValuePair, Void, JSONArray>{

    private final Context context;
    private final String deviceID;
    protected OnTaskCompleted listener;
    public static final String TAG = "SparkAPITask";
    private static final String server = "https://api.spark.io/v1/devices/";
    private String api_path = "";
    private JSONArray jArray;


    public SparkAPITask(String deviceID, String api_path, OnTaskCompleted listener){
        this.listener = listener;
        this.api_path = api_path;
        this.deviceID = deviceID;
        this.context = null;
    }
    public SparkAPITask(String deviceID,  String api_path, OnTaskCompleted listener, Context context){
        this.listener = listener;
        this.api_path = api_path;
        this.deviceID = deviceID;
        this.context = context;
    }

    @Override
    protected JSONArray doInBackground(NameValuePair... nameValuePairs) {
        try{
            //Build the URI
            URI uri = new URI(server + deviceID + "/" + api_path);

            //Building the HTTP request.
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();

            //woo secret access token written in plain code.
            //my god this is gross. Please fix at some point.
            parameters.add(new BasicNameValuePair("access_token", Spark.getInstance().getCurrentToken().toString()));

            //in case we want to support posting data in the future
            for(NameValuePair p : nameValuePairs)
            {
                parameters.add(p);
            }
            Log.i(TAG, "doInBackground() ->Sending out request: " + uri.toString());

            //tacking the access code and other parameters onto the request.
            httpPost.setEntity(new UrlEncodedFormEntity(parameters));
            
            //Getting the HTTP response
            HttpResponse response = httpClient.execute(httpPost);
            Log.i(TAG, response.toString());
            HttpEntity entity = response.getEntity();

            //Attempting to read the API response into a JSON Array for returning.
            if(entity != null){
                jArray = ResponseParser.parseEntityToJSON(entity);
                return jArray;
            }

        }catch (Exception e){
            Log.e(TAG, "Couldn't execute HTTP request! -> " + e.toString());
            return null;
        }
        return null;
    }



    @Override
    protected void onPostExecute(JSONArray jArray){
            this.listener.onTaskCompleted(jArray, this.context);
    }
}
