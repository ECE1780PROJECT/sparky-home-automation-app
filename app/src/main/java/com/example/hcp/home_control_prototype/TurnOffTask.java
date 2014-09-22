package com.example.hcp.home_control_prototype;

/**
 * Created by garygraham on 2014-09-22.
 * A task to send the shutdown signal to the Spark core.
 */
public class TurnOffTask extends AsyncAPITask {
    private static final String api_path = "v1/devicename/turn_off";

    public TurnOffTask(OnTaskCompleted listener) {
        super(listener, TurnOffTask.api_path);
    }
}
