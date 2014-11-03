package com.example.hcp.home_control_prototype;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.example.hcp.home_control_prototype.Spark.Device;
import com.example.hcp.home_control_prototype.Spark.LightToggleTask;
import com.example.hcp.home_control_prototype.Spark.Spark;

import java.util.Date;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.RemoteException;

import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionListener;
import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionService;
import com.example.hcp.home_control_prototype.gesture.classifier.Distribution;

import java.util.List;


public class BGRunnerService extends Service implements Runnable,ServiceConnection,SensorEventListener,OnTaskCompleted, SharedPreferences.OnSharedPreferenceChangeListener{
    LightToggleTask toggle;
    private SensorManager senSensorManager;
    private Device device;
    private Sensor senAccelerometer;
    private long lastUpdate=0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD=50;
    private static final String TAG = "GestureService";
    private boolean enabled;
    private static float gestError=12;
    private int idfier;

    private Date date;
    SharedPreferences prefs;
    IBinder gestureListenerStub;

    public BGRunnerService() {
    }
    IGestureRecognitionService recognitionService;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate()
    {
        senSensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer=senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        device = Spark.getInstance().getDeviceByName("Tadgh");

        //DEFAULTING THE GESTURES TO OFF IF WE CANT READ THE VALUE.
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        this.enabled = !prefs.getBoolean("disable_pref", false);
        Log.i(TAG, "BGRunnerService() -> grabbing disabled preference. Status of the service should be: " + enabled);
        this.handleSensors();

        Intent bindIntent = new Intent("com.example.hcp.home_control_prototype.gesture.GESTURE_RECOGNIZER");
        bindService(bindIntent, this, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onDestroy()
    {
        //super.onDestroy();
        senSensorManager.unregisterListener(this, senAccelerometer);
        Log.i(TAG,"SENSOR STOPPED");

    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId)
    {
        return(1);
    }
    private int flag=0, set1=0,set2=0;
    private long t1=0,t2=0;
    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;
       /* if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                //for the y-z direction plane. For xyz, x+y+z-last_x-last_y-last_z
                x = Math.abs(x);
                y = Math.abs(y);
                z = Math.abs(z);
                float speed = Math.abs(y + (8 * z) - last_y - (last_z * 5));
                float speed1 = Math.abs((8 * x) + y - (5 * last_x) - last_y);
                if ((speed > SHAKE_THRESHOLD || speed1 > SHAKE_THRESHOLD) && flag != 1) {
                    if (speed > speed1 && z > x) {
                        if(set1==1)
                        {
                            set1=0;
                            if(t1-System.currentTimeMillis()<1200)
                                set1=2;
                        }
                        else if(set1==0)
                        {
                            set1=1;
                            t1=System.currentTimeMillis();
                        }
                        else
                        {

                        }
                        if(set1==2)
                        {
                            //Insert action here for Gesture 1
                            Log.i(TAG,"Type 1 gesture found.");
                            device = Spark.getInstance().getDeviceByName("Tadgh");
                            toggle = new LightToggleTask(device.getId(), this);
                            toggle.execute();
                            flag = 1;
                        }
                    } else if (speed1 > speed && x > z) {
                        if(set2==1)
                        {
                            set2=0;
                            if(t2-System.currentTimeMillis()<1200)
                                set2=2;
                        }
                        else if(set2==0)
                        {
                            set2=1;
                            t2=System.currentTimeMillis();
                        }
                        else
                        {

                        }
                        if(set2==2)
                        {
                            //Insert action here for Gesture 2
                           //Log.i("speed1", Float.toString(speed1));
                            Log.i(TAG, "Type 2 gesture found.");
                            device = Spark.getInstance().getDeviceByName("Tadgh");
                            toggle=new LightToggleTask(device.getId(), this);
                            toggle.execute();
                            flag = 1;
                        }
                    } else {

                    }
                }
                else if(flag==1&&(set1==2||set2==2))
                {
                    try
                    {
                        Log.i(TAG, "Gesture found, task executed, going to sleep...");
                        Thread.sleep(2000);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "Waking up!");
                    flag=0;
                    set1=0;
                    set2=0;
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }*/
           // else
            //{

        gestureListenerStub  = new IGestureRecognitionListener.Stub(){

                    @Override
                    public void onGestureRecognized(final Distribution distribution) throws RemoteException {
                        idfier=prefs.getInt(Global.PREFERENCE_GESTURE_SELECT,0);
                        switch (idfier)
                        {
                            case 0:
                                if(distribution.getBestMatch().equals("gest1")&&distribution.getBestDistance()<gestError) {
                                    Log.i(TAG, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
                                    device = Spark.getInstance().getDeviceByName("Tadgh");
                                    toggle = new LightToggleTask(device.getId(),BGRunnerService.this);
                                    toggle.execute();
                                    Log.i("IDFIER", "BUMP RIGHT");
                                }
                                break;
                            case 1:
                                if(distribution.getBestMatch().equals("gest2")&&distribution.getBestDistance()<gestError) {
                                   Log.i(TAG, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
                                    device = Spark.getInstance().getDeviceByName("Tadgh");
                                    toggle = new LightToggleTask(device.getId(),BGRunnerService.this);
                                    toggle.execute();
                                    Log.i("IDFIER", "BUMP LEFT");
                                }
                                break;
                            case 2:
                                if(distribution.getBestMatch().equals("gest3")&&distribution.getBestDistance()<gestError) {
                                    Log.i(TAG, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
                                    device = Spark.getInstance().getDeviceByName("Tadgh");
                                    toggle = new LightToggleTask(device.getId(),BGRunnerService.this);
                                    toggle.execute();
                                    Log.i("IDFIER", "BUMP UP");
                                }
                                break;
                            default:
                                break;
                        }

                  }

                    @Override
                    public void onGestureLearned(String gestureName) throws RemoteException {

                    }

                    @Override
                    public void onTrainingSetDeleted(String trainingSet) throws RemoteException {

                    }
                };;
            //}
        //}
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

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
            this.handleSensors();
        }

    }

    private void handleSensors() {
        if(this.enabled){
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.i(TAG, "handleSensors() -> Registered Listener.");
            Log.i(TAG,"handleSensors() -> SENSOR STARTED");
        }else {
            senSensorManager.unregisterListener(this);
            Log.i(TAG, "handleSensors() -> Unregistered Listener.");
            Log.i(TAG,"handleSensors() -> SENSOR STOPPED");
        }
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        recognitionService = IGestureRecognitionService.Stub.asInterface(service);
        try {
            recognitionService.startClassificationMode(Global.trainingSet);
            recognitionService.registerListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
            List<String> items = recognitionService.getGestureList(Global.trainingSet);
            //Log.i(TAG, items.toString());
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

