package com.example.hcp.home_control_prototype;
import android.app.Service;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.util.Log;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import java.util.Date;


public class BGRunnerService extends Service implements SensorEventListener,OnTaskCompleted{
    ToggleTask toggle;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate=0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD=50;
    private Date date;

    public BGRunnerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate()
    {
        senSensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer=senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i("Status","SENSOR STARTED");
    }
    @Override
    public void onDestroy()
    {
        //super.onDestroy();
        senSensorManager.unregisterListener(this, senAccelerometer);
        Log.i("Status","SENSOR STOPPED");
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
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
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
                            toggle=new ToggleTask(this);
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
                           /* Log.i("speed1", Float.toString(speed1));
                            Log.i("Gesture type", "Type 2");*/
                            toggle=new ToggleTask(this);
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
                        Thread.sleep(1200);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    flag=0;
                    set1=0;
                    set2=0;
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
    @Override
    public void onTaskCompleted(Object obj,Context context)
    {
        //
    }


}

