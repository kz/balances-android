package in.iamkelv.balances.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
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
        Boolean enabledPreferenceValue = mSettings.getBoolean(mEnabledPreference.getKey(),
                Boolean.parseBoolean(getString(R.string.preferences_notifications_enabled_default)));
        updateEnabledSummary(enabledPreferenceValue);

        String timePreferenceValue = mSettings.getString(mTimePreference.getKey(), getString(R.string.preferences_notifications_time_default));
        updateTimeSummary(timePreferenceValue);
        
        String lunchPreferenceValue = mSettings.getString(mLunchPreference.getKey(), getString(R.string.preferences_notifications_lunch_default));
        updateLunchSummary(lunchPreferenceValue);
        
        String tuckPreferenceValue = mSettings.getString(mTuckPreference.getKey(), getString(R.string.preferences_notifications_tuck_default));
        updateTuckSummary(tuckPreferenceValue);

        // Bind listeners      
        mEnabledPreference.setOnPreferenceChangeListener(mEnabledPreferenceListener);
        mTimePreference.setOnPreferenceChangeListener(mTimePreferenceListener);
        mLunchPreference.setOnPreferenceChangeListener(mLunchPreferenceListener);
        mTuckPreference.setOnPreferenceChangeListener(mTuckPreferenceListener);

        return inflater.inflate(R.layout.fragment_notification_preferences, container, false);
    }
    
    protected void updateEnabledSummary(Boolean preferenceValue) {
        String enabledPreferenceSummary = getString(R.string.preferences_notifications_enabled_summary);
        String enabledPreferenceFormatValue = (preferenceValue.equals(true)) ?
                getString(R.string.preferences_notifications_enabled_true) :
                getString(R.string.preferences_notifications_enabled_false);
        mEnabledPreference.setSummary(String.format(enabledPreferenceSummary, enabledPreferenceFormatValue));
    }
    
    protected void updateTimeSummary(String preferenceValue) {
        String timePreferenceSummary = getString(R.string.preferences_notifications_time_summary);
        mTimePreference.setSummary(String.format(timePreferenceSummary, preferenceValue));
    }
    
    protected void updateLunchSummary(String preferenceValue) {
        Integer threshold = Integer.valueOf(preferenceValue);
        if (threshold == 0) {
            mLunchPreference.setSummary(getActivity().getString(R.string.preferences_notifications_lunch_summary_disabled));
        } else {
            String lunchPreferenceSummary = getString(R.string.preferences_notifications_lunch_summary);
            mLunchPreference.setSummary(String.format(lunchPreferenceSummary, threshold));
        }
    }
    
    protected void updateTuckSummary(String preferenceValue) {
        Integer threshold = Integer.valueOf(preferenceValue);
        if (threshold == 0) {
            mTuckPreference.setSummary(getActivity().getString(R.string.preferences_notifications_tuck_summary_disabled));
        } else {
            String tuckPreferenceSummary = getString(R.string.preferences_notifications_tuck_summary);
            mTuckPreference.setSummary(String.format(tuckPreferenceSummary, threshold));
        }
    }

    // Preference change listeners
    Preference.OnPreferenceChangeListener mEnabledPreferenceListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            updateEnabledSummary((Boolean) newValue);
            return true;
        }
    };

    Preference.OnPreferenceChangeListener mTimePreferenceListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            updateTimeSummary((String) newValue);
            return true;
        }
    };

    Preference.OnPreferenceChangeListener mLunchPreferenceListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            updateLunchSummary((String) newValue);
            return true;
        }
    };

    Preference.OnPreferenceChangeListener mTuckPreferenceListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            updateTuckSummary((String) newValue);
            return true;
        }
    };

}
