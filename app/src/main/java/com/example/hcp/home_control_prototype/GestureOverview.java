package com.example.hcp.home_control_prototype;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hcp.home_control_prototype.gesture.IGestureRecognitionService;

import java.util.List;

/**
 * Created by ansonliang55 on 2014-10-13.
 */
public class GestureOverview extends ListActivity {

    String trainingSet;
    private IGestureRecognitionService recognitionService;
    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            recognitionService = IGestureRecognitionService.Stub.asInterface(service);
            try {

                List<String> items = recognitionService.getGestureList(trainingSet);
                setListAdapter(new ArrayAdapter<String>(GestureOverview.this, R.layout.gesture_item, items));
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            registerForContextMenu(lv);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_LONG).show();
                    System.err.println(((TextView) view).getText());
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            recognitionService = null;
        }
    };

    @Override
    protected void onResume() {
        trainingSet = getIntent().getExtras().get("trainingSetName").toString();
        Intent bindIntent = new Intent("com.example.hcp.home_control_prototype.gesture.GESTURE_RECOGNIZER");
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        super.onResume();
    }

    @Override
    protected void onPause() {
        recognitionService = null;
        unbindService(serviceConnection);
        super.onPause();
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
                recognitionService.deleteGesture(trainingSet, getListAdapter().getItem(info.position).toString());
                List<String> items = recognitionService.getGestureList(trainingSet);
                setListAdapter(new ArrayAdapter<String>(GestureOverview.this, R.layout.gesture_item, items));
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return true;

    }
}

