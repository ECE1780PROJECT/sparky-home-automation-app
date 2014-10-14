package com.example.hcp.home_control_prototype;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.github.machinarius.preferencefragment.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    private static final String TAG = "SettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        CheckBoxPreference disableGestures = (CheckBoxPreference)findPreference("disable_pref");
        ListPreference gestureSelect = (ListPreference)findPreference("gesture_select_pref");
        Preference addGestures = findPreference("gesture_add_pref");

        boolean isDisabled = getPreferenceManager().getSharedPreferences().getBoolean("disable_pref", false);
        if(isDisabled){
            gestureSelect.setEnabled(false);
            addGestures.setEnabled(false);
        }else {
            gestureSelect.setEnabled(true);
            addGestures.setEnabled(true);
        }

        disableGestures.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.i(TAG, "onPreferenceChange() -> (DisableGesturesPref)");
                ListPreference gestureSelect = (ListPreference)findPreference("gesture_select_pref");
                Preference addGestures = findPreference("gesture_add_pref");

                boolean isDisabled = getPreferenceManager().getSharedPreferences().getBoolean("disable_pref", false);
                if(isDisabled){
                    gestureSelect.setEnabled(false);
                    addGestures.setEnabled(false);
                }else {
                    gestureSelect.setEnabled(true);
                    addGestures.setEnabled(true);
                }

                return false;
            }
        });
    }
}