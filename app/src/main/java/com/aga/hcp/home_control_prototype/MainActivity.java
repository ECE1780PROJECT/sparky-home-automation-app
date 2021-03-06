package com.aga.hcp.home_control_prototype;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.aga.hcp.home_control_prototype.Spark.GetDevicesTask;
import com.aga.hcp.home_control_prototype.Spark.Spark;
import com.aga.hcp.home_control_prototype.Spark.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



public class MainActivity extends Activity implements OnTaskCompleted {
    private static final int pageCount = 2;
    private static final String TAG = "MainActivity";
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //grab tokens from prefs.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        initializeDefaultGesturePreference(prefs);

        Set<String> tokenStrings = prefs.getStringSet("tokens", new HashSet<String>());

        Log.i(TAG, Spark.getInstance().getTokenValues().toString());
        //if we have no tokens, we need to log back in. TODO eventually replace with a custom token generator on the API.
        if (tokenStrings.size() < 1) {
            Log.i(TAG, "onCreate() -> Found no tokens in the sharedpreferences. Firing application back off to the login.");
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }else {
            //Otherwise, we can just load tokens from the shared prefs.
            ArrayList<Token> tokens = new ArrayList<Token>();
            for(String tokenString : tokenStrings){
                //my god this is getting greasy. TODO put all semi-permanent token info into a DB and load at boot.
                tokens.add(new Token(tokenString, "who cares"));
            }
            //fill spark up with new tokens.
            Spark.getInstance().replaceAllTokens(tokens);

            //grab all the devices.
            GetDevicesTask gdt = new GetDevicesTask(this);
            showProgressSpinner();
            gdt.execute(Spark.getInstance().getCurrentToken().getValue());
        }



    }

    private void initializeDefaultGesturePreference(SharedPreferences gSharedPreferences){
        SharedPreferences.Editor editor = gSharedPreferences.edit();

        for (int i=0; i < Global.DeviceList.length; i++) {
            String globalGesturePrefSelect = Global.DeviceList[i] + "_" + Global.PREFERENCE_GESTURE_SELECT;
            String globalGesturePrefSelectName = Global.DeviceList[i] + "_" + Global.PREFERENCE_GESTURE_SELECT_NAME;
            if (gSharedPreferences.getString(globalGesturePrefSelectName, null) == null ) {
                editor.putInt(globalGesturePrefSelect, i);
                editor.putString(globalGesturePrefSelectName, Global.DEFAULT_GESTURE_LIST[i]);
            }

        }
        editor.commit();
        Log.i(TAG, "initializeDefaultGesturePreference() -> Initialization completed");
    }


    @Override
    protected void onPause() {
        super.onPause();
        dialog.dismiss();
        Boolean bgServiceEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("background_services_pref", false);
        if(!bgServiceEnabled){
            Log.i(TAG, "onPause() -> Stopping background service due to preference setting...");
            stopService(new Intent(this, BGRunnerService.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean bgServiceEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("background_services_pref", false);
        if (!serviceIsRunning(BGRunnerService.class)){
            Log.i(TAG, "onPause() -> Starting background service based on preference setting...");
            startService(new Intent(this, BGRunnerService.class));
        }
    }

    private boolean serviceIsRunning(Class<?> serviceClass){
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service: am.getRunningServices(Integer.MAX_VALUE)) {
            if(serviceClass.getName().equals(service.service.getClassName())){
                Log.i(TAG, "serviceIsRunning() -> Found service to be running :" + serviceClass.getName());
                return true;
            }
        }
        Log.i(TAG, "serviceIsRunning() -> Could not find service with name: " + serviceClass.getName());
        return false;
    }


    @Override
    public void onTaskCompleted(Object obj, Context context) {
        Log.i(TAG, "onTaskCompleted () -> Received response: " + obj.toString());
        hideProgressSpinner();
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().add(R.id.container, new ToggleFragment())
                .commit();

        if(!serviceIsRunning(BGRunnerService.class)){
            startService(new Intent(this,BGRunnerService.class));
        }

    }

    private void showProgressSpinner(){
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Discovering devices...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }
    private void hideProgressSpinner(){
        dialog.dismiss();
    }

    /*
    private class SlideViewAdapter extends FragmentStatePagerAdapter {
        public SlideViewAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            switch(i){
                case 0:
                    return new ToggleLightFragment();
                case 1:
                    return new SettingsFragment();
                default:
                    return new ToggleLightFragment();
            }
        }


        @Override
        public int getCount() {
            return pageCount;
        }
    }
    */


}
