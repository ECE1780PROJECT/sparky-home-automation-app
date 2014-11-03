package com.example.hcp.home_control_prototype;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionListener;
import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionService;
import com.example.hcp.home_control_prototype.gesture.classifier.Distribution;

import java.util.List;

/**
 * Created by ansonliang55 on 14-10-28.
 */
public class BGGestureService extends Service implements Runnable, ServiceConnection{

    private static final String TAG = "BGGestureService";
    Handler handler;

    public BGGestureService(){

    }


    IGestureRecognitionService recognitionService;
    IBinder gestureListenerStub  = new IGestureRecognitionListener.Stub(){

        @Override
        public void onGestureRecognized(final Distribution distribution) throws RemoteException {

              Log.i(TAG, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
        }

        @Override
        public void onGestureLearned(String gestureName) throws RemoteException {

        }

        @Override
        public void onTrainingSetDeleted(String trainingSet) throws RemoteException {

        }
    };;


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate(){
        Intent bindIntent = new Intent("com.example.hcp.home_control_prototype.gesture.GESTURE_RECOGNIZER");
        bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
    }



    @Override
    public void run() {


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
}