package com.example.hcp.home_control_prototype;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hcp.home_control_prototype.Spark.Spark;
import com.example.hcp.home_control_prototype.Spark.Token;

import java.util.ArrayList;
import java.util.HashSet;


public class LogoutActivity extends Activity {

    private static final String TAG = "LogoutActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() -> User initiated logout.");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();

        //here im removing both the preference tokens, and the actual tokens by the spark library.
        edit.putStringSet("tokens", new HashSet<String>());
        Spark.getInstance().replaceAllTokens(new ArrayList<Token>());

        edit.commit();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }


}
