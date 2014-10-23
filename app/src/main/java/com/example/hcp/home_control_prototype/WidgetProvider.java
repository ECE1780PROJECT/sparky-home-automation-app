package com.example.hcp.home_control_prototype;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.hcp.home_control_prototype.Spark.Device;
import com.example.hcp.home_control_prototype.Spark.LightGetStatusTask;
import com.example.hcp.home_control_prototype.Spark.LightToggleTask;
import com.example.hcp.home_control_prototype.Spark.Spark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Master on 10/7/2014.
 *
 * The entire class for handling the widget. When a user taps the widget, it initiates a ToggleTask,
 * and the local OnTaskCompleted handler changes the image of the widget based on the current status of the light.
 */
public class WidgetProvider extends AppWidgetProvider implements OnTaskCompleted {
    public final static String toggleAction = "toggle";
    private static final String TAG = "WidgetProvider";
    private  static Context context = null;
    private  static AppWidgetManager awm = null;
    Device device;
    public void onUpdate(Context context, AppWidgetManager awm, int[] appWidgetIds){
        //god this is a greasy hack but it works.
        this.context = context;
        this.awm = awm;
        final int N = appWidgetIds.length;
        device = Spark.getInstance().getDeviceByName("Tadgh");

        for(int i = 0; i < N ; i++){
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, WidgetProvider.class);
            intent.setAction(toggleAction);
            PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_layout);
            views.setOnClickPendingIntent(R.id.bulbImg, pendingIntent);

            awm.updateAppWidget(appWidgetId, views);
        }
        if(device != null) {
            LightGetStatusTask status = new LightGetStatusTask(device.getId(), this, context);
            status.execute();
        }
        LightToggleTask.registerForToggleEvent(this, this.context);
    }

    /**
     * The callback that happens when the user taps the button. Executes a ToggleTask via the API.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);
        if(intent.getAction().equals(toggleAction)){
            Log.i(TAG, "onReceive() -> received toggleAction from Intent in the widget.");
            device = Spark.getInstance().getDeviceByName("Tadgh");
            if(device != null) {
                LightToggleTask toggleTask = new LightToggleTask(device.getId(), this, context);
                Log.i(TAG, "onReceive() -> sent off toggle task to AsyncAPICall.");
                toggleTask.execute();
            }else{
                Log.e(TAG, "Couldn't locate device!!!! not sending API call.");
            }

        }
    }

    /**
     * Handles all API requests in the widget. Essentially just changes the state of the widget based on the return code.
     */
    @Override
    public void onTaskCompleted(Object obj, Context context) {
        Log.i(TAG, "onTaskCompleted() -> received response in the widget class. ");
        if(obj != null) {
            JSONArray jArray = (JSONArray) obj;
            try {
                //TODO CATCH EMPTY JSON ARRAYS
                JSONObject jObject = jArray.getJSONObject(0);
                int status = jObject.getInt("return_value");
                switch (status) {
                    case 0:
                        Log.i(TAG, "Light is off!");
                        lightOff(context);
                        break;
                    case 1:
                        Log.i(TAG, "Light is on!");
                        lightOn(context);
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

    /**
     * Sets the correct image on all widgets.
     */
    public void lightOff(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_layout);

        for (int appWidgetId : awm.getAppWidgetIds(new ComponentName(context.getPackageName(),WidgetProvider.class.getName()))) {
            views.setImageViewResource(R.id.bulbImg, R.drawable.bulb_off_img);
            awm.updateAppWidget(appWidgetId, views);
        }
    }


    /**
     * Sets the correct image on all widgets.
     */
    public void lightOn(Context context){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_layout);

        for (int appWidgetId : awm.getAppWidgetIds(new ComponentName(context.getPackageName(),WidgetProvider.class.getName()))) {
            views.setImageViewResource(R.id.bulbImg, R.drawable.bulb_on_img);
            awm.updateAppWidget(appWidgetId, views);
        }
    }

}
