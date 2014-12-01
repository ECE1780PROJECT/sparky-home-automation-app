package com.aga.hcp.home_control_prototype;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aga.hcp.home_control_prototype.Spark.FanGetStatusTask;
import com.aga.hcp.home_control_prototype.Spark.FanToggleTask;

/**
 * Created by garygraham on 14-11-22.
 * Implementation of the Fan Widget
 */
public class FanWidget extends DeviceWidget {
    public static String listenerId = "fanwidget";

    public void performGetStatusTask(String id, OnTaskCompleted listener, Context context) {
        Log.i(TAG, "performGetStatusTask() -> Executing fan status task...");
        new FanGetStatusTask(id, listener, context).execute();
    }

    @Override
    public void performToggleTask(String id, OnTaskCompleted listener, Context context) {
        Log.i(TAG, "performToggleTask() -> Executing fan toggle task...");
        new FanToggleTask(id, listener, context).execute();
    }

    @Override
    public void registerForEvents(OnTaskCompleted listener, Context context) {
        Log.i(TAG, "registerForEvents() -> Registering for fan toggle updates. ...");
        FanToggleTask.registerForToggleEvent(listenerId, listener, context);
    }

    @Override
    public Intent getWidgetIntent(Context context) {
        Intent intent = new Intent(context, FanWidget.class);
        return intent;
    }

    @Override
    protected String getClassName() {
        return FanWidget.class.getName();
    }

    @Override
    protected Integer getWidgetLayout() {
        return R.layout.app_widget_layout_fan;
    }

    @Override
    protected Integer getWidgetImage() {
        return R.id.fan_widget_image;
    }

    @Override
    protected Integer getOnImage() {
        return R.drawable.fan_on_img;
    }

    @Override
    protected Integer getOffImage() {
        return R.drawable.fan_off_img;
    }

    @Override
    protected String getToggleAction() {
        return "ToggleFan";
    }

    @Override
    public boolean isRegistered() {
        return FanToggleTask.isRegistered(listenerId);

    }
}
