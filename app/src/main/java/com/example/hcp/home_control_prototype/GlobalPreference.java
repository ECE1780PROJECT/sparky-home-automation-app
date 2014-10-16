package com.example.hcp.home_control_prototype;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ansonliang55 on 2014-10-16.
 */
public class GlobalPreference {
    private static GlobalPreference mInstance;
    private Context mContext;
    //
    private static SharedPreferences globalPreference;

    private GlobalPreference(){ }

    public static GlobalPreference getInstance(){
        if (mInstance == null) mInstance = new GlobalPreference();
        return mInstance;
    }

    public void Initialize(Context ctxt){
        mContext = ctxt;
        //
        globalPreference = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public static void setPreference(String key, String value){
        SharedPreferences.Editor e = globalPreference.edit();
        e.putString(key, value);
        e.commit();
    }

    public static String getPreference(String key)
    {
        return globalPreference.getString(key, "NA");
    }
}
