package com.aga.hcp.home_control_prototype;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.aga.hcp.home_control_prototype.Spark.Device;
import com.aga.hcp.home_control_prototype.Spark.Spark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by garygraham on 14-11-22.
 * Base Widget that allows for the creation of multiple widgets. All have same vague behaviour:
 * 0. It registers itself for correct toggle notifications
 * 1. Click the widget
 * 2. It makes an async call
 * 3. On response, modify the ImageView to reflect the current state of the device.
 *
 * That's it.
 */
public abstract class DeviceWidget extends AppWidgetProvider implements OnTaskCompleted{
    protected static final String TAG = "WidgetProvider";
    protected Device device;

    public void onUpdate(Context context, AppWidgetManager awm, int[] appWidgetIds){
        //god this is a greasy hack but it works.
        final int N = appWidgetIds.length;
        device = Spark.getInstance().getDeviceByName("Tadgh");

        for(int i = 0; i < N ; i++){
            int appWidgetId = appWidgetIds[i];
            Intent intent = getWidgetIntent(context);
            intent.setAction(getToggleAction());
            PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), getWidgetLayout());
            views.setOnClickPendingIntent(getWidgetImage(), pendingIntent);

            awm.updateAppWidget(appWidgetId, views);
        }
        if(device != null) {
            performGetStatusTask(device.getId(), this, context);
        }
        registerForEvents(this, context);
    }

    public abstract void performGetStatusTask(String id, OnTaskCompleted listener, Context context);
    public abstract void performToggleTask(String id, OnTaskCompleted listener, Context context);
    public abstract void registerForEvents(OnTaskCompleted listener, Context context);
    public abstract Intent getWidgetIntent(Context context);
    protected abstract String getClassName();
    protected abstract Integer getWidgetLayout();
    protected abstract Integer getWidgetImage();
    protected abstract Integer getOnImage();
    protected abstract Integer getOffImage();
    protected abstract String getToggleAction();
    public abstract boolean isRegistered();


    /**
     * The callback that happens when the user taps the button. Executes a ToggleTask via the API.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);

        if(intent.getAction().equals(getToggleAction())){
            Log.i(TAG, "onReceive() -> received toggleAction from Intent in the widget.");


            if(!isRegistered()){
                Log.i(TAG, "onReceive() -> We aren't registered! Re-registering...");
                registerForEvents(this, context);
            }else{
                Log.i(TAG, "onReceive() -> We are already registered. Moving along...");
            }

            if(device == null){
                Log.i(TAG, "onReceive() -> Device was null, trying to pull it again...");
                device = Spark.getInstance().getDeviceByName("Tadgh");

            }
            if(device != null) {
                performToggleTask(device.getId(), this, context);
            }else{
                Log.e(TAG, "Couldn't locate device!!!! not sending API call.");
                Global.showToast(context, "Sparky must be running in the background for the widget to work!", Toast.LENGTH_SHORT);

            }
        }
    }

    /**
     * Handles all API requests in the widget. Essentially just changes the state of the widget based on the return code.
     */
    @Override
    public void onTaskCompleted(Object obj, Context context) {

        Log.i(TAG, "onTaskCompleted() -> received response in the widget class. ");
        if(obj == null) {
            showTimeOutToast(context);
        }else{

            JSONArray jArray = (JSONArray) obj;
            try {
                //TODO CATCH EMPTY JSON ARRAYS
                JSONObject jObject = jArray.getJSONObject(0);
                int status = jObject.getInt("return_value");
                switch (status) {
                    case 0:
                        Log.i(TAG, "Device is off!");
                        deviceOff(context);
                        break;
                    case 1:
                        Log.i(TAG, "Device is on!");
                        deviceOn(context);
                        break;
                    default:
                        Log.e(TAG, "Returned a non-standard response!!");
                        break;
                }
            } catch (JSONException e) {
                Log.e(TAG, "onTaskCompleted() -> Couldn't pull JSONObject from jArray : " + jArray.toString());
                e.printStackTrace();
            }
        }
    }
    private void showTimeOutToast(Context context) {
        Global.showToast(context, "Couldn't communicate with Spark core!!", Toast.LENGTH_SHORT);
    }

    /**
     * Sets the correct image on all widgets.
     */
    public void deviceOff(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), getWidgetLayout());
        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        for (int appWidgetId : awm.getAppWidgetIds(new ComponentName(context.getPackageName(), getClassName()))) {
            Log.i(TAG, "deviceOff() -> updating widget: " + appWidgetId);
            views.setImageViewResource(getWidgetImage(), getOffImage());
            awm.updateAppWidget(appWidgetId, views);
        }
    }



    /**
     * Sets the correct image on all widgets.
     */
    public void deviceOn(Context context){
        RemoteViews views = new RemoteViews(context.getPackageName(), getWidgetLayout());
        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        for (int appWidgetId : awm.getAppWidgetIds(new ComponentName(context.getPackageName(), getClassName()))) {
            Log.i(TAG, "deviceOn() -> updating widget: " + appWidgetId);
            views.setImageViewResource(getWidgetImage(), getOnImage());
            awm.updateAppWidget(appWidgetId, views);
        }
    }

}

