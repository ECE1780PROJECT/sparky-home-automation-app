package com.example.hcp.home_control_prototype;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Created by ansonliang55 on 2014-10-10.
 */
public class WiiGeeTestActivity extends Activity {
    private static final String TAG = "WiiGeeTestActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wiigee_test);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wiigee_menu, menu);
        return true;
    }
}
