package in.iamkelv.balances.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import in.iamkelv.balances.alarms.AlarmReceiver;
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
        updatePreferences();

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
        updatePreferences();
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

    private void updatePreferences() {
        boolean notificationState = getPreferenceManager().getSharedPreferences().getBoolean("notification_state", false);
        findPreference("prefs_time").setEnabled(notificationState);
        findPreference("prefs_lunch").setEnabled(notificationState);
        findPreference("prefs_tuck").setEnabled(notificationState);

        AlarmReceiver alarm = new AlarmReceiver();
        alarm.cancelAlarm(getActivity());
        if (notificationState) {
            alarm.setAlarm(getActivity());
        }

        findPreference("prefs_time").setSummary(String.format(getString(R.string.prefs_time_summary), mPreferences.getNotificationHours(), mPreferences.getNotificationMinutes()));
        findPreference("prefs_lunch").setSummary(String.format(getString(R.string.prefs_lunch_summary),(mPreferences.getLunchThreshold() == 0) ? "Disabled" : getString(R.string.pound_sign) + mPreferences.getLunchThreshold() + ".00"));
        findPreference("prefs_tuck").setSummary(String.format(getResources().getString(R.string.prefs_tuck_summary),(mPreferences.getTuckThreshold() == 0) ? "Disabled" : getString(R.string.pound_sign) + mPreferences.getTuckThreshold() + ".00"));
    }

}