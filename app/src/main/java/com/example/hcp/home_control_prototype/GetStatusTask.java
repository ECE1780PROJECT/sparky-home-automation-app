package com.example.hcp.home_control_prototype;

import android.content.Context;

/**
 * Created by Master on 10/7/2014.
 */
public class GetStatusTask extends AsyncAPITask {
    private static final String api_path = "state";

    public GetStatusTask(OnTaskCompleted listener) {
        super(listener, api_path);
    }

    public GetStatusTask(OnTaskCompleted listener, Context context) {
        super(listener, api_path, context);
    }
}
