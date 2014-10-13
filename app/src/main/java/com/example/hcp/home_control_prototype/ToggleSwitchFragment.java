package com.example.hcp.home_control_prototype;

import android.app.Activity;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;


public class ToggleSwitchFragment extends Fragment {

    private static final String TAG = "ToggleSwitchFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_toggle_switch, container, false);

        //Grabbing all the buttons.
        Button turnOn = (Button)rootView.findViewById(R.id.btn_turn_on);
        Button turnOff = (Button)rootView.findViewById(R.id.btn_turn_off);
        Button status = (Button)rootView.findViewById(R.id.btn_status);

        //Assigning the button listeners. This has to be done in code due to the way that
        //support fragments work. It can't be assigned in XML as the XML only listens to the
        //parent activity, and not to the fragments.
        turnOn.setOnClickListener(new turnOnButtonHandler());
        turnOff.setOnClickListener(new turnOffButtonHandler());
        status.setOnClickListener(new statusButtonHandler());

        return rootView;
    }


    private class turnOnButtonHandler implements View.OnClickListener, OnTaskCompleted {

        @Override
        public void onClick(View view) {
            Log.i(TAG, "turnOnButtonHandler() -> Started.");
            TurnOnTask on = new TurnOnTask(this);
            on.execute();
        }


        @Override
        public void onTaskCompleted(Object obj, Context context) {
            Log.i(TAG, "onTaskCompleted() -> Received response from TurnOnTask: " + obj.toString());
        }
    }

    private class turnOffButtonHandler implements View.OnClickListener, OnTaskCompleted {

        @Override
        public void onClick(View view) {
            Log.i(TAG, "turnOffButtonHandler() -> Started.");
            TurnOffTask off = new TurnOffTask(this);
            off.execute();
        }

        @Override
        public void onTaskCompleted(Object obj, Context context) {
            Log.i(TAG, "onTaskCompleted() -> Received response from TurnOffTask: " + obj.toString());
        }
    }

    private class statusButtonHandler implements View.OnClickListener, OnTaskCompleted {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "statusButtonHandler() -> Started.");
            GetStatusTask status = new GetStatusTask(this);
            status.execute();
        }

        @Override
        public void onTaskCompleted(Object obj, Context context) {
            Log.i(TAG, "onTaskCompleted() -> Received response from GetStatusTask: " + obj.toString());
        }
    }
}
