package com.example.hcp.home_control_prototype.Spark;

import android.content.Context;

import com.example.hcp.home_control_prototype.OnTaskCompleted;

/**
 * Created by Master on 10/7/2014.
 */
public class FanGetStatusTask extends SparkAPITask {
    private static final String api_path = "state2";

    public FanGetStatusTask(String deviceID, OnTaskCompleted listener) {
        super(deviceID,  api_path, listener);
    }

    public FanGetStatusTask(String deviceID, OnTaskCompleted listener, Context context) {
        super(deviceID,api_path,  listener, context);
    }


}
