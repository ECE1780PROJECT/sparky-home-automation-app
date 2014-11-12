package com.aga.hcp.home_control_prototype;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.aga.hcp.home_control_prototype.Spark.Device;
import com.aga.hcp.home_control_prototype.Spark.FanGetStatusTask;
import com.aga.hcp.home_control_prototype.Spark.FanToggleTask;
import com.aga.hcp.home_control_prototype.Spark.LightGetStatusTask;
import com.aga.hcp.home_control_prototype.Spark.LightToggleTask;
import com.aga.hcp.home_control_prototype.Spark.Spark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 *
 */
public class ToggleFragment extends Fragment {


    private static final String TAG = "ToggleLightFragment";
    private OnTaskCompleted lightListener, fanListener;
    private View view;
    private Animation spinningFan;

    public ToggleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_toggle_light_page, container, false);
        ImageView bulbImg = (ImageView)rootView.findViewById(R.id.bulbImgFragment);
        final ImageView bulbSettings = (ImageView)rootView.findViewById(R.id.bulb_settings);

        spinningFan = AnimationUtils.loadAnimation(this.getActivity(), R.anim.clockwise_anim);

        spinningFan.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG, "onAnimationEnd() -> animation has ended. Turning off fan.");
                fanOff();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //listeners for toggle responses
        if(fanListener == null && lightListener == null){
            fanListener = new FanToggleListener();
            lightListener = new LightToggleListener();
        }


        bulbImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"onClick() -> Entering Click handler for image bulb.");
                Device outlet = Spark.getInstance().getDeviceByName("Tadgh");
                LightToggleTask toggleTask = new LightToggleTask(outlet.getId(), lightListener);
                toggleTask.execute();

            }
        });

        bulbSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"onClick() -> Entering Settings for bulb.");
                Intent settingsIntent = new Intent(ToggleFragment.this.getActivity(), SetPreferenceActivitiy.class);
                settingsIntent.putExtra("device", Global.LIGHT_NAME);
                startActivity(settingsIntent);
            }
        });

        ImageView fanImg = (ImageView)rootView.findViewById(R.id.fanImage);
        ImageView fanSettings = (ImageView)rootView.findViewById(R.id.fan_settings);

        fanImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"onClick() -> Entering Click handler for fan");
                Device outlet = Spark.getInstance().getDeviceByName("Tadgh");
                FanToggleTask toggleTask = new FanToggleTask(outlet.getId(), fanListener);
                toggleTask.execute();
            }
        });

        fanSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"onClick() -> Entering Settings for bulb.");
                Intent settingsIntent = new Intent(ToggleFragment.this.getActivity(), SetPreferenceActivitiy.class);
                settingsIntent.putExtra("device", Global.FAN_NAME);
                startActivity(settingsIntent);
            }
        });


        FanToggleTask.registerForToggleEvent(new FanToggleListener(), this.getActivity().getBaseContext());
        LightToggleTask.registerForToggleEvent(new LightToggleListener(), this.getActivity().getBaseContext());

        view = rootView;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated() -> entering." );
        Device outlet = Spark.getInstance().getDeviceByName("Tadgh");
        if (outlet != null) {
            Log.i(TAG, "onViewCreated() -> have device, sending off status request.");
            LightGetStatusTask lightStatus = new LightGetStatusTask(outlet.getId(), lightListener);
            FanGetStatusTask fanStatus = new FanGetStatusTask(outlet.getId(), fanListener);
            lightStatus.execute();
            fanStatus.execute();
        }else{
            Log.e(TAG, "onViewCreated() -> Could not find device, not sending status request.");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    private void showTimeOutToast() {
        Global.showToast(getActivity(), "Couldn't communicate with Spark core!!", Toast.LENGTH_SHORT);
    }

    private void lightOn() {
        ImageView bulbImg = (ImageView) view.findViewById(R.id.bulbImgFragment);
        bulbImg.setImageResource(R.drawable.bulb_on_img);
    }

    private void lightOff() {
        ImageView bulbImg = (ImageView) view.findViewById(R.id.bulbImgFragment);
        bulbImg.setImageResource(R.drawable.bulb_off_img);
    }

    private void fanOn() {
        ImageView bulbImg = (ImageView) view.findViewById(R.id.fanImage);
        bulbImg.setImageResource(R.drawable.fan_on_img);
        bulbImg.startAnimation(spinningFan);
    }

    private void fanOff() {
        ImageView bulbImg = (ImageView) view.findViewById(R.id.fanImage);
        bulbImg.setImageResource(R.drawable.fan_off_img);
        bulbImg.clearAnimation();
    }

    private class FanToggleListener implements OnTaskCompleted {
        @Override
        public void onTaskCompleted(Object obj, Context context) {
            Log.i(TAG, "onTaskCompleted() -> received response in the fragment class. ");
            if(obj == null){
                showTimeOutToast();
            }else {

                JSONArray jArray = (JSONArray) obj;
                try {
                    JSONObject jObject = jArray.getJSONObject(0);
                    int status = jObject.getInt("return_value");
                    switch (status) {
                        case 0:
                            Log.i(TAG, "Fan is off!");
                            spinningFan.setRepeatCount(0);
                            break;
                        case 1:
                            Log.i(TAG, "Fan is on!");
                            spinningFan.setRepeatCount(Animation.INFINITE);
                            fanOn();
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
    }

    private class LightToggleListener implements OnTaskCompleted {
        @Override
        public void onTaskCompleted(Object obj, Context context) {
            Log.i(TAG, "onTaskCompleted() -> received response in the fragment class. ");
            if(obj == null){
                showTimeOutToast();
            }else {

                JSONArray jArray = (JSONArray) obj;
                try {
                    JSONObject jObject = jArray.getJSONObject(0);
                    int status = jObject.getInt("return_value");
                    switch (status) {
                        case 0:
                            Log.i(TAG, "Light is off!");
                            lightOff();
                            break;
                        case 1:
                            Log.i(TAG, "Light is on!");
                            lightOn();
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
    }
}
