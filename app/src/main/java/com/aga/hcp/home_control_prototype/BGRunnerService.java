package com.aga.hcp.home_control_prototype;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;
import com.aga.hcp.home_control_prototype.Spark.Device;
import com.aga.hcp.home_control_prototype.Spark.FanToggleTask;
import com.aga.hcp.home_control_prototype.Spark.LightToggleTask;
import com.aga.hcp.home_control_prototype.Spark.Spark;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.RemoteException;

import com.aga.hcp.home_control_prototype.Spark.SparkAPITask;
import com.aga.hcp.home_control_prototype.gesture.IGestureRecognitionListener;
import com.aga.hcp.home_control_prototype.gesture.IGestureRecognitionService;
import com.aga.hcp.home_control_prototype.gesture.classifier.Distribution;
import java.util.List;


public class BGRunnerService extends Service implements Runnable,ServiceConnection,OnTaskCompleted, SharedPreferences.OnSharedPreferenceChangeListener{
    SparkAPITask toggle;
    private Device device;
    private static final String TAG = "BGRunnerService";
    private boolean fan_enabled;
    private boolean light_enabled;
    private static float gestError = 12;
    //private int idfier;
    IGestureRecognitionService recognitionService;

    String globalLightPrefName = Global.LIGHT_NAME + "_" +Global.PREFERENCE_GESTURE_SELECT_NAME;
    String globalFanPrefName = Global.FAN_NAME + "_" +Global.PREFERENCE_GESTURE_SELECT_NAME;
    SharedPreferences prefs;
    IBinder gestureListenerStub  = new IGestureRecognitionListener.Stub(){

        @Override
        public void onGestureRecognized(final Distribution distribution) throws RemoteException {

            Log.i(TAG, "onGestureRecognized() Fan enabled: " + fan_enabled + " Light  enabled: " + light_enabled );
            if (light_enabled) {
                String s1 = prefs.getString(globalLightPrefName, null);
                Log.i(TAG, "onGestureRecognized() Light -> " + s1);

                toggleDevice(distribution, Global.LIGHT_NAME);


            }
            if (fan_enabled) {
                String s2 = prefs.getString(globalFanPrefName, null);
                Log.i(TAG, "onGestureRecognized() Fan: -> " + s2);

                toggleDevice(distribution, Global.FAN_NAME);


            }

        }
    private void toggleDevice(Distribution distribution, String deviceStr)
    {
        Log.i(TAG, "toggleDevice() -> toggling device");

        String gestName = prefs.getString(deviceStr +"_"+Global.PREFERENCE_GESTURE_SELECT_NAME, "Couldn't find the preference!");
        String bestMatch = distribution.getBestMatch();
        double bestDist = distribution.getBestDistance();
        Log.i(TAG, "toggleDevice():\ngesture name: " + gestName + "\nbest Match: " + bestMatch + "\nbest distnace: " + Double.toString(bestDist) + "\nallowable error: " + gestError);


        if(gestName.equals(bestMatch) && bestDist < gestError) {
            Log.i(TAG, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
            device = Spark.getInstance().getDeviceByName("Tadgh");
            if(device != null){
                if(deviceStr.equals(Global.LIGHT_NAME)) {
                    toggle = new LightToggleTask(device.getId(), BGRunnerService.this);
                    toggle.execute();
                }else if(deviceStr.equals(Global.FAN_NAME)) {
                    toggle = new FanToggleTask(device.getId(), BGRunnerService.this);
                    toggle.execute();
                }
            }else{
                Log.e(TAG, "toggleDevice() -> Couldnt make call! device not known!");
            }

        }
    }

    @Override
    public void onGestureLearned(String gestureName) throws RemoteException {

    }

    @Override
    public void onTrainingSetDeleted(String trainingSet) throws RemoteException {

    }
};

    public BGRunnerService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        device = Spark.getInstance().getDeviceByName("Tadgh");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        this.fan_enabled = !prefs.getBoolean("fan_disable_pref", false);
        Log.i(TAG, "BGRunnerService() -> grabbing disabled preference. Status of the service should be: " + fan_enabled);
        this.light_enabled = !prefs.getBoolean("light_disable_pref", false);
        Log.i(TAG, "BGRunnerService() -> grabbing disabled preference. Status of the service should be: " + light_enabled);
        //this.handleSensors();
        Log.i(TAG, "onCreate() -> About to create bindIntent");
        Intent bindIntent = new Intent("com.example.hcp.home_control_prototype.gesture.GESTURE_RECOGNIZER");
        bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "onCreate() -> Just created bindIntent");


    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unbindService(this);
        Log.i(TAG,"SENSOR STOPPED");

    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId)
    {
        return(1);
    }

    @Override
    public void onTaskCompleted(Object obj,Context context)
    {
        if(obj == null){
                showTimeOutToast();
            }
     }

    private void showTimeOutToast() {
        Global.showToast(this.getApplicationContext(), "Couldn't communicate with Spark core!!", Toast.LENGTH_SHORT);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        boolean isLightEnabled = !sharedPreferences.getBoolean("light_disable_pref", false);
        this.setLightEnabled(isLightEnabled);
        boolean isFanEnabled = !sharedPreferences.getBoolean("fan_disable_pref", false);
        this.setFanEnabled(isFanEnabled);
        Log.i(TAG, "onSharedPreferenceChanged() -> Received Changed Preference, key is: " + key);
        Log.i(TAG, "onSharedPreferenceChanged() -> Light is enable: " + this.light_enabled + " Fan is enable: " +this.fan_enabled );
    }


    public void setFanEnabled(boolean isEnabled) {
        if (this.fan_enabled != isEnabled){
            this.fan_enabled = isEnabled;
        }
    }

    public void setLightEnabled(boolean isEnabled) {
        if (this.light_enabled != isEnabled){
            this.light_enabled = isEnabled;
        }
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "onServiceConnected() -> " + "Entering onServiceConnected()");
        recognitionService = IGestureRecognitionService.Stub.asInterface(service);
        try {
            recognitionService.startClassificationMode(Global.trainingSet);
            recognitionService.registerListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
            List<String> items = recognitionService.getGestureList(Global.trainingSet);
            Log.i(TAG, "onServiceConnected() -> found all gestures: " + items.toString());
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
        //try {
        //   recognitionService.unregisterListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
        //} catch (RemoteException e) {
        //    // TODO Auto-generated catch block
        //    e.printStackTrace();
        //}

        // recognitionService = null;
        // unbindService(serviceConnection);
    }
    @Override
    public void run() {


    }
}

