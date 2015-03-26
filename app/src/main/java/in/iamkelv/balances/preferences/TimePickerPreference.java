package in.iamkelv.balances.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import in.iamkelv.balances.R;
import in.iamkelv.balances.models.PreferencesModel;

public class TimePickerPreference extends DialogPreference {

    private PreferencesModel mPreferences;
    private TimePicker mTimePicker;

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
        super.onBindDialogView(v);

        mTimePicker = (TimePicker) v.findViewById(R.id.timePicker);
        int hours = mPreferences.getNotificationHours();
        int minutes = mPreferences.getNotificationMinutes();
        mTimePicker.setCurrentHour(hours);
        mTimePicker.setCurrentMinute(minutes);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle(null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            mPreferences.setNotificationHours(mTimePicker.getCurrentHour());
            mPreferences.setNotificationMinutes(mTimePicker.getCurrentMinute());
        }
    }

}
