package com.example.hcp.home_control_prototype.Spark;

import android.content.Context;
import android.util.Log;

import com.example.hcp.home_control_prototype.OnTaskCompleted;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by garygraham on 2014-10-13.
 * A library to handle spark login and communications
 */
public class Spark implements OnTaskCompleted {
    private static final String TAG = "Spark";
    private static Spark instance = null;
    private String username;
    private String password;
    private ArrayList<Token> access_tokens;
    private ArrayList<Device> devices;
    private Token current_token;

    public static Spark getInstance() {
        if(instance == null) {
            instance = new Spark();
        }
        return instance;
    }
    //User provides nothing.
    protected Spark() {
        access_tokens = new ArrayList<Token>();
        devices = new ArrayList<Device>();
    }


    public ArrayList<String> listDeviceNames(){
        ArrayList<String> dev_names = new ArrayList<String>();
        Log.i(TAG, "listDevices() -> " + devices.toString());
        for(Device d: devices){
            dev_names.add(d.toString());
        }
        return dev_names;
    }


    public boolean login(String username, String password) {

        LoginTask loginTask = new LoginTask(this);

        try {
            loginTask.execute(username, password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return true;
    }


    public Token getCurrentToken(){
        Log.i(TAG, "getCurrentToken() -> providing token: " + current_token);
        if(current_token == null){
            rotateToken();
        }
        return current_token;
    }

    public void setCurrentToken(Token token) {
        current_token = token;
        Log.i(TAG, "setCurrentToken() -> " + " Setting Current token to : " + current_token);
        Log.i(TAG, "setCurrentToken() -> " + "Remaining Tokens: -> " + access_tokens.toString());
    }


    public void addTokenToList(Token token) {
        access_tokens.add(token);
    }

    public boolean rotateToken() {
        if (access_tokens.size() > 0) {
            setCurrentToken(access_tokens.remove(0));
            return true;
        } else {
            return false;
        }
    }

    public void replaceAllTokens(ArrayList<Token> newTokens){
        Log.i(TAG, "Wiping out old token set: ->" + access_tokens.toString());
        this.access_tokens.clear();
        this.access_tokens.addAll(newTokens);
        Log.i(TAG, "Replaced with new token set: ->" + access_tokens.toString());
    }

    public ArrayList<Token> getTokens() {
        Log.i(TAG, "getTokens() -> " + access_tokens.toString());
        return access_tokens;
    }

    public ArrayList<String> getTokenValues() {
        ArrayList<String> values = new ArrayList<String>();
        Log.i(TAG, "getTokenValues() -> " + access_tokens.toString());
        for(Token token: access_tokens){
            values.add(token.toString());
        }
        return values;

    }


    public ArrayList<Device> getDevices() {
        Log.i(TAG, "getDevices() -> " + devices);
        return devices;
    }
    public void addDevice(Device device) {
        if(!devices.contains(device)){
            devices.add(device);
        }
    }

    public void findDevices() {
        GetDevicesTask gdt = new GetDevicesTask(this);
        gdt.execute(current_token.getValue());
    }

    public Device getDeviceByName(String name){
        for(Device device: devices){
            Log.i(TAG, "Comparing :"+ name + " to known device: " + device.getName());
            if (device.getName().equals(name)){
                Log.i(TAG, "getDeviceByName() -> found device by name: " + name + ", returning it...");
                return device;
            }
        }
        Log.i(TAG, "getDeviceByName() -> Couldn't find device by the name: " + name);
        return null;
    }

    public void makeAPICall(Device device, String apiPath, OnTaskCompleted listener){
        if (devices.contains(device)){
            new SparkAPITask(device.getId(), apiPath, listener );
        }
    }

    @Override
    public void onTaskCompleted(Object obj, Context context) {

    }
}
