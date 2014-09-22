package com.example.hcp.home_control_prototype;

/**
 * Created by garygraham on 2014-09-22.
 */

/**
 * An interface implemented by any activity that wants to make an API call. This is
 * so that it can handle the API response inside the activity(assuming there is an API response).
 */
public interface OnTaskCompleted {

    void onTaskCompleted(Object obj);
}
