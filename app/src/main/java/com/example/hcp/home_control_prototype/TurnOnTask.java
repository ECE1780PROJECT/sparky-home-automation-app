package com.example.hcp.home_control_prototype;

/**
 * Created by garygraham on 2014-09-22.
 * A task to send the Turn On signal to the spark core.
 */
public class TurnOnTask extends AsyncAPITask {
    private static final String api_path = "v1/devicename/turn_on";

    public TurnOnTask(OnTaskCompleted listener) {
        super(listener, TurnOnTask.api_path);
    }
}
