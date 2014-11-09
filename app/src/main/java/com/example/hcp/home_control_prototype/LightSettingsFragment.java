package com.example.hcp.home_control_prototype;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.Log;
import android.preference.PreferenceFragment;


public class LightSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "LightSettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.lightpreferences);

        CheckBoxPreference disableGestures = (CheckBoxPreference) findPreference("light_disable_pref");
        CheckBoxPreference backgroundPrefs = (CheckBoxPreference) findPreference("light_background_services_pref");
        Preference gestureSelect = findPreference("light_gesture_select_pref");
      //  Preference signOut = findPreference("signout_pref");

        boolean isDisabled = getPreferenceManager().getSharedPreferences().getBoolean("light_disable_pref", false);

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


        disableGestures.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.i(TAG, "onPreferenceChange() -> (DisableGesturesPref)");
                Preference gestureSelect = findPreference("light_gesture_select_pref");
                Preference addGestures = findPreference("light_gesture_add_pref");

                boolean isDisabled = getPreferenceManager().getSharedPreferences().getBoolean("light_disable_pref", false);
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
