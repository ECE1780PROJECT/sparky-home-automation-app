package com.aga.hcp.home_control_prototype;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aga.hcp.home_control_prototype.Spark.LightGetStatusTask;
import com.aga.hcp.home_control_prototype.Spark.LightToggleTask;

/**
 * Created by garygraham on 14-11-22.
 */
public class LightWidget extends DeviceWidget {
    public static String listenerId = "lightwidget";

    public void performGetStatusTask(String id, OnTaskCompleted listener, Context context) {
        Log.i(TAG, "performGetStatusTask() -> Executing fan status task...");
        new LightGetStatusTask(id, listener, context).execute();
    }

    @Override
    public void performToggleTask(String id, OnTaskCompleted listener, Context context) {
        Log.i(TAG, "performToggleTask() -> Executing fan toggle task...");
        new LightToggleTask(id, listener, context).execute();
    }

    @Override
    public void registerForEvents(OnTaskCompleted listener, Context context) {
        Log.i(TAG, "registerForEvents() -> Registering for light toggle updates. ...");
        LightToggleTask.registerForToggleEvent(listenerId, listener, context);
    }

    @Override
    public Intent getWidgetIntent(Context context) {
        Intent intent = new Intent(context, LightWidget.class);
        return intent;
    }

    @Override
    protected String getClassName() {
        return LightWidget.class.getName();
    }

    @Override
    protected Integer getWidgetLayout() {
        return R.layout.app_widget_layout_light;
    }

    @Override
    protected Integer getWidgetImage() {
        return R.id.bulbImg;
    }

    @Override
    protected Integer getOnImage() {
        return R.drawable.bulb_on_img;
    }

    @Override
    protected Integer getOffImage() {
        return R.drawable.bulb_off_img;
    }

    @Override
    protected String getToggleAction() {
        return "ToggleLight";
    }

    @Override
    public boolean isRegistered() {
        return LightToggleTask.isRegistered(listenerId);

    }
}
