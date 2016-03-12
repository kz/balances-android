package in.iamkelv.balances.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.iamkelv.balances.R;
import in.iamkelv.balances.preferences.ThresholdPickerPreference;
import in.iamkelv.balances.preferences.TimePickerPreference;

public class NotificationPreferencesFragment extends PreferenceFragment {

    SharedPreferences mSettings;
    SwitchPreference mEnabledPreference;
    TimePickerPreference mTimePreference;
    ThresholdPickerPreference mLunchPreference;
    ThresholdPickerPreference mTuckPreference;

    public NotificationPreferencesFragment() {
    }


    public static NotificationPreferencesFragment newInstance() {
        NotificationPreferencesFragment fragment = new NotificationPreferencesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.notification_preferences, false);
        mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        addPreferencesFromResource(R.xml.notification_preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Bind preferences
        mEnabledPreference = (SwitchPreference) findPreference(getString(R.string.preferences_notifications_enabled_key));
        mTimePreference = (TimePickerPreference) findPreference(getString(R.string.preferences_notifications_time_key));
        mLunchPreference = (ThresholdPickerPreference) findPreference(getString(R.string.preferences_notifications_lunch_key));
        mTuckPreference = (ThresholdPickerPreference) findPreference(getString(R.string.preferences_notifications_tuck_key));

        // Update preferences with stored data
        updateSummaries();

        mEnabledPreference.setOnPreferenceChangeListener(mEnabledPreferenceListener);

        return inflater.inflate(R.layout.fragment_notification_preferences, container, false);
    }

    protected void updateSummaries() {
        String enabledPreferenceSummary = getString(R.string.preferences_notifications_enabled_summary);
        Boolean enabledPreferenceValue = mSettings.getBoolean(mEnabledPreference.getKey(),
                Boolean.parseBoolean(getString(R.string.preferences_notifications_enabled_default)));
        String enabledPreferenceFormatValue = (enabledPreferenceValue.equals(true)) ?
                getString(R.string.preferences_notifications_enabled_true) :
                getString(R.string.preferences_notifications_enabled_false);
        mEnabledPreference.setSummary(String.format(enabledPreferenceSummary, enabledPreferenceFormatValue));

        String timePreferenceSummary = getString(R.string.preferences_notifications_time_summary);
        String timePreferenceValue = mSettings.getString(mTimePreference.getKey(), getString(R.string.preferences_notifications_time_default));
        mTimePreference.setSummary(String.format(timePreferenceSummary, timePreferenceValue));

        String lunchPreferenceSummary = getString(R.string.preferences_notifications_lunch_summary);
        String lunchPreferenceValue = mSettings.getString(mLunchPreference.getKey(), getString(R.string.preferences_notifications_lunch_default));
        mLunchPreference.setSummary(String.format(lunchPreferenceSummary, lunchPreferenceValue));

        String tuckPreferenceSummary = getString(R.string.preferences_notifications_tuck_summary);
        String tuckPreferenceValue = mSettings.getString(mTuckPreference.getKey(), getString(R.string.preferences_notifications_tuck_default));
        mTuckPreference.setSummary(String.format(tuckPreferenceSummary, tuckPreferenceValue));
    }

    // Preference change listeners
    Preference.OnPreferenceChangeListener mEnabledPreferenceListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String enabledPreferenceSummary = getString(R.string.preferences_notifications_enabled_summary);
            String enabledPreferenceFormatValue = (newValue.equals(true)) ?
                    getString(R.string.preferences_notifications_enabled_true) :
                    getString(R.string.preferences_notifications_enabled_false);
            mEnabledPreference.setSummary(String.format(enabledPreferenceSummary, enabledPreferenceFormatValue));
            return true;
        }
    };

}
