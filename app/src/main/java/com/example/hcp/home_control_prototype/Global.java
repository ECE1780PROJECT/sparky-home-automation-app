package com.example.hcp.home_control_prototype;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ansonliang55 on 2014-10-14.
 * This is a global glass that has function that is used globally (singleton software design pattern)
 */
public class Global {
    public static String trainingSet = "gesturelist";
    public static int selectedIndex = 0;
    public static final String PREFERENCE_GLOBAL = "hcp_home_control_prototype";
    public static final String PREFERENCE_GESTURE_SELECT = "gesture_select";
    public static void showToast(Context applicationContext, CharSequence text, int duration){
        // Usage Global.showToast(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_LONG);
        Toast toast = Toast.makeText(applicationContext, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}
