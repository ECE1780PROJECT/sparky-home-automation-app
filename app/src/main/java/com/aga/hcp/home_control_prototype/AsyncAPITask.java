package com.aga.hcp.home_control_prototype;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by garygraham on 2014-09-22.
 * abstract base class for any tasks requiring asynchronous calls to the API. Subclass and
 * give it the API_PATH it needs, as well as the listener that implements OnTaskCompleted for
 * results handling.
 */
public abstract class AsyncAPITask extends AsyncTask<NameValuePair, Void, JSONArray>{

    private final Context context;
    protected OnTaskCompleted listener;
    public static final String TAG = "AsyncAPITask";
    private static final String server = "https://api.spark.io/v1/devices/53ff70066667574817202567/";
    private String api_path = "";
    private JSONArray jArray;


    public AsyncAPITask(OnTaskCompleted listener, String api_path){
        this.listener = listener;
        this.api_path = api_path;
        this.context = null;
    }
    public AsyncAPITask(OnTaskCompleted listener, String api_path, Context context){
        this.listener = listener;
        this.api_path = api_path;
        this.context = context;
    }

    @Override
    protected JSONArray doInBackground(NameValuePair... nameValuePairs) {
        String result = new String();
        try{
            //Build the URI
            URI uri = new URI(server + api_path);

            //Building the HTTP request.
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();

            //woo secret access token written in plain code.
            parameters.add(new BasicNameValuePair("access_token", "49ebfe28f45764750d954eda9a157352949f0c8b"));

            //in case we want to support posting data in the future
            for(NameValuePair p : nameValuePairs)
            {
                parameters.add(p);
            }
            Log.i(TAG, "doInBackground() ->Sending out request: " + server + api_path);

            //tacking the access code and other parameters onto the request.
            httpPost.setEntity(new UrlEncodedFormEntity(parameters));
            
            //Getting the HTTP response
            HttpResponse response = httpClient.execute(httpPost);
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
                   jArray = new JSONArray();
                   JSONObject jObject = new JSONObject(result);
                   jArray.put(jObject);
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
    private String parseEntity(HttpEntity entity){
        String result = null;
        try{
            InputStream is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine())!= null){
                sb.append(line).append("\n");
            }
            is.close();
            result = sb.toString();
            Log.i(TAG, "parseEntity() -> Received API Response -> " + result);

        }
        catch (Exception e){
            Log.e(TAG, "parseEntity() -> Could not read API response! -> " + e.toString());

        }
        return result;
    }

    private JSONArray parseResultToJSON(String result){
        JSONArray jArray;
        try{

            jArray = new JSONArray(result);
            Log.i(TAG, "parseResultToJSON() -> " + "result was JSONArray, returning it. ");
            return jArray;

        }catch(JSONException e){

            Log.i(TAG, "parseResultToJSON() -> " + "Result wasn't an array, creating an array from single object. ");
            jArray = new JSONArray();

            try {

                JSONObject jObject = new JSONObject(result);
                jArray.put(jObject);
                return jArray;

            } catch (JSONException e1) {

                e1.printStackTrace();
                Log.e(TAG, "Error parsing the json! -> " + e1.toString());

            }

            return null;
        }
    }


    @Override
    protected void onPostExecute(JSONArray jArray){
            this.listener.onTaskCompleted(jArray, this.context);
    }
}
