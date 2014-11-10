package com.aga.hcp.home_control_prototype.Spark;

import android.util.Log;

/**
 * Created by garygraham on 2014-10-14.
 */
public class Device {
    private static final String TAG = "Device";
    private String id;
    private String name;
    private boolean connected;
    private String lastHeard;

    public Device(String id, String name, boolean connected, String lastHeard){
        this.id = id;
        this.name = name;
        this.connected = connected;
        this.lastHeard = lastHeard;
        Log.i(TAG, "Constructor() -> " + this.id + " -- " + this.name);
    }

    public String getLastHeard() {
        return lastHeard;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getName() {
        return name;
    }

    public String getId() { return id; }

    @Override
    public String toString(){
        return this.name;
    }
}
