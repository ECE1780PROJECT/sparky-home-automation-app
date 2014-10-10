package com.example.hcp.home_control_prototype;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;

import org.json.JSONArray;


public class ToggleSwitchActivity extends Activity implements OnTaskCompleted {

    private static final String TAG = "ToggleSwitchActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle_switch);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toggle_switch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(Object obj) {
        JSONArray jArray = (JSONArray)obj;

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_toggle_switch, container, false);
            return rootView;
        }
    }


    public void turnOnButtonHandler(View v){
        Log.i(TAG, "turnOnButtonHandler() -> Started.");
        TurnOnTask on = new TurnOnTask(this);
        on.execute();
    }


    public void turnOffButtonHandler(View v){
        Log.i(TAG, "turnOffButtonHandler() -> Started.");
        TurnOffTask off = new TurnOffTask(this);
        off.execute();
    }

    public void statusButtonHandler(View v){
        Log.i(TAG, "statusButtonHandler() -> Started.");
        GetStatusTask status = new GetStatusTask(this);
        status.execute();
    }
}
