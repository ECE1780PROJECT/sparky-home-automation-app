package com.example.hcp.home_control_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionListener;
import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionService;
import com.example.hcp.home_control_prototype.gesture.classifier.Distribution;

import java.util.List;


/**
 * Created by ansonliang55 on 2014-10-10.
 */
public class AddGestureActivity extends ListActivity {
    private static final String TAG = "AddGestureActivity";


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
                setListAdapter(new ArrayAdapter<String>(AddGestureActivity.this, android.R.layout.simple_list_item_single_choice, items));
            } catch (RemoteException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            registerForContextMenu(lv);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    //Global.showToast(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_LONG);
                    System.err.println(((TextView) view).getText());
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            recognitionService = null;
        }
    };

    IBinder gestureListenerStub = new IGestureRecognitionListener.Stub() {

        @Override
        public void onGestureLearned(String gestureName) throws RemoteException {
            Global.showToast(AddGestureActivity.this, String.format("Gesture %s learned", gestureName), Toast.LENGTH_SHORT);
            System.err.println("Gesture %s learned");
        }

        @Override
        public void onTrainingSetDeleted(String trainingSet) throws RemoteException {
            Global.showToast(AddGestureActivity.this, String.format("Training set %s deleted", trainingSet), Toast.LENGTH_SHORT);
            System.err.println(String.format("Training set %s deleted", trainingSet));
        }

        @Override
        public void onGestureRecognized(final Distribution distribution) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Global.showToast(AddGestureActivity.this, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()), Toast.LENGTH_LONG);
                    System.err.println(String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_gesture_enhanced);
        //final TextView activeTrainingSetText = (TextView) findViewById(R.id.activeTrainingSet);
        //final EditText trainingSetText = (EditText) findViewById(R.id.trainingSetName);
        //final EditText editText = (EditText) findViewById(R.id.gestureName);
        final Button startTrainButton = (Button) findViewById(R.id.trainButton);
        //final Button deleteTrainingSetButton = (Button) findViewById(R.id.deleteTrainingSetButton);
        //final Button changeTrainingSetButton = (Button) findViewById(R.id.startNewSetButton);
        //final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar1);
        //seekBar.setVisibility(View.INVISIBLE);
        //seekBar.setMax(20);
        //seekBar.setProgress(20);
        //seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

       //     @Override
 //           public void onStopTrackingTouch(SeekBar seekBar) {
   //             // TODO Auto-generated method stub
//
  //          }

    //        @Override
        //    public void onStartTrackingTouch(SeekBar seekBar) {
      //          // TODO Auto-generated method stub

       //     }

//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                try {
//                    recognitionService.setThreshold(progress / 10.0f);
//                } catch (RemoteException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//        });
        startTrainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (recognitionService != null) {
                    try {
                        if (!recognitionService.isLearning()) {

//                            editText.setEnabled(false);
//                            deleteTrainingSetButton.setEnabled(false);
//                            changeTrainingSetButton.setEnabled(false);
//                            trainingSetText.setEnabled(false);

                            final AlertDialog.Builder alert = new AlertDialog.Builder(AddGestureActivity.this);
                            alert.setTitle("Gesture Name");
                            alert.setMessage("Please enter a gesture name");

                            // Set an EditText view to get user input
                            final EditText input = new EditText(AddGestureActivity.this);
                            alert.setView(input);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String gestureName = input.getText().toString();
                                    if(gestureName!=null && !gestureName.isEmpty()) {
                                        try {
                                            Global.showToast(AddGestureActivity.this, "Listening Gesture, please make a gesture", Toast.LENGTH_LONG);
                                            startTrainButton.setText("Stop Training");
                                            recognitionService.startLearnMode(Global.trainingSet, gestureName);//editText.getText().toString());
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Global.showToast(AddGestureActivity.this, "Please enter a none empty string", Toast.LENGTH_LONG);

                                    }

                                }
                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Canceled.
                                }
                            });
                            alert.show();


                        } else {
                            startTrainButton.setText("Start Training");
//                            editText.setEnabled(true);
//                            deleteTrainingSetButton.setEnabled(true);
//                            changeTrainingSetButton.setEnabled(true);
//                            trainingSetText.setEnabled(true);


                            recognitionService.stopLearnMode();
                            List<String> items = recognitionService.getGestureList(Global.trainingSet);
                            setListAdapter(new ArrayAdapter<String>(AddGestureActivity.this, android.R.layout.simple_list_item_single_choice, items));
                        }
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });



//        changeTrainingSetButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                activeTrainingSet = trainingSetText.getText().toString();
//                activeTrainingSetText.setText(activeTrainingSet);
//
//                if (recognitionService != null) {
//                    try {
//                        recognitionService.startClassificationMode(activeTrainingSet);
//                    } catch (RemoteException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

//        deleteTrainingSetButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(AddGestureActivity.this);
//                builder.setMessage("You really want to delete the training set?"+activeTrainingSet).setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        if (recognitionService != null) {
//                            try {
//                                recognitionService.deleteTrainingSet(activeTrainingSet);
//                            } catch (RemoteException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//                builder.create().show();
//            }
//        });




    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(getListAdapter().getItem(info.position).toString());
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
                recognitionService.deleteGesture(Global.trainingSet, getListAdapter().getItem(info.position).toString());
                List<String> items = recognitionService.getGestureList(Global.trainingSet);
                setListAdapter(new ArrayAdapter<String>(AddGestureActivity.this, android.R.layout.simple_list_item_single_choice, items));
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return true;

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



}
