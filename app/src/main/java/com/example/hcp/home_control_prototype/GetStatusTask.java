package com.example.hcp.home_control_prototype;

/**
 * Created by Master on 10/7/2014.
 */
public class GetStatusTask extends AsyncAPITask {
    private static final String api_path = "state";

    public GetStatusTask(OnTaskCompleted listener) {
        super(listener, api_path);
    }
}
