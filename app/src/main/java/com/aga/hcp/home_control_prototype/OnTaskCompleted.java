package com.aga.hcp.home_control_prototype;

/**
 * Created by garygraham on 2014-09-22.
 */

import android.content.Context;

/**
 * An interface implemented by any activity that wants to make an API call. This is
 * so that it can handle the API response inside the activity(assuming there is an API response).
 * Currently all API calls return essentially just garbage(nothing of value).
 */
public interface OnTaskCompleted {

    //some calls require a context, for example the widget call.
    void onTaskCompleted(Object obj, Context context);
}
