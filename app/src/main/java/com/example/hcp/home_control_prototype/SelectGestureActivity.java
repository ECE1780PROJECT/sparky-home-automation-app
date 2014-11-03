package com.example.hcp.home_control_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionListener;
import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionService;
import com.example.hcp.home_control_prototype.gesture.classifier.Distribution;

import java.util.ArrayList;
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
    List<String> default_gesture_list = new ArrayList<String>();
    List<Integer> image_list = new ArrayList<Integer>();

    IGestureRecognitionService recognitionService;

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            recognitionService = IGestureRecognitionService.Stub.asInterface(service);
            try {
                recognitionService.startClassificationMode(Global.trainingSet);
                recognitionService.registerListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
                List<String> items = recognitionService.getGestureList(Global.trainingSet);
                updateListView(items);

            } catch (RemoteException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
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
    CustomList adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_gesture);

        gSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        Integer[] imageId = {
                R.drawable.bumpr,
                R.drawable.bumpl,
                R.drawable.shake,
        };
        image_list.add(R.drawable.bumpr);
        image_list.add(R.drawable.bumpl);
        adapter = new CustomList(SelectGestureActivity.this, default_gesture_list, image_list);
        adapter.setSelectedIndex(gSharedPreferences.getInt(Global.PREFERENCE_GESTURE_SELECT, 0));
        //Set<String> stringS = gSharedPreferences.getStringSet(Global.PREFERENCE_GESTURE_LIST, null);
       // if (stringS != null) {
        //    Log.i("TEST", stringS.toArray().toString());
        //}
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setTextFilterEnabled(true);
        registerForContextMenu(list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SharedPreferences.Editor editor = gSharedPreferences.edit();
                editor.clear();
                editor.putInt(Global.PREFERENCE_GESTURE_SELECT, position);
                Set<String> stringSet = new HashSet<String>(default_gesture_list);
                editor.putStringSet(Global.PREFERENCE_GESTURE_LIST, stringSet);
                editor.commit();
                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
            }
        });


        final Button trainButton = (Button) findViewById(R.id.trainButton);
        trainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (recognitionService != null) {
                    try {
                        if (!recognitionService.isLearning()) {
                            startTrainingButton(trainButton);
                        } else {
                            stopTrainingButton(trainButton);
                        }
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void startTrainingButton(final Button trainButton) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(SelectGestureActivity.this);
        alert.setTitle("Gesture Name");
        alert.setMessage("Please enter a gesture name");

        // Set an EditText view to get user input
        final EditText input = new EditText(SelectGestureActivity.this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String gestureName = input.getText().toString();
                if(gestureName!=null && !gestureName.isEmpty()) {
                    try {
                        Global.showToast(SelectGestureActivity.this, "Listening Gesture, please make a gesture", Toast.LENGTH_SHORT);
                        trainButton.setText("Stop Training");
                        recognitionService.startLearnMode(Global.trainingSet, gestureName);//editText.getText().toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Global.showToast(SelectGestureActivity.this, "Please enter a none empty string", Toast.LENGTH_SHORT);

                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    private void updateListView(List<String> items) {

        adapter.clear();
        adapter.setList(items);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    private void stopTrainingButton(final Button trainButton){
        trainButton.setText("Start Training");
        try {
            recognitionService.stopLearnMode();
            List<String> items = recognitionService.getGestureList(Global.trainingSet);
            updateListView(items);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        try {
            recognitionService.unregisterListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        recognitionService = null;
        unbindService(serviceConnection);
        super.onPause();
    }

    @Override
    protected void onResume() {
        Intent bindIntent = new Intent("com.example.hcp.home_control_prototype.gesture.GESTURE_RECOGNIZER");
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        super.onResume();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(adapter.getGestureName(info.position));
        String[] menuItems = {"Delete"};

        for (int i = 0; i < menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == 0) {
            try {
                recognitionService.deleteGesture(Global.trainingSet, adapter.getGestureName(info.position));
                if(info.position < adapter.getSelectedIndex()) {
                    adapter.setSelectedIndex(adapter.getSelectedIndex()-1);
                    SharedPreferences.Editor editor = gSharedPreferences.edit();
                    editor.clear();
                    editor.putInt(Global.PREFERENCE_GESTURE_SELECT, adapter.getSelectedIndex());
                    editor.commit();
                    adapter.notifyDataSetChanged();
                }
                List<String> items = recognitionService.getGestureList(Global.trainingSet);
                updateListView(items);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return true;

    }

}
