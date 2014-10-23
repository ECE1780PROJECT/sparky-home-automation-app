package com.example.hcp.home_control_prototype;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hcp.home_control_prototype.Spark.Device;
import com.example.hcp.home_control_prototype.Spark.LightGetStatusTask;
import com.example.hcp.home_control_prototype.Spark.LightToggleTask;
import com.example.hcp.home_control_prototype.Spark.Spark;

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
public class ToggleLightFragment extends Fragment implements OnTaskCompleted, View.OnClickListener {


    private static final String TAG = "ToggleLightPage";

    public ToggleLightFragment() {
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
        bulbImg.setOnClickListener(this);
        LightToggleTask.registerForToggleEvent(this, this.getActivity().getBaseContext());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Device outlet = Spark.getInstance().getDeviceByName("Tadgh");
        if (outlet != null) {
            LightGetStatusTask gst = new LightGetStatusTask(outlet.getId(), this);
            gst.execute();
        }
    }

    /**
     * Handles all API requests in the widget. Essentially just changes the state of the widget based on the return code.
     */
    @Override
    public void onTaskCompleted(Object obj, Context context) {
        Log.i(TAG, "onTaskCompleted() -> received response in the widget class. ");
        JSONArray jArray = (JSONArray)obj;
        try{
            JSONObject jObject = jArray.getJSONObject(0);
            int status = jObject.getInt("return_value");
            switch(status){
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

    private void lightOn() {
        ImageView bulbImg = (ImageView) this.getView().findViewById(R.id.bulbImgFragment);
        bulbImg.setImageResource(R.drawable.bulb_on_img);
    }

    private void lightOff() {
        ImageView bulbImg = (ImageView) this.getView().findViewById(R.id.bulbImgFragment);
        bulbImg.setImageResource(R.drawable.bulb_off_img);
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG,"onClick() -> Entering Click handler for image bulb.");
        Device outlet = Spark.getInstance().getDeviceByName("Tadgh");
        LightToggleTask toggleTask = new LightToggleTask(outlet.getId(), this);
        toggleTask.execute();

    }
}
