package com.example.hcp.home_control_prototype.Spark;

import android.content.Context;

import com.example.hcp.home_control_prototype.AsyncAPITask;
import com.example.hcp.home_control_prototype.OnTaskCompleted;
import com.example.hcp.home_control_prototype.WidgetProvider;

/**
 * Created by Master on 10/7/2014.
 */
public class LightGetStatusTask extends SparkAPITask {
    private static final String api_path = "state";

    public LightGetStatusTask(String deviceID, OnTaskCompleted listener) {
        super(deviceID,  api_path, listener);
    }

    public LightGetStatusTask(String deviceID, OnTaskCompleted listener, Context context) {
        super(deviceID,api_path,  listener, context);
    }


}
