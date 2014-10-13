package com.example.hcp.home_control_prototype;

import android.content.Context;

/**
 * Created by Master on 9/24/2014.
 */
public class ToggleTask extends AsyncAPITask {
    private static final String api_path = "toggle";

    public ToggleTask(OnTaskCompleted listener) {
        super(listener, api_path);
    }

    public ToggleTask(OnTaskCompleted listener, Context context) {
        super(listener, api_path, context);
    }
}
