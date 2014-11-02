package com.example.hcp.home_control_prototype;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionListener;
import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionService;
import com.example.hcp.home_control_prototype.gesture.classifier.Distribution;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ansonliang55 on 2014-10-16.
 */
public class SelectGestureActivity extends Activity {


    private static final String TAG = "SelectGestureActivity";
    private SharedPreferences gSharedPreferences;
    String[] default_gesture_list = {
            "BUMP RIGHT",
            "BUMP LEFT",
            "BUMP UP",
    } ;
    Integer[] imageId = {
            R.drawable.bumpr,
            R.drawable.bumpl,
            R.drawable.shake,
    };

    IGestureRecognitionService recognitionService;
    ArrayAdapter<String> adapter;

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            recognitionService = IGestureRecognitionService.Stub.asInterface(service);
            try {
                recognitionService.startClassificationMode(Global.trainingSet);
                recognitionService.registerListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
                List<String> items = recognitionService.getGestureList(Global.trainingSet);
                //setListAdapter(new ArrayAdapter<String>(AddGestureActivity.this, android.R.layout.simple_list_item_single_choice, items));
            } catch (RemoteException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            //ListView lv = getListView();
           // lv.setTextFilterEnabled(true);
           // registerForContextMenu(lv);

            //lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //         // When clicked, show a toast with the TextView text
            //        //Global.showToast(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_LONG);
             //       System.err.println(((TextView) view).getText());
            //    }
            //});
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            recognitionService = null;
        }
    };

    IBinder gestureListenerStub = new IGestureRecognitionListener.Stub() {

        @Override
        public void onGestureLearned(String gestureName) throws RemoteException {
            Global.showToast(SelectGestureActivity.this, String.format("Gesture %s learned", gestureName), Toast.LENGTH_SHORT);
            System.err.println("Gesture %s learned");
        }

        @Override
        public void onTrainingSetDeleted(String trainingSet) throws RemoteException {
            Global.showToast(SelectGestureActivity.this, String.format("Training set %s deleted", trainingSet), Toast.LENGTH_SHORT);
            System.err.println(String.format("Training set %s deleted", trainingSet));
        }

        @Override
        public void onGestureRecognized(final Distribution distribution) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Global.showToast(SelectGestureActivity.this, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()), Toast.LENGTH_LONG);
                    System.err.println(String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
                }
            });
        }
    };

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_gesture);
        gSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final CustomList adapter = new CustomList(SelectGestureActivity.this, default_gesture_list, imageId);
        adapter.setSelectedIndex(gSharedPreferences.getInt(Global.PREFERENCE_GESTURE_SELECT, 0));
        //Set<String> stringS = gSharedPreferences.getStringSet(Global.PREFERENCE_GESTURE_LIST, null);
       // if (stringS != null) {
        //    Log.i("TEST", stringS.toArray().toString());
        //}
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SharedPreferences.Editor editor = gSharedPreferences.edit();
                editor.clear();
                editor.putInt(Global.PREFERENCE_GESTURE_SELECT, position);
                Set<String> stringSet = new HashSet<String>(Arrays.asList(default_gesture_list));
                editor.putStringSet(Global.PREFERENCE_GESTURE_LIST, stringSet);
                editor.commit();
                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
            }
        });



    }
}
