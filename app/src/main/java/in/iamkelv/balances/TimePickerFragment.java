package in.iamkelv.balances;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    SetupActivity mParent;

    public void setParent(SetupActivity parent) {
        mParent = parent;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get current values from mTxtScheduledTime
        TextView mTxtScheduledTimeHours = mParent.mTxtScheduledTimeHours;
        TextView mTxtScheduledTimeMinutes = mParent.mTxtScheduledTimeMinutes;
        int hour = Integer.parseInt(mTxtScheduledTimeHours.getText().toString());
        int minute = Integer.parseInt(mTxtScheduledTimeMinutes.getText().toString());

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hours = String.format("%02d", hourOfDay);
        String minutes = String.format("%02d", minute);
        mParent.mTxtScheduledTimeHours.setText(hours);
        mParent.mTxtScheduledTimeMinutes.setText(minutes);
        PreferencesModel preferences = new PreferencesModel(getActivity());
        preferences.setNotificationHours(hourOfDay);
        preferences.setNotificationMinutes(minute);
    }

}
