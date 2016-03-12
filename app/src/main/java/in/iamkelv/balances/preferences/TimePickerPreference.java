package in.iamkelv.balances.preferences;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import in.iamkelv.balances.R;
import in.iamkelv.balances.helpers.Helpers;

public class TimePickerPreference extends DialogPreference {

    SharedPreferences mSettings;
    String mTimePreferenceValue;
    TimePicker mTimePicker;

    public TimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_timepicker);

        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        mTimePreferenceValue = mSettings.getString(getKey(), "18:00");
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);

        if (restorePersistedValue) {
            persistString(getPersistedString(mTimePreferenceValue));
        } else {
            persistString((String) defaultValue);
        }
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);

        // Set the current time
        int hours = Helpers.parseHoursFromTime(mTimePreferenceValue);
        int minutes = Helpers.parseMinutesFromTime(mTimePreferenceValue);

        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hours);
            mTimePicker.setMinute(minutes);
        } else {
            mTimePicker.setCurrentHour(hours);
            mTimePicker.setCurrentMinute(minutes);
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                persistString(Helpers.createTimeString(mTimePicker.getHour(), mTimePicker.getMinute()));
            } else {
                persistString(Helpers.createTimeString(mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute()));
            }
            notifyChanged();
        }
    }
}
