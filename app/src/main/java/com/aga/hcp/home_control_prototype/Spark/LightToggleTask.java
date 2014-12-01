package com.aga.hcp.home_control_prototype.Spark;

import android.content.Context;
import android.util.Log;

import com.aga.hcp.home_control_prototype.OnTaskCompleted;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Master on 10/22/2014.
 */
public class LightToggleTask extends SparkAPITask {
    private static String api_path = "toggle";
    private static HashMap<String, ListenerAndContext> statusListeners = new HashMap<String, ListenerAndContext>();
    
    
    public LightToggleTask(String deviceID, OnTaskCompleted listener) {
        super(deviceID, api_path, listener);
    }
    public LightToggleTask(String deviceID,OnTaskCompleted listener, Context context) {
        super(deviceID, api_path, listener, context);
    }

    @Override
    public void onPostExecute(JSONArray jsonArray){
        Set entries = statusListeners.entrySet();
        Iterator i = entries.iterator();
        OnTaskCompleted listener;
        Context context;

        while(i.hasNext()){

            Map.Entry entry = (Map.Entry)i.next();
            Log.i(TAG, "onPostExecute() -> Notifying observer that task has completed: " + entry.toString());
            listener = ((ListenerAndContext)entry.getValue()).getListener();
            context = ((ListenerAndContext)entry.getValue()).getContext();
            listener.onTaskCompleted(jsonArray, context);
        }
    }

    public static void registerForToggleEvent(String id, OnTaskCompleted listener, Context context){
        statusListeners.put(id, new ListenerAndContext(listener, context));
    }

    public static boolean isRegistered(String id){
        return statusListeners.containsKey(id);
    }
}
