package com.example.hcp.home_control_prototype;

/**
 * Created by Master on 9/24/2014.
 */
public class ToggleTask extends AsyncAPITask {
    private static final String api_path = "toggle";

    public ToggleTask(OnTaskCompleted listener) {
        super(listener, api_path);
    }
}
