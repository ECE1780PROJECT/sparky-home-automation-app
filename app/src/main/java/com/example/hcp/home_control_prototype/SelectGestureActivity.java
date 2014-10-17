package com.example.hcp.home_control_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by ansonliang55 on 2014-10-16.
 */
public class SelectGestureActivity extends Activity {

    private SharedPreferences gSharedPreferences;
    String[] default_gesture_list = {
            "BUMP RIGHT",
            "BUMP LEFT",
            "BUMP UP",
    } ;
    Integer[] imageId = {
            R.drawable.BumpR,
            R.drawable.BumpL,
            R.drawable.Handshake,
    };
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_gesture);
        gSharedPreferences = getSharedPreferences(Global.PREFERENCE_GLOBAL, MODE_PRIVATE);

        final CustomList adapter = new CustomList(SelectGestureActivity.this, default_gesture_list, imageId);
        adapter.setSelectedIndex(gSharedPreferences.getInt(Global.PREFERENCE_GESTURE_SELECT, 0));
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SharedPreferences.Editor editor = gSharedPreferences.edit();
                editor.putInt(Global.PREFERENCE_GESTURE_SELECT, position);
                editor.commit();
                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
            }
        });



    }
}
