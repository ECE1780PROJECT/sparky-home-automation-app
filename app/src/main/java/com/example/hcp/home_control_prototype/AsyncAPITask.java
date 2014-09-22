package com.example.hcp.home_control_prototype;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by garygraham on 2014-09-22.
 * abstract base class for any tasks requiring asynchronous calls to the API. Subclass and
 * give it the API_PATH it needs, as well as the listener that implements OnTaskCompleted for
 * results handling.
 */
public abstract class AsyncAPITask extends AsyncTask<NameValuePair, Void, JSONArray>{

    private OnTaskCompleted listener;
    public static final String TAG = "AsyncAPITask";
    private static final String server = "https://api.spark.io/";
    private String api_path = "";
    private JSONArray jArray;


    public AsyncAPITask(OnTaskCompleted listener, String api_path){
        this.listener = listener;
        this.api_path = api_path;
    }

    @Override
    protected JSONArray doInBackground(NameValuePair... nameValuePairs) {
        String result = new String();
        try{
            //Build the URI
            URI new_fangled_uri = new URI(server + api_path);


            //Until I get the actual API up and running I'll use some dummy API.
            URI uri = new URI("https://www.wanikani.com/api/user/50f4abec6b4afdecdb892938e1193edb/user-information");

            //Building the HTTP request.
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(uri);

            Log.i(TAG, "doInBackground() -> Sending out request: " + server + api_path);

            //Getting the HTTP response
            HttpResponse response = httpClient.execute(httpGet);
            Log.i(TAG, response.toString());
            HttpEntity entity = response.getEntity();

            //Attempting to read the API response into a string.
            if(entity != null){
                try{
                    InputStream is = entity.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while((line = br.readLine())!= null){
                        sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
                    Log.i(TAG, result);
                }
                catch (Exception e){
                    Log.e(TAG, "Could not read API response! -> " + e.toString());
                }

                //Parse out the JSON Array from the response.
                try{
                   jArray = new JSONArray(result);
                   return jArray;
                }catch(JSONException e){
                    Log.e(TAG, "Error parsing the json! -> " + e.toString());
                }

            }

        }catch (Exception e){
            Log.e(TAG, "Couldn't execute HTTP request! -> " + e.toString());
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray jArray){
        this.listener.onTaskCompleted(jArray);
    }
}
