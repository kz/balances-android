package in.iamkelv.balances;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerPreference extends DialogPreference implements TimePicker.OnTimeChangedListener {

    private PreferencesModel mPreferences;

    public TimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPreferences = new PreferencesModel(context);

        setDialogLayoutResource(R.layout.dialog_timepicker);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View v) {
        TimePicker timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        int hours = mPreferences.getNotificationHours();
        int minutes = mPreferences.getNotificationMinutes();
        timePicker.setCurrentHour(hours);
        timePicker.setCurrentMinute(minutes);

        super.onBindDialogView(v);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle(null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {

        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        // TODO: FIX THIS
        mPreferences.setNotificationHours(hourOfDay);
        mPreferences.setNotificationMinutes(minute);
    }

}
