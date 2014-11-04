package com.example.hcp.home_control_prototype;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;
import com.example.hcp.home_control_prototype.Spark.Device;
import com.example.hcp.home_control_prototype.Spark.LightToggleTask;
import com.example.hcp.home_control_prototype.Spark.Spark;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.RemoteException;
import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionListener;
import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionService;
import com.example.hcp.home_control_prototype.gesture.classifier.Distribution;
import java.util.List;


public class BGRunnerService extends Service implements Runnable,ServiceConnection,OnTaskCompleted, SharedPreferences.OnSharedPreferenceChangeListener{
    LightToggleTask toggle;
    private Device device;
    private static final String TAG = "GestureService";
    private boolean enabled;
    private static float gestError = 12;
    private int idfier;
    IGestureRecognitionService recognitionService;

    SharedPreferences prefs;
    IBinder gestureListenerStub  = new IGestureRecognitionListener.Stub(){

        @Override
        public void onGestureRecognized(final Distribution distribution) throws RemoteException {
            if (enabled) {
                idfier = prefs.getInt(Global.PREFERENCE_GESTURE_SELECT, 0);
                String s = prefs.getString(Global.PREFERENCE_GESTURE_SELECT_NAME, null);
                Log.i(TAG, "onGestureRecognized() -> " + s);
                switch (idfier) {
                    case 0:
                        toggleDevice(0, distribution);
                        break;
                    case 1:
                        toggleDevice(1, distribution);
                        break;
                    case 2:
                        toggleDevice(2, distribution);
                        break;
                    case 3:
                        toggleDevice(3, distribution);
                        break;
                    default:
                        Log.i(TAG, "wut");
                        break;
                }

            }
        }
    private void toggleDevice(int i, Distribution distribution)
    {
        Log.i(TAG, "toggleDevice() -> toggling device");
        String gestName = prefs.getString(Global.PREFERENCE_GESTURE_SELECT_NAME, "Couldn't find the preference!");
        String bestMatch = distribution.getBestMatch();
        double bestDist = distribution.getBestDistance();
        Log.i(TAG, "toggleDevice():\ngesture name: " + gestName + "\nbest Match: " + bestMatch + "\nbest distnace: " + Double.toString(bestDist) + "\nallowable error: " + gestError);
        if(gestName.equals(bestMatch) && bestDist < gestError) {
            Log.i(TAG, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
            device = Spark.getInstance().getDeviceByName("Tadgh");
            if(device != null){
                toggle = new LightToggleTask(device.getId(), BGRunnerService.this);
                toggle.execute();
            }else{
                Log.e(TAG, "toggleDevice() -> Couldnt make call! device not known!");
            }

            Log.i(TAG, "BUMP RIGHT");
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

        this.enabled = !prefs.getBoolean("disable_pref", false);
        Log.i(TAG, "BGRunnerService() -> grabbing disabled preference. Status of the service should be: " + enabled);
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
        Log.i(TAG, "onSharedPreferenceChanged() -> Received Changed Preference, key is: " + key);
        boolean isEnabled = !sharedPreferences.getBoolean("disable_pref", false);
        this.setEnabled(isEnabled);
    }


    public void setEnabled(boolean isEnabled) {
        if (this.enabled != isEnabled){
            this.enabled = isEnabled;
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

