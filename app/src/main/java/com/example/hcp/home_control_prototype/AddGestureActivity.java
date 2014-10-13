package com.example.hcp.home_control_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionListener;
import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionService;
import com.example.hcp.home_control_prototype.gesture.classifier.Distribution;


/**
 * Created by ansonliang55 on 2014-10-10.
 */
public class AddGestureActivity extends Activity {
    private static final String TAG = "AddGestureActivity";


    IGestureRecognitionService recognitionService;
    String activeTrainingSet;


    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            recognitionService = IGestureRecognitionService.Stub.asInterface(service);
            try {
                recognitionService.startClassificationMode(activeTrainingSet);
                recognitionService.registerListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
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
            Toast.makeText(AddGestureActivity.this, String.format("Gesture %s learned", gestureName), Toast.LENGTH_SHORT).show();
            System.err.println("Gesture %s learned");
        }

        @Override
        public void onTrainingSetDeleted(String trainingSet) throws RemoteException {
            Toast.makeText(AddGestureActivity.this, String.format("Training set %s deleted", trainingSet), Toast.LENGTH_SHORT).show();
            System.err.println(String.format("Training set %s deleted", trainingSet));
        }

        @Override
        public void onGestureRecognized(final Distribution distribution) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddGestureActivity.this, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()), Toast.LENGTH_LONG).show();
                    System.err.println(String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_gesture);
        final TextView activeTrainingSetText = (TextView) findViewById(R.id.activeTrainingSet);
        final EditText trainingSetText = (EditText) findViewById(R.id.trainingSetName);
        final EditText editText = (EditText) findViewById(R.id.gestureName);
        activeTrainingSet = editText.getText().toString();
        final Button startTrainButton = (Button) findViewById(R.id.trainButton);
        final Button deleteTrainingSetButton = (Button) findViewById(R.id.deleteTrainingSetButton);
        final Button changeTrainingSetButton = (Button) findViewById(R.id.startNewSetButton);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setMax(20);
        seekBar.setProgress(20);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                try {
                    recognitionService.setThreshold(progress / 10.0f);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        startTrainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (recognitionService != null) {
                    try {
                        if (!recognitionService.isLearning()) {
                            startTrainButton.setText("Stop Training");
                            editText.setEnabled(false);
                            deleteTrainingSetButton.setEnabled(false);
                            changeTrainingSetButton.setEnabled(false);
                            trainingSetText.setEnabled(false);
                            recognitionService.startLearnMode(activeTrainingSet, editText.getText().toString());
                        } else {
                            startTrainButton.setText("Start Training");
                            editText.setEnabled(true);
                            deleteTrainingSetButton.setEnabled(true);
                            changeTrainingSetButton.setEnabled(true);
                            trainingSetText.setEnabled(true);
                            recognitionService.stopLearnMode();
                        }
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        changeTrainingSetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                activeTrainingSet = trainingSetText.getText().toString();
                activeTrainingSetText.setText(activeTrainingSet);

                if (recognitionService != null) {
                    try {
                        recognitionService.startClassificationMode(activeTrainingSet);
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        deleteTrainingSetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddGestureActivity.this);
                builder.setMessage("You really want to delete the training set?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (recognitionService != null) {
                            try {
                                recognitionService.deleteTrainingSet(activeTrainingSet);
                            } catch (RemoteException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gesture_test_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_gestures:
                Intent editGesturesIntent = new Intent().setClass(this, GestureOverview.class);
                editGesturesIntent.putExtra("trainingSetName", activeTrainingSet);
                startActivity(editGesturesIntent);
                return true;

            default:
                return false;
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

}
