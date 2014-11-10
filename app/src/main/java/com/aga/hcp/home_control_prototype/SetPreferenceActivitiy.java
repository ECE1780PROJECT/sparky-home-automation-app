package com.aga.hcp.home_control_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class SetPreferenceActivitiy extends Activity {

    private static final String TAG = "SetSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent sendingIntent = getIntent();
        String settingsPage = sendingIntent.getStringExtra("device");
        Log.i(TAG, "onCreate() -> Deciding which preference fragment to open...");


        if (settingsPage.equals(Global.FAN_NAME)) {
            Log.i(TAG, "onCreate() -> decided on Fan Settings.");
            getFragmentManager().beginTransaction().replace(android.R.id.content, new FanSettingsFragment()).commit();
        } else if (settingsPage.equals(Global.LIGHT_NAME)) {
            Log.i(TAG, "onCreate() -> decided on Light Settings.");
            getFragmentManager().beginTransaction().replace(android.R.id.content, new LightSettingsFragment()).commit();
        }


    }
}