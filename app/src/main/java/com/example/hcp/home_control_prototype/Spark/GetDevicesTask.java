package com.example.hcp.home_control_prototype.Spark;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Created by garygraham on 2014-10-14.
 */
public class GetDevicesTask extends AsyncTask<String, Void, ArrayList<Device>> {
    private static final String URL = "https://api.spark.io/v1/devices";
    private static final String TAG = "GetDevicesTask";
    private Spark listener;

    public GetDevicesTask(Spark listener){
        this.listener = listener;
    }

    @Override
    protected ArrayList<Device> doInBackground(String... token) {
        String usable_token = token[0];
        ArrayList<Device> resultantDevices = new ArrayList<Device>();

        try {
            //Build the URI
            Uri.Builder builder = new Uri.Builder();
            String uri = builder.scheme("https")
                            .authority("api.spark.io")
                            .path("v1/devices")
                            .appendQueryParameter("access_token", usable_token)
                            .build().toString();

            //Building the HTTP request.
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(uri);
            Log.i(TAG, "doInBackground() ->Sending out request: " + uri + " with token: " + usable_token);
            //Getting the HTTP response

            HttpResponse response = httpClient.execute(httpGet);
            Log.i(TAG, response.toString());
            HttpEntity entity = response.getEntity();

            //static parsing class for Spark API.
            JSONArray jsonResult = ResponseParser.parseEntityToJSON(entity);

            //looping through all devices
            for(int i = 0; i < jsonResult.length(); i++){
                JSONObject jObject = jsonResult.getJSONObject(i);
                String device_id = jObject.getString("id");
                String device_name = jObject.getString("name");
                String last_heard = jObject.getString("last_heard");
                boolean connected = jObject.getBoolean("connected");
                resultantDevices.add(new Device(device_id, device_name, connected, last_heard));
                Log.i(TAG, "login() -> " + "Found new device! -> " + device_name);

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        for(Device device: resultantDevices) {
            if (!this.listener.getDevices().contains(device)) {
                this.listener.addDevice(device);
            }
        }
        return resultantDevices;


    }

    @Override
    public void onPostExecute(ArrayList<Device> result) {
    }
}

