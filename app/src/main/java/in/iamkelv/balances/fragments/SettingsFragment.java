package in.iamkelv.balances.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import in.iamkelv.balances.R;
import in.iamkelv.balances.activities.SetupActivity;
import in.iamkelv.balances.models.PreferencesModel;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private PreferencesModel mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        getPreferenceManager().setSharedPreferencesName("BalancesPrefs");
        addPreferencesFromResource(R.xml.preferences);
        mPreferences = new PreferencesModel(getActivity());

        // Update preferences
        updatePreferenceEnabledState();
        updatePreferenceSummary();

        // Handle reset button click
        Preference resetPreference = findPreference("prefs_reset");
        resetPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick (Preference pref){
                mPreferences.clearPreferences();
                Intent setupIntent = new Intent(getActivity(), SetupActivity.class);
                getActivity().startActivity(setupIntent);
                getActivity().finish();
                return true;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("notification_state")) {
            updatePreferenceEnabledState();
        } else {
            updatePreferenceSummary();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private void updatePreferenceEnabledState() {
        boolean notificationState = getPreferenceManager().getSharedPreferences().getBoolean("notification_state", false);
        findPreference("prefs_time").setEnabled(notificationState);
        findPreference("prefs_lunch").setEnabled(notificationState);
        findPreference("prefs_tuck").setEnabled(notificationState);
    }

    private void updatePreferenceSummary() {
        findPreference("prefs_time").setSummary(String.format(getResources().getString(R.string.prefs_time_summary),mPreferences.getNotificationHours(),mPreferences.getNotificationMinutes()));
        findPreference("prefs_lunch").setSummary(String.format(getResources().getString(R.string.prefs_lunch_summary),mPreferences.getLunchThreshold()));
        findPreference("prefs_tuck").setSummary(String.format(getResources().getString(R.string.prefs_tuck_summary),mPreferences.getTuckThreshold()));
    }

}