package in.iamkelv.balances.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import in.iamkelv.balances.R;
import in.iamkelv.balances.models.PreferencesModel;

public class ThresholdPickerPreference extends DialogPreference {

    private PreferencesModel mPreferences;
    private SeekBar mSeekBar;
    private TextView txtThreshold;
    private int mProgress;

    public ThresholdPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPreferences = new PreferencesModel(context);
        setDialogLayoutResource(R.layout.dialog_thresholdpicker);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        mSeekBar = (SeekBar) v.findViewById(R.id.seekBar);
        txtThreshold = (TextView) v.findViewById(R.id.txtThreshold);

        int progress = 0;
        if (getKey().equals("prefs_lunch")) {
            progress = mPreferences.getLunchThreshold();
            mSeekBar.setProgress(progress);
        } else if (getKey().equals("prefs_tuck")) {
            progress = mPreferences.getTuckThreshold();
            mSeekBar.setProgress(progress);
        }
        txtThreshold.setText(String.format(getContext().getString(R.string.thpicker_threshold),(progress == 0) ? "Disabled" : getContext().getString(R.string.pound_sign) + progress));

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtThreshold.setText(String.format(getContext().getString(R.string.thpicker_threshold),(progress == 0) ? "Disabled" : getContext().getString(R.string.pound_sign) + progress));
                mProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle(null);
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            if (getKey().equals("prefs_lunch")) {
                mPreferences.setLunchThreshold(mProgress);
            } else if (getKey().equals("prefs_tuck")) {
                mPreferences.setTuckThreshold(mProgress);
            }
        }
    }

}
