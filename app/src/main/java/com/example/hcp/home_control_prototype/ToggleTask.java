package com.example.hcp.home_control_prototype;

import android.content.Context;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Master on 9/24/2014.
 */
public class ToggleTask extends AsyncAPITask {
    private static final String api_path = "toggle";
    private static HashMap<OnTaskCompleted, Context> statusListeners = new HashMap<OnTaskCompleted, Context>();


    public ToggleTask(OnTaskCompleted listener) {
        super(listener, api_path);
        statusListeners.put(listener, null);

    }

    public ToggleTask(OnTaskCompleted listener, Context context) {
        super(listener, api_path, context);
        statusListeners.put(listener, context);
    }

    @Override
    public void onPostExecute(JSONArray jsonArray){
        Set entries = statusListeners.entrySet();
        Iterator i = entries.iterator();
        while(i.hasNext()){
            Map.Entry entry = (Map.Entry)i.next();
            ((OnTaskCompleted)entry.getKey()).onTaskCompleted(jsonArray, (Context)entry.getValue());
        }
    }

    public static void registerForToggleEvent(OnTaskCompleted listener, Context context){
        statusListeners.put(listener, context);
    }
}
