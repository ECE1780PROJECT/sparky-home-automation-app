package com.example.hcp.home_control_prototype.Spark;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by garygraham on 2014-10-13.
 * A library to handle spark login and communications
 */
public class Spark {
    private static final String TAG = "Spark";
    private String username;
    private String password;
    private ArrayList<Token> access_tokens;
    private ArrayList<Device> devices;
    private Token current_token;

    //User provides nothing.
    public Spark() {
        access_tokens = new ArrayList<Token>();

    }

    //User just straight-up provides access token
    public Spark(String access_token_value) {
        current_token = new Token(access_token_value, null);
        access_tokens = new ArrayList<Token>();
        access_tokens.add(current_token);

    }

    //User provides Username and Password.
    public Spark(String username, String password) {
        access_tokens = new ArrayList<Token>();
        devices = new ArrayList<Device>();
        login(username, password);

    }

    public ArrayList<Device> listDevices(){
        Log.i(TAG, "listDevices() -> " + devices.toString());
        return devices;
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
        loginTask.execute(username, password);
        return true;
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
        Log.i(TAG, "getDevices() -> " + devices.toString());
        return devices;
    }
    public void addDevice(Device device) {
        if(!devices.contains(device)){
            devices.add(device);
        }
    }

    public void addDeviceByID(String id){
        //Device new_device = new Device(id);

    }

    public void findDevices() {
        GetDevicesTask gdt = new GetDevicesTask(this);
        gdt.execute(current_token.getValue());
    }

    public Device getDeviceByName(String name){
        for(Device device: devices){
            if (device.getName().equals(name)){
                Log.i(TAG, "getDeviceByName() -> found device by name: " + name + ", returning it...");
                return device;
            }
        }
        Log.i(TAG, "getDeviceByName() -> Couldn't find device by the name: " + name);
        return null;
    }
}
