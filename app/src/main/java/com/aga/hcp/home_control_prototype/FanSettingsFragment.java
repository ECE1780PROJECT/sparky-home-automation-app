package com.aga.hcp.home_control_prototype;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;


public class FanSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "FanSettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fanpreferences);

        CheckBoxPreference disableGestures = (CheckBoxPreference) findPreference("fan_disable_pref");
        CheckBoxPreference backgroundPrefs = (CheckBoxPreference) findPreference("fan_background_services_pref");
        Preference gestureSelect = findPreference("fan_gesture_select_pref");
       // Preference signOut = findPreference("signout_pref");

        boolean isDisabled = getPreferenceManager().getSharedPreferences().getBoolean("fan_disable_pref", false);

        if (isDisabled) {
            gestureSelect.setEnabled(false);
        } else {
            gestureSelect.setEnabled(true);
        }




        backgroundPrefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Log.i(TAG, "backgroundprefs changed! Current value is " + o.toString());
                return true;
            }
        });
/*
        signOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                SharedPreferences.Editor edit = getPreferenceManager().getSharedPreferences().edit();
                edit.putBoolean("logged_in", false);
                edit.commit();
                getActivity().finish();
                return true;
            }
        });
        */

        disableGestures.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.i(TAG, "onPreferenceChange() -> (DisableGesturesPref)");
                Preference gestureSelect = findPreference("fan_gesture_select_pref");

                boolean isDisabled = getPreferenceManager().getSharedPreferences().getBoolean("fan_disable_pref", false);
                if (isDisabled) {
                    gestureSelect.setEnabled(false);
                } else {
                    gestureSelect.setEnabled(true);
                }

                return false;
            }
        });

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            EditTextPreference etp = (EditTextPreference) pref;
            pref.setSummary(etp.getText());
        }else if(pref instanceof ListPreference){
            ListPreference lp = (ListPreference)pref;
            pref.setSummary(lp.getEntry());
        }
    }
}
