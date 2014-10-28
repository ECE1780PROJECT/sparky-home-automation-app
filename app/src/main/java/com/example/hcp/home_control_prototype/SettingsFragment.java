package com.example.hcp.home_control_prototype;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.github.machinarius.preferencefragment.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


        CheckBoxPreference disableGestures = (CheckBoxPreference) findPreference("disable_pref");
        Preference gestureSelect = findPreference("gesture_select_pref");
        Preference addGestures = findPreference("gesture_add_pref");
        Preference signOut = findPreference("signout_pref");


        boolean isDisabled = getPreferenceManager().getSharedPreferences().getBoolean("disable_pref", false);

        if (isDisabled) {
            gestureSelect.setEnabled(false);
            addGestures.setEnabled(false);
        } else {
            gestureSelect.setEnabled(true);
            addGestures.setEnabled(true);
        }
        signOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                SharedPreferences.Editor edit = getPreferenceManager().getSharedPreferences().edit();
                edit.putBoolean("logged_in", false);
                edit.commit();
                getActivity().finish();
                return false;
            }
        });
        disableGestures.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.i(TAG, "onPreferenceChange() -> (DisableGesturesPref)");
                Preference gestureSelect = findPreference("gesture_select_pref");
                Preference addGestures = findPreference("gesture_add_pref");

                boolean isDisabled = getPreferenceManager().getSharedPreferences().getBoolean("disable_pref", false);
                if (isDisabled) {
                    gestureSelect.setEnabled(false);
                    addGestures.setEnabled(false);
                } else {
                    gestureSelect.setEnabled(true);
                    addGestures.setEnabled(true);
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
