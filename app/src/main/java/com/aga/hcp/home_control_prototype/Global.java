package com.aga.hcp.home_control_prototype;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by ansonliang55 on 2014-10-14.
 * This is a global glass that has function that is used globally (singleton software design pattern)
 */
public class Global {
    public static String trainingSet = "gesturelist";
    public static String[] DEFAULT_GESTURE_LIST = {"Bump Front","Bump Left"};
    public static final String PREFERENCE_GESTURE_SELECT = "gesture_select";
    public static final String PREFERENCE_GESTURE_SELECT_NAME = "gesture_select_name";
    public static final String FAN_NAME = "fan";
    public static final String LIGHT_NAME = "light";
    public static String[] DeviceList = {LIGHT_NAME, FAN_NAME};
    public static void showToast(Context applicationContext, CharSequence text, int duration){
        Toast toast = Toast.makeText(applicationContext, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}
