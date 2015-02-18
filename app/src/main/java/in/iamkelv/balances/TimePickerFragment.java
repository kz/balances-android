package in.iamkelv.balances;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    SetupActivity mParent;

    public void setParent(SetupActivity parent) {
        mParent = parent;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get current values from mTxtScheduledTime
        SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(mParent.mTxtScheduledTime.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int hour = Calendar.HOUR_OF_DAY;
        int minute = Calendar.HOUR_OF_DAY;

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String mTime = String.format("%02d:%02d", hourOfDay, minute);
        mParent.mTxtScheduledTime.setText(mTime);
    }

}
